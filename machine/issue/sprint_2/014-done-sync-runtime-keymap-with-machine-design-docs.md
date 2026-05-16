summary - Синхронизировать runtime keymap 0/1/2/3 с machine/design документацией
status - done
priority - high
cost - S

goal - Убрать расхождение между реализованной релизной навигацией по крупным зонам и каноническими design/machine артефактами.

description - После завершения `012-done-ui-polish-accessibility-and-keymap-consistency.md` runtime-политика стала: `0` top menu, `1` left menu, `2` canvas, `3` inspector; `Tab` только внутри панелей/полей, `F6` убран из runtime hints/handler. При этом `machine/design.json`, `machine/tasks.json`, `machine/ai-brief.md`, `machine/README.md`, `design/final-summary.md`, `design/db-diagram-ui-ux-v07.md` и mockup/docs всё ещё местами называют `F6` / `Shift+F6` канонической навигацией. Нужно зафиксировать новую runtime-политику как текущую source of truth и явно пометить F6 как заменённое решение v07, чтобы следующие агенты не возвращали старую модель.

acceptance criteria -
- `machine/design.json`, `machine/tasks.json`, `machine/ai-brief.md`, `machine/README.md` описывают 0/1/2/3 как текущую major-area navigation.
- `design/final-summary.md` и актуальные docs/mockup-подсказки не противоречат runtime hints.
- История v07/F6 сохранена как previous design decision или refinement, а не молча потеряна.
- Поиск по актуальным runtime docs не выдаёт F6 как действующий shortcut без пометки legacy/replaced.
- Краткий consistency note добавлен в `machine/current/consistency-check.md`.

dependencies/risks -
- Depends on completed issue `sprint_1/012-done-ui-polish-accessibility-and-keymap-consistency.md`.
- Risk: при неаккуратной правке можно исказить историю design v01-v07; нужно разделить historical notes и current runtime policy.


work log - 2026-05-15 13:30 MSK
- Selected by scheduler as highest-priority S task in sprint_2; `issue/backlog/` was empty, so sprint_2 planned backlog was used.
- Updated `machine/design.json`, `machine/tasks.json`, `machine/ai-brief.md`, `machine/README.md` to make `0/1/2/3` the current major-area navigation.
- Updated `design/final-summary.md`, `design/db-diagram-ui-ux-v07.md`, docs/mockup copies, HTML hints, Figma plugin labels, and SVG fallback labels so F6 is not shown as active runtime navigation.
- Preserved F6/Shift+F6 as legacy/replaced v07 context.
- Added runtime keymap sync note to `machine/current/consistency-check.md`.
- Verification: `python3 -m json.tool machine/design.json machine/tasks.json`; grep confirms remaining F6 mentions are marked historical/legacy/replaced or unrelated color literals.
