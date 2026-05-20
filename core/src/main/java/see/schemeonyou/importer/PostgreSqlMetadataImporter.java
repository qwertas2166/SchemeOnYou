package see.schemeonyou.importer;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

/** Headless PostgreSQL metadata importer for MVP table/column/PK/FK import. */
public class PostgreSqlMetadataImporter {
    static final String CONNECT_TIMEOUT_SECONDS = "10";
    static final String LOGIN_TIMEOUT_SECONDS = "10";
    static final String SOCKET_TIMEOUT_SECONDS = "30";

    public DbImportResult importProfile(ConnectionProfile profile, String schemaOrCatalog) throws DbMetadataImportException {
        String schema = cleanSchema(schemaOrCatalog, profile == null ? null : profile.defaultSchemaOrCatalog());
        if (profile == null) throw new DbMetadataImportException("Connection profile is required");
        Properties properties = jdbcProperties(profile);
        String url = "jdbc:postgresql://" + profile.host() + ":" + profile.port() + "/" + profile.database();
        try (Connection connection = DriverManager.getConnection(url, properties)) {
            return importConnection(connection, schema);
        } catch (SQLException e) {
            throw new DbMetadataImportException("Could not read PostgreSQL metadata for profile '" + profile.displayName() + "'", e);
        }
    }

    static Properties jdbcProperties(ConnectionProfile profile) {
        Properties properties = new Properties();
        properties.setProperty("user", profile.user());
        properties.setProperty("password", profile.password());
        properties.setProperty("connectTimeout", CONNECT_TIMEOUT_SECONDS);
        properties.setProperty("loginTimeout", LOGIN_TIMEOUT_SECONDS);
        properties.setProperty("socketTimeout", SOCKET_TIMEOUT_SECONDS);
        return properties;
    }

    public DbImportResult importConnection(Connection connection, String schemaOrCatalog) throws DbMetadataImportException {
        try {
            return importMetadata(connection.getMetaData(), cleanSchema(schemaOrCatalog, null));
        } catch (SQLException e) {
            throw new DbMetadataImportException("Could not read PostgreSQL metadata", e);
        }
    }

    public DbImportResult importMetadata(DatabaseMetaData metaData, String schemaOrCatalog) throws DbMetadataImportException {
        String schema = cleanSchema(schemaOrCatalog, null);
        if (schema.isEmpty()) throw new DbMetadataImportException("PostgreSQL schema/catalog is required");
        try {
            List<String> tableNames = readTableNames(metaData, schema);
            if (tableNames.isEmpty()) throw new DbMetadataImportException("No tables found in PostgreSQL schema/catalog '" + schema + "'");
            Map<String, List<String>> pkByTable = readPrimaryKeys(metaData, schema, tableNames);
            Map<String, List<DbImportColumn>> columnsByTable = readColumns(metaData, schema, pkByTable);
            List<DbImportTable> tables = new ArrayList<>();
            for (String tableName : tableNames) {
                List<DbImportColumn> columns = new ArrayList<>(columnsByTable.getOrDefault(tableName, List.of()));
                columns.sort(Comparator.comparingInt(DbImportColumn::ordinalPosition).thenComparing(DbImportColumn::name));
                tables.add(new DbImportTable(tableName, columns, pkByTable.getOrDefault(tableName, List.of())));
            }
            ForeignKeyImport foreignKeyImport = readForeignKeys(metaData, schema, tableNames);
            return new DbImportResult(schema, tables, foreignKeyImport.foreignKeys(), foreignKeyImport.warnings());
        } catch (SQLException e) {
            throw new DbMetadataImportException("Could not map PostgreSQL metadata for schema/catalog '" + schema + "'", e);
        }
    }

    private static List<String> readTableNames(DatabaseMetaData metaData, String schema) throws SQLException {
        List<String> result = new ArrayList<>();
        try (ResultSet rs = metaData.getTables(null, schema, "%", new String[] {"TABLE"})) {
            while (rs.next()) result.add(rs.getString("TABLE_NAME"));
        }
        result.sort(String::compareTo);
        return result;
    }

    private static Map<String, List<String>> readPrimaryKeys(DatabaseMetaData metaData, String schema, List<String> tableNames) throws SQLException {
        Map<String, List<PrimaryKeyColumn>> rawByTable = new HashMap<>();
        for (String tableName : tableNames) {
            List<PrimaryKeyColumn> columns = new ArrayList<>();
            try (ResultSet rs = metaData.getPrimaryKeys(null, schema, tableName)) {
                while (rs.next()) columns.add(new PrimaryKeyColumn(rs.getString("COLUMN_NAME"), safeShort(rs)));
            }
            rawByTable.put(tableName, columns);
        }

        Map<String, List<String>> result = new HashMap<>();
        for (String tableName : tableNames) {
            List<String> orderedColumns = rawByTable.getOrDefault(tableName, List.of()).stream()
                    .sorted(Comparator.comparingInt(PrimaryKeyColumn::keySequence)
                            .thenComparing(PrimaryKeyColumn::name))
                    .map(PrimaryKeyColumn::name)
                    .distinct()
                    .toList();
            result.put(tableName, orderedColumns);
        }
        return result;
    }

