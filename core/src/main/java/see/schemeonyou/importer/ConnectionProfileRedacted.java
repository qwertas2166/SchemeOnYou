package see.schemeonyou.importer;

public record ConnectionProfileRedacted(
        String id,
        String displayName,
        String host,
        int port,
        String database,
        String user,
        String defaultSchemaOrCatalog,
        String driverKind
) {
    public String password() {
        return "<redacted>";
    }
}
