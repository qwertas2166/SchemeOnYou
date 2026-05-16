summary - Нормализовать статусы issue и sprint/backlog индексы после переносов задач

status - done
priority - mid
cost - S

goal - Убрать риск ошибочного планирования из-за рассинхрона между расположением issue-файлов, полем `status` и sprint `cost.md` сводками.

description - В текущем дереве задач есть признаки рассинхрона: например `machine/issue/work/030-sequence-delete-undo-safe.md` лежит в `work/`, но внутри имеет `status - backlog`; `sprint_4/cost.md` всё ещё описывает `027` как planned, хотя файл уже находится в `work/`; отдельные sprint/work summaries могут отставать от фактических переносов. Это не блокирует код напрямую, но ухудшает scheduler-планирование, приоритизацию и отчеты по release readiness.

acceptance criteria -
- Все файлы в `machine/issue/work/` имеют status `work` или `in_progress`, без `backlog`/`planned` внутри.
- Все файлы в `machine/issue/backlog/` имеют status `backlog`.
- Sprint `cost.md` файлы обновлены так, чтобы ссылки на moved/in_progress задачи совпадали с фактическим расположением файлов.
- Для задач, уже перенесенных из backlog в work, добавлена короткая строка provenance/progress, если её нет.
- Добавлена/обновлена простая проверка или manual checklist для будущих scheduler-проходов: path/status/cost summary не должны противоречить друг другу.
- Не меняется scope/product behavior и не выполняется переоценка задач без отдельного основания.

dependencies/risks -
- Related to scheduler backlog refinement reports and `machine/work/cost_rule.md`.
- Risk: можно случайно переписать смысл задач; ограничить работу только статусами, расположением, индексами и короткими progress notes.
progress -
- 2026-05-15 18:08 Europe/Moscow scheduler-1: moved from backlog to sprint_5 as small planning-hygiene task; scope remains limited to status/path/index normalization.
- 2026-05-15 19:04 Europe/Moscow scheduler-1: normalized `work/030-sequence-delete-undo-safe.md` status from `backlog` to `work`; refreshed sprint_4/sprint_5 summaries so path/status/index no longer contradict the active work tree.

manual checklist - future scheduler passes should verify: `backlog/` files keep `status - backlog`; `work/` files keep `status - work` or `status - in_progress` unless explicitly done; sprint `cost.md` summaries must describe the actual file location/status before moving more backlog tasks.