    private static Map<String, List<DbImportColumn>> readColumns(DatabaseMetaData metaData, String schema, Map<String, List<String>> pkByTable) throws SQLException {
        Map<String, List<DbImportColumn>> result = new LinkedHashMap<>();
        try (ResultSet rs = metaData.getColumns(null, schema, "%", "%")) {
            while (rs.next()) {
                String tableName = rs.getString("TABLE_NAME");
                String columnName = rs.getString("COLUMN_NAME");
                String typeName = rs.getString("TYPE_NAME");
                int nullableFlag = rs.getInt("NULLABLE");
                int ordinal = rs.getInt("ORDINAL_POSITION");
                boolean nullable = nullableFlag != DatabaseMetaData.columnNoNulls;
                boolean primaryKey = pkByTable.getOrDefault(tableName, List.of()).contains(columnName);
                result.computeIfAbsent(tableName, ignored -> new ArrayList<>())
                        .add(new DbImportColumn(tableName, columnName, typeName, nullable, primaryKey, ordinal));
            }
        }
        return result;
    }

    private static ForeignKeyImport readForeignKeys(DatabaseMetaData metaData, String schema, List<String> tableNames) throws SQLException {
        Map<ForeignKeyGroupKey, List<DbImportForeignKey>> grouped = new LinkedHashMap<>();
        for (String tableName : tableNames) {
            try (ResultSet rs = metaData.getImportedKeys(null, schema, tableName)) {
                while (rs.next()) {
                    DbImportForeignKey foreignKey = new DbImportForeignKey(
                            nullToEmpty(rs.getString("FK_NAME")),
                            rs.getString("FKTABLE_NAME"),
                            rs.getString("FKCOLUMN_NAME"),
                            rs.getString("PKTABLE_NAME"),
                            rs.getString("PKCOLUMN_NAME"),
                            safeShort(rs)
                    );
                    grouped.computeIfAbsent(ForeignKeyGroupKey.of(foreignKey), ignored -> new ArrayList<>()).add(foreignKey);
                }
            }
        }

        List<DbImportForeignKey> foreignKeys = new ArrayList<>();
        List<String> warnings = new ArrayList<>();
        grouped.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> {
                    List<DbImportForeignKey> group = new ArrayList<>(entry.getValue());
                    group.sort(Comparator.comparingInt(DbImportForeignKey::keySequence)
                            .thenComparing(DbImportForeignKey::sourceColumn)
                            .thenComparing(DbImportForeignKey::targetColumn));
                    if (group.size() == 1) {
                        foreignKeys.add(group.getFirst());
                    } else {
                        warnings.add(compositeForeignKeyWarning(entry.getKey(), group));
                    }
                });
        foreignKeys.sort(Comparator.comparing(DbImportForeignKey::sourceTable)
                .thenComparing(DbImportForeignKey::name)
                .thenComparing(DbImportForeignKey::keySequence)
                .thenComparing(DbImportForeignKey::sourceColumn));
        warnings.sort(String::compareTo);
        return new ForeignKeyImport(foreignKeys, warnings);
    }

    private static String compositeForeignKeyWarning(ForeignKeyGroupKey key, List<DbImportForeignKey> group) {
        String sourceColumns = group.stream().map(DbImportForeignKey::sourceColumn).collect(Collectors.joining(", "));
        String targetColumns = group.stream().map(DbImportForeignKey::targetColumn).collect(Collectors.joining(", "));
        String name = key.name().isBlank() ? "<unnamed>" : key.name();
        return "Composite foreign key '" + name + "' from " + key.sourceTable() + "(" + sourceColumns + ") to "
                + key.targetTable() + "(" + targetColumns + ") was skipped: MVP imports only single-column FK edges.";
    }

    private static int safeShort(ResultSet rs) throws SQLException {
        try {
            return rs.getShort("KEY_SEQ");
        } catch (SQLException e) {
            return 0;
        }
    }

    private static String cleanSchema(String primary, String fallback) {
        String value = primary == null || primary.isBlank() ? fallback : primary;
        return value == null ? "" : value.trim();
    }

    private static String nullToEmpty(String value) {
        return value == null ? "" : value;
    }

    private record ForeignKeyImport(List<DbImportForeignKey> foreignKeys, List<String> warnings) { }

    private record PrimaryKeyColumn(String name, int keySequence) { }

    private record ForeignKeyGroupKey(String name, String sourceTable, String targetTable) implements Comparable<ForeignKeyGroupKey> {
        static ForeignKeyGroupKey of(DbImportForeignKey foreignKey) {
            return new ForeignKeyGroupKey(
                    nullToEmpty(foreignKey.name()),
                    nullToEmpty(foreignKey.sourceTable()),
                    nullToEmpty(foreignKey.targetTable())
            );
        }

        @Override public int compareTo(ForeignKeyGroupKey other) {
            int bySource = sourceTable.compareTo(other.sourceTable);
            if (bySource != 0) return bySource;
            int byName = name.compareTo(other.name);
            if (byName != 0) return byName;
            return targetTable.compareTo(other.targetTable);
        }
    }
}
