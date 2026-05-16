package see.schemeonyou.ui.command;

import see.schemeonyou.model.DiagramType;

import java.util.LinkedHashMap;
import java.util.Map;

public class SpaceCommandSheet {
    private final Map<String, String> entries = new LinkedHashMap<>();
    public SpaceCommandSheet() {
        entries.put("A T", "Add table");
        entries.put("A C", "Add column");
        entries.put("A P", "Add participant");
        entries.put("A M", "Add message");
        entries.put("A F", "Add foreign key");
        entries.put("A J", "Add join table");
        entries.put("P", "Pin relation target");
        entries.put("U", "Clear relation pin");
        entries.put("D", "Delete selected");
        entries.put("E", "Edit selected");
        entries.put("G T", "Go to table");
        entries.put("G S", "Search");
        entries.put("L D", "Layout diagram");
        entries.put("L S", "Layout selection");
    }
    public Map<String, String> entries() { return Map.copyOf(entries); }

    public Map<String, String> entries(DiagramType activeDiagramType) {
        LinkedHashMap<String, String> scoped = new LinkedHashMap<>();
        entries.forEach((key, value) -> {
            if (isAvailable(key, activeDiagramType)) scoped.put(key, value);
        });
        return Map.copyOf(scoped);
    }

    public boolean isAvailable(String chord, DiagramType activeDiagramType) {
        if (activeDiagramType == DiagramType.DATABASE) return !chord.equals("A P") && !chord.equals("A M");
        if (activeDiagramType == DiagramType.SEQUENCE) return !chord.equals("A T") && !chord.equals("A C") && !chord.equals("A F") && !chord.equals("A J") && !chord.equals("P") && !chord.equals("U") && !chord.equals("G T") && !chord.equals("L S");
        return true;
    }
}
