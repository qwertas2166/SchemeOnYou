package see.schemeonyou.ui.inspector;

import java.util.LinkedHashMap;
import java.util.Map;

public class InspectorPanel {
    private final Map<String, String> properties = new LinkedHashMap<>();
    public Map<String, String> properties() { return properties; }
    public void show(String key, String value) { properties.put(key, value); }
    public void clear() { properties.clear(); }
}
