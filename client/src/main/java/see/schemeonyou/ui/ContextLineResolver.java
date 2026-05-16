package see.schemeonyou.ui;

import see.schemeonyou.model.FkPreview;
import see.schemeonyou.model.RelationPin;
import see.schemeonyou.validation.ValidationIssue;
import see.schemeonyou.validation.ValidationResult;

import java.util.Optional;

public class ContextLineResolver {
    public String resolve(Optional<FkPreview> fkPreview, RelationPin pin, String fallback) {
        return resolve(Optional.empty(), fkPreview, pin, Optional.empty(), Optional.empty(), Optional.empty(), fallback);
    }

    public String resolve(Optional<String> blockingConfirmation,
                          Optional<FkPreview> activePreview,
                          RelationPin pin,
                          Optional<String> recentResult,
                          Optional<String> selectionTip,
                          Optional<ValidationResult> validation,
                          String fallback) {
        if (blockingConfirmation.isPresent() && !blockingConfirmation.get().isBlank()) {
            return blockingConfirmation.get();
        }
        if (activePreview.isPresent()) {
            FkPreview preview = activePreview.get();
            return "FK preview: Source " + preview.getSourceTableId() + " → Target " + preview.getTargetTableId() + " • X Swap • Enter Create • Esc Cancel";
        }
        if (pin != null && pin.isPinned()) {
            return "Relation pin: " + pin.targetTableId().orElse("") + pin.targetColumnId().map(column -> "." + column).orElse("") + " • Space A F create FK • Space U clear";
        }
        if (recentResult.isPresent() && !recentResult.get().isBlank()) {
            return recentResult.get();
        }
        if (validation.isPresent() && !validation.get().issues().isEmpty()) {
            ValidationResult result = validation.get();
            long errors = result.issues().stream().filter(issue -> issue.severity() == ValidationIssue.Severity.ERROR).count();
            long warnings = result.issues().stream().filter(issue -> issue.severity() == ValidationIssue.Severity.WARNING).count();
            return errors > 0
                    ? "Validation: " + errors + " error(s), " + warnings + " warning(s) — see inspector"
                    : "Validation: " + warnings + " warning(s) — see inspector";
        }
        if (selectionTip.isPresent() && !selectionTip.get().isBlank()) {
            return selectionTip.get();
        }
        return fallback == null || fallback.isBlank() ? "Ready" : fallback;
    }
}
