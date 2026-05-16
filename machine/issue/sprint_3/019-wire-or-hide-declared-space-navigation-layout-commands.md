summary - Согласовать Space-команды `G T`, `L S` и `E` с runtime: реализовать или убрать/переименовать в подсказках
status - planned
priority - mid
cost - S

goal - Убрать keyboard-first UX trap, когда F1/Space hints и `ShortcutMap` рекламируют команды, которые runtime не исполняет.

description - `ShortcutMap` и подсказки содержат `Space G T` / go table и `Space L S` / layout selection, но `executeSpaceCommand` сейчас поддерживает только `G S` и `L D`; `G T`/`L S` попадают в Unknown Space command. Дополнительно найден drift по `Space E`: runtime/ShortcutMap используют его как `Edit selected`, а `design/final-summary.md` и `machine/design.json` всё ещё описывают `Space E` как FK edge picker / `select_fk_edge`. Нужно либо реализовать минимальные flows, либо убрать/переименовать active hints и machine/design docs, оставив future notes. Предпочтение: для MVP реализовать `G T` как выбор таблицы через существующий find/picker flow, `L S` скрыть до selected-cluster layout, а `Space E` привести к одному source of truth: edit selected или explicit FK edge picker без конфликта с edit.

acceptance criteria -
- Нажатие `Space G T` не приводит к `Unknown Space command`; есть понятное действие или команда не показывается как активная.
- Нажатие `Space L S` не приводит к `Unknown Space command`; есть понятное действие или команда не показывается как активная.
- `Space E` имеет единую семантику во всех active sources: runtime handler, `ShortcutMap`, Space sheet, `machine/design.json`, `design/final-summary.md` и docs mirror не противоречат друг другу.
- Если FK edge picker остается требованием, он получает отдельный non-conflicting command/chord или `Space E` реализуется как picker, а edit/rename переносится на согласованный shortcut.
- `ShortcutMap`, F1 overlay и Space hint text согласованы с фактически исполняемыми командами.
- Если `L S` остается out-of-scope, это явно отражено в docs/hints как future/non-MVP, а не active shortcut.
- Добавлен минимальный regression check или manual checklist для Space command routing.

dependencies/risks -
- Related to `sprint_2/015-non-modal-keyboard-flows-for-frequent-actions.md`, но не дублирует его: здесь только declared-vs-executable/declared-vs-documented command consistency.
- Risk: `L S` может разрастись до layout selected cluster; при росте scope оставить `L S` скрытым и завести отдельную M-задачу.

analysis - 2026-05-15T18:32:00+03:00 scheduler-3: expanded scope after finding `Space E` drift: runtime uses Edit selected, while `design/final-summary.md` and `machine/design.json` still say FK edge picker/select_fk_edge. Kept cost S because fix is command/docs consistency, not a new UI flow unless explicit picker semantics are chosen.
