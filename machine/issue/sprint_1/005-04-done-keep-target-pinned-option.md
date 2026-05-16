summary - Добавить опцию Keep target pinned after create

goal - Поддержать быстрый повторный FK workflow без скрытых transient repeat shortcuts.

description - В FK preview добавить явную опцию `[ ] Keep target pinned after create`, выключенную по умолчанию. После успешного создания FK pin очищается, если опция выключена, или остается видимым, если включена. Нужно обновить context line/result feedback: сначала показать результат создания, затем восстановить pin state при активной опции. Поведение Esc не должно очищать pin; explicit unpin остается `Space U`.

priority - mid
status - done
result - В FK preview добавлена опция Keep target pinned after create, выключенная по умолчанию. При включении target column остается pinned после создания FK; при выключении pin очищается.
check - SEE_REPO=http://89.223.121.28:8181 mvn -q -DskipTests compile
