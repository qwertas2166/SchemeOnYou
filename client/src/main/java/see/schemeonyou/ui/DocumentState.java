package see.schemeonyou.ui;

import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;

/** Tracks the application document lifecycle independently from JavaFX widgets. */
public class DocumentState {
    private Path currentFile;
    private boolean dirty;

    public Optional<Path> currentFile() {
        return Optional.ofNullable(currentFile);
    }

    public boolean isDirty() {
        return dirty;
    }

    public void bindCleanFile(Path file) {
        currentFile = Objects.requireNonNull(file).toAbsolutePath().normalize();
        dirty = false;
    }

    public void markDirty() {
        dirty = true;
    }

    public void markClean() {
        dirty = false;
    }

    public void resetUntitledClean() {
        currentFile = null;
        dirty = false;
    }

    public String displayName(String projectName) {
        return currentFile == null ? projectName : currentFile.getFileName().toString();
    }

    public String dirtyMarker() {
        return dirty ? "* Unsaved" : "Saved";
    }
}
