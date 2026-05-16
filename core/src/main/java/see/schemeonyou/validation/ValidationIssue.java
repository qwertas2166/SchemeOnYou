package see.schemeonyou.validation;

public record ValidationIssue(Severity severity, String code, String message, String elementId) {
    public enum Severity { INFO, WARNING, ERROR }
}
