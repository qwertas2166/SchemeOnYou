package see.schemeonyou.model;

import lombok.NonNull;

import java.util.List;
import java.util.Objects;

public record DbTableConstraint(@NonNull String id, @NonNull DbTableConstraintType type, @NonNull List<String> columnIds) {
    public DbTableConstraint {
        Objects.requireNonNull(id);
        Objects.requireNonNull(type);
        columnIds = List.copyOf(Objects.requireNonNull(columnIds));
        if (columnIds.isEmpty()) {
            throw new IllegalArgumentException("Table constraint must reference at least one column");
        }
    }
}
