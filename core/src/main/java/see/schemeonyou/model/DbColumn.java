package see.schemeonyou.model;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.Objects;

@Getter
public class DbColumn {
    private final String id;
    @Setter
    @NonNull
    private String name;
    @Setter
    @NonNull
    private String type;
    @Setter
    private boolean primaryKey;
    @Setter
    private boolean unique;
    @Setter
    private boolean nullable = true;

    public DbColumn(String id, String name, String type) {
        this.id = Objects.requireNonNull(id);
        this.name = Objects.requireNonNull(name);
        this.type = Objects.requireNonNull(type);
    }

}
