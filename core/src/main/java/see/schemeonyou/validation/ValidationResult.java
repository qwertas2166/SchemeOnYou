package see.schemeonyou.validation;

import java.util.ArrayList;
import java.util.List;

public class ValidationResult {
    private final List<ValidationIssue> issues = new ArrayList<>();
    public List<ValidationIssue> issues() { return issues; }
    public boolean hasErrors() { return issues.stream().anyMatch(i -> i.severity() == ValidationIssue.Severity.ERROR); }
    public void warn(String code, String message, String elementId) { issues.add(new ValidationIssue(ValidationIssue.Severity.WARNING, code, message, elementId)); }
    public void error(String code, String message, String elementId) { issues.add(new ValidationIssue(ValidationIssue.Severity.ERROR, code, message, elementId)); }
}
