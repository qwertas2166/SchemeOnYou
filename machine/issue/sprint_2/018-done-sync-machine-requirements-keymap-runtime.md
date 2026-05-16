summary - Синхронизировать `machine/requirements.json` с runtime keymap 0/1/2/3
status - done
priority - high
cost - S

goal - Убрать оставшееся machine-расхождение после перехода runtime-навигации с F6 на прямые 0/1/2/3.

description - После `014-done-sync-runtime-keymap-with-machine-design-docs.md` актуальные docs/design/machine summary описывают крупные зоны как `0` top menu, `1` left menu, `2` canvas, `3` inspector. Но `machine/requirements.json` всё ещё содержит активные поля `major_area_next: F6`, `major_area_previous: Shift+F6` и текст design refinement, который может быть прочитан как текущая политика. Нужно обновить structured requirements так, чтобы F6 был только legacy/replaced context, а текущая runtime policy была однозначной.

acceptance criteria -
- `machine/requirements.json` описывает `0/1/2/3` как current major-area navigation.
- `F6` / `Shift+F6` в `machine/requirements.json` остаются только как legacy/replaced historical note или удалены из active shortcut map.
- `python3 -m json.tool machine/requirements.json` проходит.
- Поиск по `machine/*.json` не показывает F6 как активный runtime shortcut без пометки legacy/replaced.

dependencies/risks -
- Depends on completed `sprint_2/014-done-sync-runtime-keymap-with-machine-design-docs.md` and `sprint_1/012-done-ui-polish-accessibility-and-keymap-consistency.md`.
- Risk: не потерять исторический контекст DB design v07; отделить active requirements от historical note.

work log - 2026-05-15 14:00 MSK
- Selected by scheduler as highest-priority suitable backlog task: high priority, S size, safe structured-requirements-only change.
- Updated `machine/requirements.json` active shortcut model to current `0/1/2/3` major-area focus targets: top menu, left menu, canvas, inspector.
- Removed F6 / Shift+F6 from active database diagram shortcut refinement and retained them only under `legacy_major_area_cycle` as replaced v07 context.
- Updated design refinement text so current policy is unambiguous and F6 is historical only.
- Verification: `python3 -m json.tool machine/requirements.json`; grep across `machine/*.json` shows remaining F6 mentions are legacy/replaced context.
