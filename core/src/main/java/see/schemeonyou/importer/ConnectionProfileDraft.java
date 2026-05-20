package see.schemeonyou.importer;

/** UI-facing editable values for a local DB connection profile. */
public record ConnectionProfileDraft(
        String displayName,
        String host,
        int port,
        String database,
        String user,
        String password,
        String defaultSchemaOrCatalog,
        String driverKind
) {
    public static ConnectionProfileDraft postgresql(
            String displayName,
            String host,
            int port,
            String database,
            String user,
            String password,
            String defaultSchemaOrCatalog
    ) {
        return new ConnectionProfileDraft(displayName, host, port, database, user, password, defaultSchemaOrCatalog, "postgresql");
    }
}
