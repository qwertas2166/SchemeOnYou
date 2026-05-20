package see.schemeonyou.importer;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.PosixFileAttributeView;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Local-only persistence for PostgreSQL connection profiles.
 *
 * <p>The file is intentionally separate from project storage/export. The UI should pass a
 * launcher-adjacent path via {@link #launcherAdjacentProfileFile(Path)} for portable builds.</p>
 */
public class ConnectionProfileStore {
    public static final String FILE_NAME = "schemeonyou-connection-profiles.local";
    private static final String HEADER = "scheme-on-you-connection-profiles-v1";
    private static final Set<PosixFilePermission> OWNER_ONLY_PERMISSIONS = Set.of(
            PosixFilePermission.OWNER_READ,
            PosixFilePermission.OWNER_WRITE
    );
    private final Path file;

    public ConnectionProfileStore(Path file) {
        this.file = file;
    }

    public static Path launcherAdjacentProfileFile(Path launcherPath) {
        Path directory = Files.isDirectory(launcherPath) ? launcherPath : launcherPath.toAbsolutePath().getParent();
        if (directory == null) directory = Path.of(".").toAbsolutePath();
        return directory.resolve(FILE_NAME);
    }

    public Path file() {
        return file;
    }

    public List<ConnectionProfile> list() throws IOException {
        if (!Files.exists(file) || Files.size(file) == 0) return List.of();
        List<String> lines = Files.readAllLines(file, StandardCharsets.UTF_8);
        if (lines.isEmpty()) return List.of();
        if (!HEADER.equals(lines.getFirst())) throw corrupt("Unsupported connection profiles file format");
        List<ConnectionProfile> result = new ArrayList<>();
        for (int i = 1; i < lines.size(); i++) {
            String line = lines.get(i);
            if (line.isBlank()) continue;
            String[] parts = line.split("\\t", -1);
            if (parts.length != 9 || !"profile".equals(parts[0])) throw corrupt("Corrupt connection profile entry at line " + (i + 1));
            try {
                result.add(new ConnectionProfile(
                        decode(parts[1]), decode(parts[2]), decode(parts[3]), parsePort(decode(parts[4]), i + 1),
                        decode(parts[5]), decode(parts[6]), decode(parts[7]), decode(parts[8]), "postgresql"
                ));
            } catch (IllegalArgumentException e) {
                throw corrupt("Corrupt connection profile entry at line " + (i + 1), e);
            }
        }
        return List.copyOf(result);
    }

    public ConnectionProfile create(ConnectionProfileDraft draft) throws IOException {
        ConnectionProfileValidation.requireValid(draft);
        List<ConnectionProfile> profiles = new ArrayList<>(list());
        ConnectionProfile created = new ConnectionProfile(UUID.randomUUID().toString(),
                clean(draft.displayName()), clean(draft.host()), draft.port(), clean(draft.database()), clean(draft.user()),
                draft.password() == null ? "" : draft.password(), clean(draft.defaultSchemaOrCatalog()), clean(draft.driverKind()));
        profiles.add(created);
        write(profiles);
        return created;
    }

    public ConnectionProfile update(String id, ConnectionProfileDraft draft) throws IOException {
        ConnectionProfileValidation.requireValid(draft);
        List<ConnectionProfile> profiles = new ArrayList<>(list());
        for (int i = 0; i < profiles.size(); i++) {
            if (profiles.get(i).id().equals(id)) {
                ConnectionProfile updated = profiles.get(i).withDraft(new ConnectionProfileDraft(
                        clean(draft.displayName()), clean(draft.host()), draft.port(), clean(draft.database()), clean(draft.user()),
                        draft.password() == null ? "" : draft.password(), clean(draft.defaultSchemaOrCatalog()), clean(draft.driverKind())));
                profiles.set(i, updated);
                write(profiles);
                return updated;
            }
        }
        throw new ConnectionProfileStoreException("Connection profile not found: " + id);
    }

    public boolean delete(String id) throws IOException {
        List<ConnectionProfile> profiles = new ArrayList<>(list());
        boolean removed = profiles.removeIf(profile -> profile.id().equals(id));
        if (removed) write(profiles);
        return removed;
    }

    private void write(List<ConnectionProfile> profiles) throws IOException {
        if (file.getParent() != null) Files.createDirectories(file.getParent());
        StringBuilder out = new StringBuilder(HEADER).append('\n');
        for (ConnectionProfile profile : profiles) {
            out.append("profile");
            out.append('\t').append(encode(profile.id()));
            out.append('\t').append(encode(profile.displayName()));
            out.append('\t').append(encode(profile.host()));
            out.append('\t').append(encode(Integer.toString(profile.port())));
            out.append('\t').append(encode(profile.database()));
            out.append('\t').append(encode(profile.user()));
            out.append('\t').append(encode(profile.password()));
            out.append('\t').append(encode(profile.defaultSchemaOrCatalog()));
            out.append('\n');
        }
        Path temp = file.resolveSibling(file.getFileName() + ".tmp");
        writeOwnerOnlyTempFile(temp, out.toString());
        Files.move(temp, file, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
        restrictOwnerOnlyBestEffort(file);
    }

    private static void writeOwnerOnlyTempFile(Path temp, String content) throws IOException {
        Files.deleteIfExists(temp);
        try {
            if (supportsPosixPermissions(temp)) {
                Files.createFile(temp, PosixFilePermissions.asFileAttribute(OWNER_ONLY_PERMISSIONS));
            } else {
                Files.createFile(temp);
            }
        } catch (UnsupportedOperationException e) {
            Files.createFile(temp);
        }
        restrictOwnerOnlyBestEffort(temp);
        Files.writeString(temp, content, StandardCharsets.UTF_8);
    }

    static boolean supportsPosixPermissions(Path path) {
        Path probe = path.toAbsolutePath().getParent();
        if (probe == null) probe = Path.of(".").toAbsolutePath();
        return Files.getFileAttributeView(probe, PosixFileAttributeView.class) != null;
    }

    static void restrictOwnerOnlyBestEffort(Path path) {
        try {
            if (supportsPosixPermissions(path)) {
                Files.setPosixFilePermissions(path, OWNER_ONLY_PERMISSIONS);
            }
        } catch (IOException | UnsupportedOperationException ignored) {
            // Non-POSIX platforms are allowed in the MVP; this hardening is best-effort.
        }
    }

    private static String clean(String value) {
        return ConnectionProfileValidation.trim(value);
    }

    private static String encode(String value) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString((value == null ? "" : value).getBytes(StandardCharsets.UTF_8));
    }

    private static String decode(String encoded) {
        return new String(Base64.getUrlDecoder().decode(encoded), StandardCharsets.UTF_8);
    }

    private static int parsePort(String value, int line) throws ConnectionProfileStoreException {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw corrupt("Invalid port in connection profile entry at line " + line, e);
        }
    }

    private static ConnectionProfileStoreException corrupt(String message) {
        return new ConnectionProfileStoreException(message + "; recreate or edit the local profiles file");
    }

    private static ConnectionProfileStoreException corrupt(String message, Throwable cause) {
        return new ConnectionProfileStoreException(message + "; recreate or edit the local profiles file", cause);
    }
}
