status - done
summary - Реализовать редактирование свойств выбранного элемента через inspector

goal - Закрыть MVP-требование keyboard-accessible properties editing и убрать ограничение текущего read-only inspector.

description - Сейчас inspector показывает сведения о выбранной таблице/колонках, но редактирование недоступно. Нужно сделать inspector интерактивным: переименование таблицы/колонки, изменение типа колонки, nullable/not-null, PK/unique flags. Все изменения должны выполняться через отдельные undoable commands с корректным undo/redo. Управление должно быть доступно с клавиатуры: фокус через `3`, перемещение по полям, Enter/commit, Esc/cancel. В canvas после изменения должны обновляться подписи, validation и SVG/export output.

priority - high
clarification - Решения SEE от 2026-05-15:
- Inspector редактирует выбранную таблицу и её существующие колонки прямо в правой панели.
- Commit изменений: Enter или потеря фокуса.
- Cancel: Esc.
- В первой версии редактируются все поля: table name, column name, column type, nullable, PK, unique.
- Добавление/удаление колонок из inspector пока не требуется; только редактирование существующих.
result - Inspector switched from read-only text to keyboard-editable fields for table name and existing column name/type/nullable/PK/unique; commits run undoable commands and redraw validation/canvas/export state.
check - SEE_REPO=http://89.223.121.28:8181 mvn -q -DskipTests compile could not run: mvn not installed; fallback javac compile for SchemeOnYou core/client sources passed; git diff --check on touched files passed.
