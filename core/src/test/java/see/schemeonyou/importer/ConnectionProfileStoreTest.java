package see.schemeonyou.importer;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.PosixFilePermission;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ConnectionProfileStoreTest {
    @TempDir
    Path tempDir;

    @Test
    void emptyOrMissingFileLoadsAsEmptyList() throws IOException {
        ConnectionProfileStore store = new ConnectionProfileStore(tempDir.resolve("profiles.local"));
        assertTrue(store.list().isEmpty());

        Files.writeString(store.file(), "");
        assertTrue(store.list().isEmpty());
    }

    @Test
    void createsUpdatesDeletesProfilesPersistently() throws IOException {
        Path file = tempDir.resolve("profiles.local");
        ConnectionProfileStore store = new ConnectionProfileStore(file);

        ConnectionProfile created = store.create(ConnectionProfileDraft.postgresql(
                "Local dev", "localhost", 5432, "scheme", "see", "secret", "public"));

        ConnectionProfileStore reloaded = new ConnectionProfileStore(file);
        assertEquals(1, reloaded.list().size());
        assertEquals("secret", reloaded.list().getFirst().password());
        assertEquals("public", reloaded.list().getFirst().defaultSchemaOrCatalog());

        ConnectionProfile updated = reloaded.update(created.id(), ConnectionProfileDraft.postgresql(
                "Staging", "db.example", 5433, "scheme_stg", "app", "newSecret", "app_schema"));
        assertEquals("Staging", updated.displayName());
        assertEquals("newSecret", new ConnectionProfileStore(file).list().getFirst().password());

        assertTrue(new ConnectionProfileStore(file).delete(created.id()));
        assertTrue(new ConnectionProfileStore(file).list().isEmpty());
        assertFalse(new ConnectionProfileStore(file).delete(created.id()));
    }

    @Test
    void redactsPasswordFromDisplayHelpersAndToString() throws IOException {
        ConnectionProfile profile = new ConnectionProfileStore(tempDir.resolve("profiles.local"))
                .create(ConnectionProfileDraft.postgresql("Prod", "db", 5432, "prod", "admin", "top-secret", ""));

        assertEquals("<redacted>", profile.redacted().password());
        assertFalse(profile.toString().contains("top-secret"));
        assertTrue(profile.toString().contains("Prod"));
    }

    @Test
    void validatesRequiredFieldsPortAndDriver() {
        var errors = ConnectionProfileValidation.validate(new ConnectionProfileDraft(" ", "", 0, "", "", "pw", "", "mysql"));

        assertTrue(errors.contains("Display name is required"));
        assertTrue(errors.contains("Host is required"));
        assertTrue(errors.contains("Database is required"));
        assertTrue(errors.contains("User is required"));
        assertTrue(errors.contains("Port must be between 1 and 65535"));
        assertTrue(errors.contains("Only postgresql driver is supported"));
    }

    @Test
    void corruptFileReturnsRecoverableErrorWithoutPasswordLeak() throws IOException {
        Path file = tempDir.resolve("profiles.local");
        Files.writeString(file, "not-the-format\nsecret-password\n");

        ConnectionProfileStoreException error = assertThrows(ConnectionProfileStoreException.class,
                () -> new ConnectionProfileStore(file).list());
        assertTrue(error.getMessage().contains("Unsupported connection profiles file format"));
        assertFalse(error.getMessage().contains("secret-password"));
    }

    @Test
    void restrictsProfileFileToOwnerOnlyWhenPosixPermissionsAreAvailable() throws IOException {
        Path file = tempDir.resolve("profiles.local");
        new ConnectionProfileStore(file).create(ConnectionProfileDraft.postgresql(
                "Local", "localhost", 5432, "scheme", "see", "secret", "public"));

        if (ConnectionProfileStore.supportsPosixPermissions(file)) {
            assertEquals(Set.of(PosixFilePermission.OWNER_READ, PosixFilePermission.OWNER_WRITE),
                    Files.getPosixFilePermissions(file));
        } else {
            assertTrue(Files.exists(file));
        }
    }

    @Test
    void ownerOnlyRestrictionIsBestEffort() throws IOException {
        Path file = tempDir.resolve("profiles.local");
        Files.writeString(file, "temporary");

        assertDoesNotThrow(() -> ConnectionProfileStore.restrictOwnerOnlyBestEffort(file));
        assertTrue(Files.exists(file));
    }

    @Test
    void resolvesLauncherAdjacentPath() {
        Path launcher = tempDir.resolve("app").resolve("SchemeOnYou");
        assertEquals(launcher.getParent().resolve(ConnectionProfileStore.FILE_NAME),
                ConnectionProfileStore.launcherAdjacentProfileFile(launcher));
    }
}
