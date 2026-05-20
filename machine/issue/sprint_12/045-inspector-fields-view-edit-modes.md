summary - Добавить режимы просмотра и редактирования для полей inspector

status - done
priority - high
cost - M

goal - Сделать keyboard-first редактирование inspector предсказуемым: поля inspector имеют режим просмотра и режим редактирования, вход/сохранение выполняются через `Enter`, отмена — через `Esc`.

description - Поля в inspector не должны сразу редактироваться при фокусе/выборе. По умолчанию поле находится в режиме просмотра. При нажатии `Enter` выбранное поле переходит в режим редактирования. Повторное нажатие `Enter` сохраняет изменение и выходит в режим просмотра. Нажатие `Esc` отменяет изменение и выходит в режим просмотра без сохранения.

acceptance criteria -
- Каждое editable-поле inspector поддерживает два состояния: view mode и edit mode.
- При выборе/focus поля inspector оно находится в view mode и не изменяет значение от обычной навигации.
- `Enter` в view mode переводит выбранное поле в edit mode.
- `Enter` в edit mode сохраняет текущее значение, применяет undoable command/dirty state и возвращает поле в view mode.
- `Esc` в edit mode отменяет несохраненное изменение, восстанавливает исходное значение и возвращает поле в view mode.
- `Esc` в view mode не должен случайно менять данные; поведение cancel/back должно быть безопасным и консистентным с остальным UI.
- В edit mode ввод текста/значений не перехватывается глобальными shortcuts, кроме явно разрешенных `Enter`/`Esc` для commit/cancel.
- После commit/cancel фокус остается на том же inspector-поле или на предсказуемом view-state элементе.
- Поведение работает для table name, column name/type/flags, sequence participant name, sequence message label/activation и других editable inspector fields.
- Добавлены headless/presenter tests или UI smoke-check для enter-to-edit, enter-to-save и esc-to-cancel.

notes -
- Требование SEE от 2026-05-17: поля в inspector должны иметь два режима: просмотр и редактирование; вход в редактирование — `Enter`; выход с сохранением — `Enter`; выход без сохранения — `Esc`.
- Связано с inspector/editing presenter slice `026-04` и keyboard-first focus model.
- Оценка дана в формате S/M/L/XL: `M`.

progress - 2026-05-17 02:00 MSK scheduler-2:
- Взята в работу как самая приоритетная подходящая задача: priority high, cost M.
- Начат безопасный UI-slice: inspector text fields теперь открываются в edit mode по Enter, сохраняются по Enter и отменяются по Esc; checkbox fields защищены от случайного toggle в view mode и сохраняются по Enter из edit mode.
- Проверка заблокирована локально: `mvn` не установлен в runtime, поэтому `mvn test` / package не запускались.
validation -
- 2026-05-17 02:12 MSK: `JAVA_HOME=/home/openclaw/workspace/java/openjdk-25.0.2 PATH=/home/openclaw/workspace/java/apache-maven-3.9.11/bin:/home/openclaw/workspace/java/openjdk-25.0.2/bin:$PATH mvn -q test` — passed.
- 2026-05-17 02:12 MSK: `JAVA_HOME=/home/openclaw/workspace/java/openjdk-25.0.2 PATH=/home/openclaw/workspace/java/apache-maven-3.9.11/bin:/home/openclaw/workspace/java/openjdk-25.0.2/bin:$PATH mvn -q -DskipTests package && git diff --check` — passed.

progress - 2026-05-17 13:05 MSK scheduler-1: moved from `work/` to `sprint_12/` as done artifact during hourly backlog normalization; `work/` kept for active tasks only.
