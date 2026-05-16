package see.schemeonyou.ui.presenter;

import see.schemeonyou.ui.DocumentState;

import java.util.Objects;
import java.util.Optional;

/** Pure document-lifecycle guard decisions for app-shell UI prompts. */
public final class DocumentLifecycleGuard {
    public enum UserChoice {
        SAVE,
        DISCARD,
        CANCEL
    }

    public enum Decision {
        PROCEED,
        CANCEL
    }

    public record UnsavedPrompt(String title, String header, String content) {
        public UnsavedPrompt {
            Objects.requireNonNull(title, "title");
            Objects.requireNonNull(header, "header");
            Objects.requireNonNull(content, "content");
        }
    }

    public Optional<UnsavedPrompt> prompt(DocumentState state, String projectName) {
        Objects.requireNonNull(state, "state");
        Objects.requireNonNull(projectName, "projectName");
        if (!state.isDirty()) return Optional.empty();
        String displayName = state.displayName(projectName);
        return Optional.of(new UnsavedPrompt(
                "Unsaved changes",
                "Save changes to " + displayName + "?",
                "Unsaved changes will be lost if you discard them."
        ));
    }

    public Decision decide(UserChoice choice, boolean saveSucceeded) {
        Objects.requireNonNull(choice, "choice");
        return switch (choice) {
            case SAVE -> saveSucceeded ? Decision.PROCEED : Decision.CANCEL;
            case DISCARD -> Decision.PROCEED;
            case CANCEL -> Decision.CANCEL;
        };
    }
}
