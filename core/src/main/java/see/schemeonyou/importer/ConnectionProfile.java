package see.schemeonyou.importer;

import java.util.Objects;

/** Local-only saved connection profile. Never put instances into project JSON/export. */
public record ConnectionProfile(
        String id,
        String displayName,
        String host,
        int port,
        String database,
        String user,
        String password,
        String defaultSchemaOrCatalog,
        String driverKind
) {
    public ConnectionProfile {
        Objects.requireNonNull(id, "id");
        Objects.requireNonNull(displayName, "displayName");
        Objects.requireNonNull(host, "host");
        Objects.requireNonNull(database, "database");
        Objects.requireNonNull(user, "user");
        Objects.requireNonNull(password, "password");
        defaultSchemaOrCatalog = defaultSchemaOrCatalog == null ? "" : defaultSchemaOrCatalog;
        Objects.requireNonNull(driverKind, "driverKind");
    }

    public ConnectionProfileRedacted redacted() {
        return new ConnectionProfileRedacted(id, displayName, host, port, database, user, defaultSchemaOrCatalog, driverKind);
    }

    public ConnectionProfile withDraft(ConnectionProfileDraft draft) {
        return new ConnectionProfile(
                id,
                draft.displayName(),
                draft.host(),
                draft.port(),
                draft.database(),
                draft.user(),
                draft.password(),
                draft.defaultSchemaOrCatalog(),
                draft.driverKind()
        );
    }

    @Override
    public String toString() {
        return redacted().toString();
    }
}
