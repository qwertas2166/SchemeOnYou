package see.schemeonyou.ui.presenter;

import org.junit.jupiter.api.Test;
import see.schemeonyou.ui.DocumentState;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class DocumentLifecycleGuardTest {
    private final DocumentLifecycleGuard guard = new DocumentLifecycleGuard();

    @Test
    void cleanDocumentDoesNotNeedUnsavedPrompt() {
        DocumentState state = new DocumentState();

        assertTrue(guard.prompt(state, "Project").isEmpty());
    }

    @Test
    void dirtyDocumentBuildsPromptFromDisplayName() {
        DocumentState state = new DocumentState();
        state.bindCleanFile(Path.of("target/demo.soy.json"));
        state.markDirty();

        DocumentLifecycleGuard.UnsavedPrompt prompt = guard.prompt(state, "Project").orElseThrow();

        assertEquals("Unsaved changes", prompt.title());
        assertEquals("Save changes to demo.soy.json?", prompt.header());
        assertTrue(prompt.content().contains("discard"));
    }

    @Test
    void saveOnlyProceedsWhenSaveSucceeded() {
        assertEquals(DocumentLifecycleGuard.Decision.PROCEED,
                guard.decide(DocumentLifecycleGuard.UserChoice.SAVE, true));
        assertEquals(DocumentLifecycleGuard.Decision.CANCEL,
                guard.decide(DocumentLifecycleGuard.UserChoice.SAVE, false));
    }

    @Test
    void discardProceedsAndCancelStops() {
        assertEquals(DocumentLifecycleGuard.Decision.PROCEED,
                guard.decide(DocumentLifecycleGuard.UserChoice.DISCARD, false));
        assertEquals(DocumentLifecycleGuard.Decision.CANCEL,
                guard.decide(DocumentLifecycleGuard.UserChoice.CANCEL, true));
    }
}
