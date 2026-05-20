package see.schemeonyou.importer;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Proxy;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

class PostgreSqlMetadataImporterTest {
    @Test
    void mapsTablesColumnsPrimaryKeysAndForeignKeysDeterministically() throws Exception {
        DatabaseMetaData metaData = fakeMetadata();

        DbImportResult result = new PostgreSqlMetadataImporter().importMetadata(metaData, "public");

        assertEquals("public", result.schemaOrCatalog());
        assertEquals(List.of("orders", "users"), result.tables().stream().map(DbImportTable::name).toList());

        DbImportTable users = result.tables().get(1);
        assertEquals(List.of("id", "email"), users.columns().stream().map(DbImportColumn::name).toList());
        assertTrue(users.columns().getFirst().primaryKey());
        assertFalse(users.columns().getFirst().nullable());
        assertEquals("int8", users.columns().getFirst().typeName());
        assertTrue(users.columns().get(1).nullable());

        assertEquals(1, result.foreignKeys().size());
        DbImportForeignKey fk = result.foreignKeys().getFirst();
        assertEquals("fk_orders_user", fk.name());
        assertEquals("orders", fk.sourceTable());
        assertEquals("user_id", fk.sourceColumn());
        assertEquals("users", fk.targetTable());
        assertEquals("id", fk.targetColumn());
    }

    @Test
    void detectsCompositeForeignKeysAndSkipsThemWithDeterministicWarning() throws Exception {
        DatabaseMetaData metaData = fakeCompositeFkMetadata();

        DbImportResult result = new PostgreSqlMetadataImporter().importMetadata(metaData, "public");

        assertTrue(result.foreignKeys().isEmpty(), "composite FK must not be imported as misleading single-column edges");
        assertEquals(List.of("Composite foreign key 'fk_order_line_order' from order_line(order_id, tenant_id) to orders(id, tenant_id) was skipped: MVP imports only single-column FK edges."),
                result.importWarnings());
    }

    @Test
    void keepsSingleColumnForeignKeysWhenCompositeForeignKeyIsSkipped() throws Exception {
        DatabaseMetaData metaData = fakeMixedForeignKeyMetadata();

        DbImportResult result = new PostgreSqlMetadataImporter().importMetadata(metaData, "public");

        assertEquals(List.of("fk_order_line_product"), result.foreignKeys().stream().map(DbImportForeignKey::name).toList());
        assertEquals(1, result.importWarnings().size());
    }


    @Test
    void mapsCompositePrimaryKeysInDeterministicKeySequenceOrder() throws Exception {
        DatabaseMetaData metaData = fakeCompositePrimaryKeyMetadata();

        DbImportResult result = new PostgreSqlMetadataImporter().importMetadata(metaData, "public");

        DbImportTable membership = result.tables().getFirst();
        assertEquals("membership", membership.name());
        assertEquals(List.of("user_id", "group_id"), membership.primaryKeyColumnNames());
        assertEquals(List.of("group_id", "user_id", "role"), membership.columns().stream().map(DbImportColumn::name).toList());
        assertTrue(membership.columns().stream()
                .filter(column -> column.name().equals("group_id") || column.name().equals("user_id"))
                .allMatch(DbImportColumn::primaryKey));
        assertFalse(membership.columns().stream().filter(column -> column.name().equals("role")).findFirst().orElseThrow().primaryKey());
    }

    @Test
    void profileConnectionUsesBoundedJdbcTimeouts() {
        ConnectionProfile profile = new ConnectionProfile(
                "profile-1", "Local", "localhost", 5432, "app", "see", "secret", "public", "postgresql");

        Properties properties = PostgreSqlMetadataImporter.jdbcProperties(profile);

        assertEquals("see", properties.getProperty("user"));
        assertEquals("secret", properties.getProperty("password"));
        assertEquals(PostgreSqlMetadataImporter.CONNECT_TIMEOUT_SECONDS, properties.getProperty("connectTimeout"));
        assertEquals(PostgreSqlMetadataImporter.LOGIN_TIMEOUT_SECONDS, properties.getProperty("loginTimeout"));
        assertEquals(PostgreSqlMetadataImporter.SOCKET_TIMEOUT_SECONDS, properties.getProperty("socketTimeout"));
    }

