package see.schemeonyou.service;

import see.di.See;
import see.schemeonyou.model.*;

@See
public class ProjectFactory {
    private final IdGenerator ids = new IdGenerator();

    public SchemeProject createProject(String name) {
        SchemeProject project = new SchemeProject(ids.next("project"), name);
        project.getDiagrams().add(new Diagram(ids.next("diagram"), "Database diagram", DiagramType.DATABASE));
        project.getDiagrams().add(new Diagram(ids.next("diagram"), "Sequence diagram", DiagramType.SEQUENCE));
        return project;
    }
    public DbTable table(String name) { return new DbTable(ids.next("table"), name); }
    public DbColumn column(String name, String type) { return new DbColumn(ids.next("column"), name, type); }
    public ForeignKey foreignKey(String sourceTableId, String sourceColumnId, String targetTableId, String targetColumnId) {
        return new ForeignKey(ids.next("fk"), sourceTableId, sourceColumnId, targetTableId, targetColumnId);
    }
}
