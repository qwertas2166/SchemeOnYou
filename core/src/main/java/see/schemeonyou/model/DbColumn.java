package see.schemeonyou.model;

import java.util.Objects;

public class DbColumn {
    private final String id;
    private String name;
    private String type;
    private boolean primaryKey;
    private boolean unique;
    private boolean nullable = true;

    public DbColumn(String id, String name, String type) {
        this.id = Objects.requireNonNull(id);
        this.name = Objects.requireNonNull(name);
        this.type = Objects.requireNonNull(type);
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = Objects.requireNonNull(name); }
    public String getType() { return type; }
    public void setType(String type) { this.type = Objects.requireNonNull(type); }
    public boolean isPrimaryKey() { return primaryKey; }
    public void setPrimaryKey(boolean primaryKey) { this.primaryKey = primaryKey; }
    public boolean isUnique() { return unique; }
    public void setUnique(boolean unique) { this.unique = unique; }
    public boolean isNullable() { return nullable; }
    public void setNullable(boolean nullable) { this.nullable = nullable; }
}
