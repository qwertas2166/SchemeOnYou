package see.schemeonyou.command;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class EditValueCommand<T, V> implements Command {
    private final T target;
    private final String id;
    private final String title;
    private final Function<T, V> getter;
    private final BiConsumer<T, V> setter;
    private final V newValue;
    private V oldValue;

    public EditValueCommand(T target, String id, String title, Function<T, V> getter, BiConsumer<T, V> setter, V newValue) {
        this.target = Objects.requireNonNull(target);
        this.id = Objects.requireNonNull(id);
        this.title = Objects.requireNonNull(title);
        this.getter = Objects.requireNonNull(getter);
        this.setter = Objects.requireNonNull(setter);
        this.newValue = Objects.requireNonNull(newValue);
    }

    public CommandMetadata metadata() {
        return CommandMetadata.of(id, title, "Inspector", "edit");
    }

    public void execute() {
        oldValue = getter.apply(target);
        setter.accept(target, newValue);
    }

    public void undo() {
        if (oldValue != null) setter.accept(target, oldValue);
    }
}
