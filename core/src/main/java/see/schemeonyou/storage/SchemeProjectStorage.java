package see.schemeonyou.storage;

import see.di.See;
import see.schemeonyou.model.SchemeProject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

@See
public class SchemeProjectStorage {
    private final SchemeProjectJsonWriter writer = new SchemeProjectJsonWriter();
    private final SchemeProjectJsonReader reader = new SchemeProjectJsonReader();

    public void save(Path path, SchemeProject project) throws IOException {
        Files.createDirectories(path.toAbsolutePath().getParent());
        Files.writeString(path, writer.write(project), StandardCharsets.UTF_8);
    }

    public SchemeProject load(Path path) throws IOException {
        return reader.read(Files.readString(path, StandardCharsets.UTF_8));
    }
}
