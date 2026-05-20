summary - Сверить active issue path/status policy и убрать dangling references на отсутствующие issue
status - done
priority - mid
cost - S

goal - Сделать `machine/issue` надежным источником планирования для scheduler: активные задачи и ссылки на задачи не должны расходиться с фактическим деревом файлов.

description - В текущем дереве `machine/issue/work/` пустой, но несколько issue в sprint-директориях всё ещё имеют `status - work` / `status - in_progress` (`011`, `015`, `016`, `017`, `022`). Дополнительно `machine/current/progress.md` и sprint summaries ссылаются на canonical issue-файлы, которых сейчас нет в `machine/issue/backlog`, `machine/issue/work` или `machine/issue/sprint_*` (`024`, `027`, `030`, `032`, `033`). Ранее закрытые hygiene-задачи (`031`, `051`, `062`) исправляли прошлые drift-состояния, но не покрывают эту текущую комбинацию active-status вне work и dangling references. Нужно принять/зафиксировать единую политику: либо active issue lives in `work/`, либо sprint-директории могут содержать active files; затем привести статусы, ссылки и cost/progress summaries к этой политике без изменения product/code scope.

acceptance criteria -
- Сформулирована и записана короткая текущая policy для `machine/issue/work/` vs active `status - work/in_progress` в sprint-директориях.
- Все active-status файлы (`work`/`in_progress`) находятся там, где требует policy, или их status/path приведены в соответствие.
- Dangling references на отсутствующие canonical issue-файлы (`024`, `027`, `030`, `032`, `033`) либо восстановлены как history/backlog/work files, либо заменены на существующие canonical paths / explicit archived notes.
- `machine/issue/*/cost.md` и `machine/current/progress.md` больше не вводят scheduler в заблуждение о текущих active/backlog/work задачах.
- `machine/issue/consistency-checklist.md` обновлен, если policy изменилась.

dependencies/risks -
- Не менять продуктовые требования и код в рамках этой задачи.
- Риск: можно потерять историю выполненных задач при простом удалении ссылок; предпочтительно сохранять history note или восстановить minimal issue stubs.


progress - 2026-05-19 13:00 MSK scheduler-2:
- Chosen from backlog as the only available backlog task and it fits S (≤ M) with mid priority.
- Adopted current issue-tree policy: backlog has only `status - backlog`; `work/` is optional for active task staging and may be empty; `sprint_N/` is the canonical planning/history location and may contain `planned`, `work`, `in_progress`, or `done` issues when sprint summaries make the active state explicit.
- Restored minimal history stubs for missing canonical issues `024`, `027`, `030`, `032`, and `033` so old progress/sprint references keep history without implying current backlog/work items.
- Updated `consistency-checklist.md`, current progress, and affected sprint summaries with dated corrections.
- Product/code scope intentionally unchanged.

validation - 2026-05-19 13:00 MSK scheduler-2:
- Documentation-only change; production tests not run.
- `git diff --check -- machine/issue machine/current/progress.md` passed.
