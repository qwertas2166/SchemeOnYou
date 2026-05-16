package see.schemeonyou.command;

import see.schemeonyou.model.Diagram;
import see.schemeonyou.model.SchemeProject;

public class AddDiagramCommand implements Command {
    private final SchemeProject project;
    private final Diagram diagram;
    public AddDiagramCommand(SchemeProject project, Diagram diagram) { this.project = project; this.diagram = diagram; }
    public CommandMetadata metadata() { return CommandMetadata.of("diagram.add", "Add diagram", "Space A D", "diagram", "new"); }
    public void execute() { project.getDiagrams().add(diagram); project.touch(); }
    public void undo() { project.getDiagrams().remove(diagram); project.touch(); }
}
