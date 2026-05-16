package see.schemeonyou.ui;

import org.junit.jupiter.api.Test;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class DocumentStateTest {
    @Test
    void newDocumentStartsCleanWithoutCurrentFile() {
        DocumentState state = new DocumentState();

        assertTrue(state.currentFile().isEmpty());
        assertFalse(state.isDirty());
        assertEquals("Saved", state.dirtyMarker());
    }

    @Test
    void mutateSaveAndUndoRedoTransitionsAreRepresentable() {
        DocumentState state = new DocumentState();

        state.markDirty();
        assertTrue(state.isDirty());
        assertEquals("* Unsaved", state.dirtyMarker());

        state.bindCleanFile(Path.of("target/project.json"));
        assertTrue(state.currentFile().isPresent());
        assertFalse(state.isDirty());

        state.markDirty();
        assertTrue(state.isDirty());
    }

    @Test
    void resetUntitledCleanKeepsCancelledOpenOrNewFromChangingState() {
        DocumentState state = new DocumentState();
        state.bindCleanFile(Path.of("target/project.json"));
        state.markDirty();

        state.resetUntitledClean();

        assertTrue(state.currentFile().isEmpty());
        assertFalse(state.isDirty());
    }
}
