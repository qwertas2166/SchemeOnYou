package see.schemeonyou.command;

import see.schemeonyou.model.DbColumn;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class EditColumnCommand<T> implements Command {
    private final DbColumn column;
    private final String title;
    private final String id;
    private final Function<DbColumn, T> getter;
    private final BiConsumer<DbColumn, T> setter;
    private final T newValue;
    private T oldValue;

    public EditColumnCommand(DbColumn column, String id, String title, Function<DbColumn, T> getter, BiConsumer<DbColumn, T> setter, T newValue) {
        this.column = Objects.requireNonNull(column);
        this.id = Objects.requireNonNull(id);
        this.title = Objects.requireNonNull(title);
        this.getter = Objects.requireNonNull(getter);
        this.setter = Objects.requireNonNull(setter);
        this.newValue = Objects.requireNonNull(newValue);
    }

    public CommandMetadata metadata() {
        return CommandMetadata.of(id, title, "Inspector", "column", "edit");
    }

    public void execute() {
        oldValue = getter.apply(column);
        setter.accept(column, newValue);
    }

    public void undo() {
        if (oldValue != null) setter.accept(column, oldValue);
    }
}
