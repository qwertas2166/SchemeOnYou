package see.schemeonyou.model;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Getter
public class DbTable {
    private final String id;
    @Setter
    @NonNull
    private String name;
    private final List<DbColumn> columns = new ArrayList<>();
    private final List<DbTableConstraint> constraints = new ArrayList<>();

    public DbTable(String id, String name) {
        this.id = Objects.requireNonNull(id);
        this.name = Objects.requireNonNull(name);
    }

    public Optional<DbColumn> findColumn(String columnId) {
        return columns.stream().filter(c -> c.getId().equals(columnId)).findFirst();
    }
}
