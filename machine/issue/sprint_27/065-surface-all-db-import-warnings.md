summary - Показать все предупреждения DB import, а не только первое
status - done
priority - high
cost - S

goal - Не терять диагностическую информацию при импорте схемы с несколькими degraded/ignored metadata cases.

description - После `059` `PostgreSqlMetadataImporter` возвращает `DbImportResult.importWarnings()`, но `DbImportDialog` в success status показывает только `result.importWarnings().getFirst()` и оставляет остальные warnings недоступными пользователю. Для одной composite FK этого достаточно, но при нескольких composite FK/дальнейших degraded cases пользователь не видит полный список пропущенных constraints и не может оценить полноту импортированной диаграммы. Нужен маленький UI-slice: показывать deterministic summary/count и дать пользователю увидеть полный список warnings без записи секретов в проект/export.

acceptance criteria -
- Если `DbImportResult.importWarnings()` содержит несколько сообщений, UI показывает count/summary и доступный пользователю полный список, а не только первый warning.
- Single-warning case остается простым и понятным.
- Success без warnings по-прежнему закрывает dialog автоматически как сейчас, если это выбранное MVP-поведение.
- Warning text проходит через существующий redaction path и не пишет connection password в status/log/project JSON/export.
- Добавлен headless/UI-slice тест на multiple warnings rendering/formatting без запуска реального PostgreSQL.

dependencies/risks -
- Depends on completed `sprint_26/059-db-import-composite-foreign-key-policy.md`.
- Related to DB import dialog lifecycle `sprint_22/060`, but not duplicate: lifecycle/cancel уже закрыт; здесь полнота warning surface.
- Risk: не разрастить в persistent import report; для MVP достаточно dialog/status details.

progress - 2026-05-19 15:30 MSK scheduler-2: реализован UI-slice форматирования import warnings в `DbImportDialog`: no-warning status не меняется и dialog по-прежнему auto-close, single warning остается inline, multiple warnings показываются с count и полным списком. Warning text проходит через redaction перед показом. Добавлен headless test `DbImportDialogTest` на no/single/multiple warning formatting без PostgreSQL. Проверка: `mvn -q -pl client -am -Dtest=DbImportDialogTest -Dsurefire.failIfNoSpecifiedTests=false test` OK.
