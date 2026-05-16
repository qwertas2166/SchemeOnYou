package see.schemeonyou.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class DbTable {
    private final String id;
    private String name;
    private final List<DbColumn> columns = new ArrayList<>();

    public DbTable(String id, String name) {
        this.id = Objects.requireNonNull(id);
        this.name = Objects.requireNonNull(name);
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = Objects.requireNonNull(name); }
    public List<DbColumn> getColumns() { return columns; }
    public Optional<DbColumn> findColumn(String columnId) {
        return columns.stream().filter(c -> c.getId().equals(columnId)).findFirst();
    }
}