    @Test
    void emptySchemaIsUserFacingError() {
        DbMetadataImportException error = assertThrows(DbMetadataImportException.class,
                () -> new PostgreSqlMetadataImporter().importMetadata(fakeMetadata(), " "));

        assertTrue(error.getMessage().contains("schema/catalog is required"));
    }

    @Test
    void missingTablesIsRecoverableUserFacingError() {
        DatabaseMetaData metaData = (DatabaseMetaData) Proxy.newProxyInstance(getClass().getClassLoader(),
                new Class<?>[] {DatabaseMetaData.class},
                (proxy, method, args) -> {
                    if (method.getName().equals("getTables")) return rows(List.of());
                    return defaultValue(method.getReturnType());
                });

        DbMetadataImportException error = assertThrows(DbMetadataImportException.class,
                () -> new PostgreSqlMetadataImporter().importMetadata(metaData, "public"));

        assertTrue(error.getMessage().contains("No tables found"));
    }


    private static DatabaseMetaData fakeCompositePrimaryKeyMetadata() {
        return (DatabaseMetaData) Proxy.newProxyInstance(PostgreSqlMetadataImporterTest.class.getClassLoader(),
                new Class<?>[] {DatabaseMetaData.class},
                (proxy, method, args) -> switch (method.getName()) {
                    case "getTables" -> rows(List.of(Map.of("TABLE_NAME", "membership")));
                    case "getPrimaryKeys" -> rows(List.of(
                            Map.of("COLUMN_NAME", "group_id", "KEY_SEQ", 2),
                            Map.of("COLUMN_NAME", "user_id", "KEY_SEQ", 1)
                    ));
                    case "getColumns" -> rows(List.of(
                            row("membership", "role", "text", DatabaseMetaData.columnNullable, 3),
                            row("membership", "group_id", "int8", DatabaseMetaData.columnNoNulls, 1),
                            row("membership", "user_id", "int8", DatabaseMetaData.columnNoNulls, 2)
                    ));
                    case "getImportedKeys" -> rows(List.of());
                    default -> defaultValue(method.getReturnType());
                });
    }

    private static DatabaseMetaData fakeCompositeFkMetadata() {
        return fakeForeignKeyMetadata(false);
    }

    private static DatabaseMetaData fakeMixedForeignKeyMetadata() {
        return fakeForeignKeyMetadata(true);
    }

    private static DatabaseMetaData fakeForeignKeyMetadata(boolean includeSingleColumnFk) {
        return (DatabaseMetaData) Proxy.newProxyInstance(PostgreSqlMetadataImporterTest.class.getClassLoader(),
                new Class<?>[] {DatabaseMetaData.class},
                (proxy, method, args) -> switch (method.getName()) {
                    case "getTables" -> rows(List.of(
                            Map.of("TABLE_NAME", "order_line"),
                            Map.of("TABLE_NAME", "orders"),
                            Map.of("TABLE_NAME", "products")
                    ));
                    case "getPrimaryKeys" -> rows(List.of(Map.of("COLUMN_NAME", "id")));
                    case "getColumns" -> rows(List.of(
                            row("order_line", "id", "int8", DatabaseMetaData.columnNoNulls, 1),
                            row("order_line", "order_id", "int8", DatabaseMetaData.columnNoNulls, 2),
                            row("order_line", "tenant_id", "int8", DatabaseMetaData.columnNoNulls, 3),
                            row("order_line", "product_id", "int8", DatabaseMetaData.columnNoNulls, 4),
                            row("orders", "id", "int8", DatabaseMetaData.columnNoNulls, 1),
                            row("orders", "tenant_id", "int8", DatabaseMetaData.columnNoNulls, 2),
                            row("products", "id", "int8", DatabaseMetaData.columnNoNulls, 1)
                    ));
                    case "getImportedKeys" -> {
                        String table = (String) args[2];
                        if (!table.equals("order_line")) yield rows(List.of());
                        List<Map<String, Object>> keys = new ArrayList<>();
                        keys.add(Map.of(
                                "FK_NAME", "fk_order_line_order",
                                "FKTABLE_NAME", "order_line",
                                "FKCOLUMN_NAME", "tenant_id",
                                "PKTABLE_NAME", "orders",
                                "PKCOLUMN_NAME", "tenant_id",
                                "KEY_SEQ", 2));
                        keys.add(Map.of(
                                "FK_NAME", "fk_order_line_order",
                                "FKTABLE_NAME", "order_line",
                                "FKCOLUMN_NAME", "order_id",
                                "PKTABLE_NAME", "orders",
                                "PKCOLUMN_NAME", "id",
                                "KEY_SEQ", 1));
                        if (includeSingleColumnFk) {
                            keys.add(Map.of(
                                    "FK_NAME", "fk_order_line_product",
                                    "FKTABLE_NAME", "order_line",
                                    "FKCOLUMN_NAME", "product_id",
                                    "PKTABLE_NAME", "products",
                                    "PKCOLUMN_NAME", "id",
                                    "KEY_SEQ", 1));
                        }
                        yield rows(keys);
                    }
                    default -> defaultValue(method.getReturnType());
                });
    }

