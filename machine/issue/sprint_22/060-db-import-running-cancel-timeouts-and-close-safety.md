summary - Сделать DB import running-state безопасным: cancel/timeout/close guard
status - done
priority - high
cost - M

goal - Не допустить зависший или скрытый background import и неожиданную мутацию проекта после закрытия import dialog.

description - `DbImportDialog.importAndReplace(...)` запускает PostgreSQL metadata import в daemon `Task`/`Thread`, отключает основные кнопки и показывает progress, но не дает пользователю отменить уже запущенную операцию, не блокирует/не обрабатывает закрытие dialog во время running-state и не задает явный connect/login timeout на JDBC path. Если пользователь закроет modal после старта или соединение зависнет, background worker может продолжить жить вне видимого UI и при success вызвать `importReplace`/`onImported`, т.е. заменить диаграмму после того, как пользователь считает flow закрытым. `049-03` покрыл modal progress/errors и cancel before destructive replace; `057` покрыл undo-safe replace. Этот task закрывает отдельный release-safety gap вокруг running import lifecycle.

acceptance criteria -
- Во время running import dialog нельзя закрыть так, чтобы background worker молча продолжил и потом мутировал project без видимого active flow.
- Есть явная кнопка/действие Cancel для running import либо close переводится в confirmed cancel/ignore-result state.
- После cancel/close импорт не вызывает `importReplace`/`onImported`; late success/failure safely ignored or reported only while dialog is alive.
- JDBC connect/login path имеет bounded timeout для MVP, чтобы плохой host/network не оставлял UI в бесконечном `Connecting…`.
- Running-state controls/focus/status остаются понятными: import/profile buttons disabled as needed, cancel enabled, status says cancelled/timed out on expected paths.
- Password redaction сохраняется для timeout/cancel/error messages.
- Добавлены headless/UI-slice tests где возможно: late-success-after-cancel does not mutate project; timeout/cancel status path не регрессирует.

dependencies/risks -
- Depends on completed `sprint_16/049-03-import-modal-ui-progress-errors.md` and `sprint_19/057-make-db-import-replace-undo-safe-or-explicitly-committed.md`.
- Related to `sprint_2/015-non-modal-keyboard-flows-for-frequent-actions.md`, but not duplicate: this is specifically DB import background worker lifecycle, not generic modal replacement.
- Risk: JDBC metadata calls may not be fully interruptible; if hard cancellation is not reliable, implement safe ignore-result + bounded login timeout first and document the limitation.

progress - 2026-05-19 10:30 MSK scheduler-2: taken because `machine/issue/backlog/` is empty and this is the highest-priority suitable planned issue in `sprint_22` (high/M). Implemented running import lifecycle guard in `DbImportDialog`: explicit Cancel import action, close-button/window-close cancellation while running, stale/late task result ignored before project mutation, and cancelled status keeps password redaction path intact. Added bounded PostgreSQL JDBC properties (`connectTimeout`, `loginTimeout`, `socketTimeout`) and a headless regression test for timeout properties. Validation: `mvn -q -pl core -Dtest=PostgreSqlMetadataImporterTest test` OK; `mvn -q -pl client -am -DskipTests compile` OK; full `mvn -q test` OK.
