summary - Вынести headless inspector/editing presenter slice

status - backlog
priority - mid
cost - M

parent - sprint_8/026-extract-application-presenters-for-headless-ui-tests.md

goal - Продолжить декомпозицию `SchemeOnYouApplication` по решению SEE: следующим seam выделить inspector/editing, чтобы модель полей инспектора и commit-решения редактирования проверялись без JavaFX Stage/DISPLAY.

description - В `SchemeOnYouApplication` inspector state и editing flow сейчас собираются напрямую в JavaFX UI: выбор типа selection, список editable/read-only полей, sequence/table/column редакторы, commit через undoable edit commands и cancel/focus behavior. Нужно вынести headless presenter/controller model для inspector/editor state, оставив app-shell только thin wiring layer для JavaFX controls.

acceptance criteria -
- Для текущей selection строится headless inspector model: empty diagram, table, column, FK preview, sequence participant, sequence message.
- Editable fields и read-only labels описываются в presenter model без JavaFX Node/Stage dependencies.
- Commit/edit decisions для table name, column name/type/flags, sequence participant name, sequence message label/activation делегируются из app-shell в presenter/controller слой.
- `SchemeOnYouApplication.updateInspector(...)` и editor commit handlers становятся thin wiring layer для JavaFX controls и undoable command execution.
- Поведение undo/redo, dirty state, validation surface, context line и existing keyboard shortcuts не регрессирует.
- Добавлены headless tests без JavaFX Stage/DISPLAY для inspector model и editing decisions минимум по DB table/column и sequence participant/message.
- Storage format и тяжелые dependencies не меняются.

notes -
- Решение SEE от 2026-05-16 15:03 MSK: следующий seam для `026` — inspector, editing.
- Scope держать M: не переписывать canvas rendering и file dialogs/errors в этом slice.

progress -
- 2026-05-16 scheduler-2: задача взята в работу; добавлен `InspectorPresenter` с headless model/edit decisions для diagram/table/column/FK preview/sequence participant/message, app-shell начал строить inspector через presenter; добавлены headless tests. Проверка Maven/JDK заблокирована: в окружении нет `mvn`, `java`, `javac`.
- 2026-05-16 22:01 MSK scheduler-3: normalized status spelling `in progress` → `in_progress` and updated parent link to canonical `sprint_8/026-...` path; no scope change.