    private static DatabaseMetaData fakeMetadata() {
        return (DatabaseMetaData) Proxy.newProxyInstance(PostgreSqlMetadataImporterTest.class.getClassLoader(),
                new Class<?>[] {DatabaseMetaData.class},
                (proxy, method, args) -> switch (method.getName()) {
                    case "getTables" -> rows(List.of(
                            Map.of("TABLE_NAME", "users"),
                            Map.of("TABLE_NAME", "orders")
                    ));
                    case "getPrimaryKeys" -> {
                        String table = (String) args[2];
                        yield rows(switch (table) {
                            case "users" -> List.of(Map.of("COLUMN_NAME", "id"));
                            case "orders" -> List.of(Map.of("COLUMN_NAME", "id"));
                            default -> List.of();
                        });
                    }
                    case "getColumns" -> rows(List.of(
                            row("users", "email", "varchar", DatabaseMetaData.columnNullable, 2),
                            row("users", "id", "int8", DatabaseMetaData.columnNoNulls, 1),
                            row("orders", "user_id", "int8", DatabaseMetaData.columnNoNulls, 2),
                            row("orders", "id", "int8", DatabaseMetaData.columnNoNulls, 1)
                    ));
                    case "getImportedKeys" -> {
                        String table = (String) args[2];
                        yield rows(table.equals("orders")
                                ? List.of(Map.of(
                                        "FK_NAME", "fk_orders_user",
                                        "FKTABLE_NAME", "orders",
                                        "FKCOLUMN_NAME", "user_id",
                                        "PKTABLE_NAME", "users",
                                        "PKCOLUMN_NAME", "id",
                                        "KEY_SEQ", 1))
                                : List.of());
                    }
                    default -> defaultValue(method.getReturnType());
                });
    }

    private static Map<String, Object> row(String table, String column, String type, int nullable, int ordinal) {
        return Map.of(
                "TABLE_NAME", table,
                "COLUMN_NAME", column,
                "TYPE_NAME", type,
                "NULLABLE", nullable,
                "ORDINAL_POSITION", ordinal
        );
    }

    private static ResultSet rows(List<Map<String, Object>> rows) {
        List<Map<String, Object>> copy = new ArrayList<>(rows);
        return (ResultSet) Proxy.newProxyInstance(PostgreSqlMetadataImporterTest.class.getClassLoader(),
                new Class<?>[] {ResultSet.class},
                new java.lang.reflect.InvocationHandler() {
                    int index = -1;

                    @Override
                    public Object invoke(Object proxy, java.lang.reflect.Method method, Object[] args) throws Throwable {
                        return switch (method.getName()) {
                            case "next" -> ++index < copy.size();
                            case "getString" -> value(args[0]).toString();
                            case "getInt" -> ((Number) value(args[0])).intValue();
                            case "getShort" -> ((Number) value(args[0])).shortValue();
                            case "close" -> null;
                            case "wasNull" -> false;
                            default -> defaultValue(method.getReturnType());
                        };
                    }

                    private Object value(Object column) throws SQLException {
                        Object value = copy.get(index).get(String.valueOf(column));
                        if (value == null) throw new SQLException("Missing fake column " + column);
                        return value;
                    }
                });
    }

    private static Object defaultValue(Class<?> type) {
        if (type == boolean.class) return false;
        if (type == byte.class) return (byte) 0;
        if (type == short.class) return (short) 0;
        if (type == int.class) return 0;
        if (type == long.class) return 0L;
        if (type == float.class) return 0f;
        if (type == double.class) return 0d;
        return null;
    }
}
