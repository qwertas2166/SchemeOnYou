summary - Реализовать открытие проекта из файла

goal - Закрыть MVP-требование `Сохранение/открытие проекта` и сделать приложение пригодным для реальной работы между запусками.

description - Сейчас в UI есть сохранение проекта в JSON, но нет обратного пути загрузки: приложение стартует с seeded sample-проектом, а `Ctrl+O`/Open project не реализованы как рабочий сценарий. Нужно добавить JSON reader/parser для текущего Git-friendly формата, storage load service, кнопку/shortcut `Open`, обработку ошибок версии/формата и замену активного состояния приложения загруженным проектом. После загрузки нужно обновлять дерево диаграмм, активную диаграмму, выбранный элемент, canvas и inspector. Желательно добавить round-trip проверку: save -> load -> deterministic save без лишних изменений.

priority - high
cost - M
status - done
startedAt - 2026-05-15T10:55:00+03:00
note - Взят безопасный малый срез: core JSON reader/load service и round-trip coverage без UI/DISPLAY.
progress - Added SchemeProjectJsonReader, wired SchemeProjectStorage.load(Path), preserved project timestamps on load, connected Open button/Ctrl+O to FileChooser loading with state replacement, and added reader round-trip/version tests. Verification blocked locally: mvn/javac are not installed in this runtime.

progress - 2026-05-15T11:10:00+03:00 scheduler-2: backlog directory is empty, continued active M task 001. Hardened project JSON loading errors: invalid diagram type now becomes IOException with field context; invalid unicode escapes now produce IOException instead of NumberFormatException; added regression tests for both format failures.

progress - 2026-05-15T11:20:00+03:00 scheduler-2: backlog directory is empty, continued active M task 001. Hardened JSON parser to reject raw control characters inside strings during project open; added regression coverage for a project name containing an unescaped newline. Verification remains blocked locally: `mvn` is not installed in this runtime.
progress - 2026-05-15T11:25:00+03:00 scheduler-2: backlog directory is empty, continued active M task 001. Hardened JSON parser for project open: rejects non-finite numeric overflow (e.g. 1e9999) and leading-zero integer forms; added regression coverage for both malformed-number cases. Verification remains blocked locally: `mvn`, `java`, and `javac` are not installed in this runtime.
progress - 2026-05-15T11:30:00+03:00 scheduler-2: backlog directory is empty, continued active M task 001. Hardened project JSON parser to accept only JSON-defined whitespace outside strings (space/tab/LF/CR), rejecting form-feed and other Java whitespace; added regression coverage. Verification remains blocked locally: `mvn`, `java`, and `javac` are not installed in this runtime.
progress - 2026-05-15T11:35:00+03:00 scheduler-2: backlog directory is empty, continued active M task 001. Hardened project JSON parser to reject duplicate object keys instead of silently accepting shadowed values; added regression coverage. Verification remains blocked locally: `mvn`, `java`, and `javac` are not installed in this runtime.
progress - 2026-05-15T11:40:00+03:00 scheduler-2: backlog directory is empty, continued active M task 001. Hardened project storage version parsing to reject integer values outside Java int range instead of truncating/wrapping; added regression coverage for an out-of-range version. Verification remains blocked locally: `mvn`, `java`, and `javac` are not installed in this runtime.
progress - 2026-05-15T11:50:00+03:00 scheduler-2: backlog directory is empty, continued active M task 001. Hardened project JSON loading to reject duplicate IDs for diagrams and per-diagram/table elements (tables, foreign keys, sequence participants/messages, columns); added regression coverage for duplicate diagram/table/column IDs. Verification remains blocked locally: `mvn`, `java`, and `javac` are not installed in this runtime.
progress - 2026-05-15T13:00:00+03:00 scheduler-2: backlog directory is empty, continued highest-priority active M task 001. Hardened project open canvas parsing: rejects out-of-range zoom values instead of silently clamping, and rejects non-positive saved element bounds; added regression tests for both malformed canvas cases. Verification remains blocked locally: `mvn` is not installed in this runtime.
- 2026-05-17 01:50 MSK: взята в работу как следующая доступная задача из backlog.
validation -
- 2026-05-17 01:58 MSK: `JAVA_HOME=/home/openclaw/workspace/java/openjdk-25.0.2 PATH=/home/openclaw/workspace/java/apache-maven-3.9.11/bin:/home/openclaw/workspace/java/openjdk-25.0.2/bin:$PATH mvn -q test` — passed.
- 2026-05-17 01:58 MSK: `JAVA_HOME=/home/openclaw/workspace/java/openjdk-25.0.2 PATH=/home/openclaw/workspace/java/apache-maven-3.9.11/bin:/home/openclaw/workspace/java/openjdk-25.0.2/bin:$PATH mvn -q -DskipTests package && git diff --check` — passed.
