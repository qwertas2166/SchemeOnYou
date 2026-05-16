summary - Добавить regression tests и release gates

goal - Снизить риск регрессий перед релизом и формализовать критерии готовности.

description - Сейчас основная проверка — compile/package/core-only и ручная проверка UI затруднена в headless-среде. Нужно добавить unit tests для command layer, undo/redo, deterministic serialization, validator, layout и SVG exporter. Отдельно нужны интеграционные проверки save/load round-trip и compound join-table undo. Для UI стоит выделить тестируемые presenter/controller части без DISPLAY, а визуальный JavaFX запуск проверять отдельной ручной release checklist. CI/release gate должен включать compile, tests, package, diff check и smoke core-only.

priority - mid
cost - L
status - backlog
startedAt - 2026-05-15T10:50:00+03:00
note - Взят безопасный малый срез: базовые core unit tests для command layer и deterministic JSON writer без UI/DISPLAY.
progress - Added JUnit 5 test dependency/surefire gate and first core tests for undo/redo and deterministic JSON writer. Verification blocked locally: `mvn` and `javac` are not installed in this runtime.

clarification - Решения SEE от 2026-05-15:
- Минимальный release gate: compile + tests + package. UI smoke в MVP-gate не нужен.
- Source of truth для gate: локальный Maven contract. GitHub Actions не обязателен.

clarification - Решения SEE от 2026-05-15 19:06 MSK:
- Отдельный manual release checklist-файл не нужен; достаточно локального Maven gate: compile + tests + package.

clarification - Решения SEE от 2026-05-15 20:28 MSK:
- Подтверждено повторно: достаточно compile + tests + package; отдельный manual release checklist не нужен.
