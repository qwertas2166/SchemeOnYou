summary - Разрезать монолитный `SchemeOnYouApplication` на тестируемые presenter/controller slices

status - done
priority - mid
cost - L

goal - Снизить release-риск от 1900+ строк UI-монолита и сделать частые keyboard-first flows проверяемыми без JavaFX `DISPLAY`.

description - `client/src/main/java/see/schemeonyou/SchemeOnYouApplication.java` сейчас содержит регистрацию команд, document lifecycle, palette/find/delete/join dialogs, canvas rendering, hit-testing, sequence rendering, inspector state, drag/drop, storage/export и shortcut handling в одном классе (~1918 строк). Уже есть отдельные `DocumentState`, `DeleteSelectedController`, `ContextLineResolver`, но новые задачи (`015`, `020`, `022`, `024`) продолжают добавлять UI-flow логику в app-shell. Это повышает риск регрессий и мешает headless presenter-level тестам, которые нужны для release gates. Нужно выделить безопасные slices без переписывания всего приложения сразу.

acceptance criteria -
- Выделены 2-3 presenter/controller компонента с чистыми input/output state models без необходимости запускать JavaFX Stage, например: command routing/palette actions, canvas selection/hit-test state, document lifecycle guard, sequence add/edit flow.
- `SchemeOnYouApplication` становится thin wiring layer для JavaFX controls и делегирует бизнес-flow в presenters/controllers.
- Покрыты headless tests для выделенных slices: command availability by diagram context, selection transitions, dirty/new/open guard decisions или sequence add/edit flow.
- Поведение существующих shortcuts, command palette actions, undo/redo, dirty state и context line не регрессирует.
- Рефактор не меняет storage format и не добавляет тяжелых dependencies.
- Если scope растет, задача декомпозируется на M-подзадачи до реализации.

dependencies/risks -
- Related to `sprint_2/015-non-modal-keyboard-flows-for-frequent-actions.md`, `work/024-dirty-state-current-file-and-unsaved-change-guard.md`, `sprint_3/022-sequence-keyboard-creation-and-inspector-editing.md`.
- Risk: L-задача может стать слишком широкой; перед стартом нужно выбрать первый безопасный seam и не делать big-bang rewrite.
- Risk: JavaFX controls нельзя тянуть в core; presenters лучше держать в client module, но без Stage/DISPLAY.

decomposition -
- `work/026-01-command-router-presenter-slice.md` — M, first safe seam: headless command metadata/search/dispatch router.
- `work/026-02-document-lifecycle-guard-presenter-slice.md` — S, second safe seam: headless dirty/unsaved-change guard prompt and decision model.
- `sprint_6/026-04-inspector-editing-presenter-slice.md` — M, next confirmed seam: headless inspector model and editing decisions.

progress -
- 2026-05-15 scheduler-2: parent is L and too large for the M-or-less cron slice, so it was decomposed; first M child was started instead of big-bang refactor.
- 2026-05-15 scheduler-2: `work/026-01-command-router-presenter-slice.md` completed and validated; first safe command-routing seam extracted with headless tests.
- 2026-05-15 scheduler-2: `work/026-02-document-lifecycle-guard-presenter-slice.md` completed and validated; document lifecycle guard prompt/decision logic extracted from app-shell with headless tests.
- 2026-05-15 scheduler-2: после закрытия command routing/document lifecycle взят следующий подтвержденный seam — sequence editing (`sprint_3/022`, M); реализован первый add/edit/navigation slice с headless tests.
- 2026-05-15 scheduler-2: после решения SEE 20:28 MSK закрыт следующий presenter seam — canvas selection / hit-testing; добавлен `work/026-03-canvas-hit-test-presenter-slice.md` с headless tests.
- 2026-05-16 scheduler-2: backlog содержит только parent `026` cost L; M/S/XS-задач в `backlog/` нет, поэтому новая M-or-less задача не взята. Подтверждено, что уже выделенные child slices `026-01`, `026-02`, `026-03` закрыты.

questions -
- Какие seams приоритетнее вынести первыми: command palette/non-modal flows, canvas selection/hit-testing, document lifecycle, sequence editing?
- Нужна ли цель по размеру `SchemeOnYouApplication` для первого релиза (например, <1200 строк), или достаточно новых headless tests вокруг самых рискованных flows?

clarification - Решения SEE от 2026-05-15:
- Приоритет выноса seams: 1) command palette / command routing; 2) document lifecycle; 3) sequence editing; 4) canvas selection / hit-testing.
- Цель по размеру `SchemeOnYouApplication` не ставить; важнее headless tests и снижение риска регрессий.
- Presenters делать по рекомендации: оставить в `client` module, вынести в отдельный package (`see.schemeonyou.ui.presenter` или близкий), без зависимости от JavaFX Stage/DISPLAY. Новый Maven module пока не нужен.

clarification - Решения SEE от 2026-05-15 19:06 MSK:
- Следующий seam после command routing/document lifecycle: sequence editing.

clarification - Решения SEE от 2026-05-15 20:28 MSK:
- Следующий presenter seam: canvas selection / hit-testing.
clarification - Решение SEE от 2026-05-16 15:03 MSK:
- Следующий presenter seam для `026`: inspector, editing.
- Не расширять этот slice на file dialogs/errors или canvas rendering.

- 2026-05-16 scheduler-2: взят M-slice `work/026-04-inspector-editing-presenter-slice.md`; реализован первый inspector/editing presenter pass с headless tests, нужна валидация в окружении с JDK/Maven.
clarification - Решение SEE от 2026-05-16 15:44 MSK:
- Parent `026` закрываем; дополнительных presenter seams после inspector/editing не требуется как условие parent.
