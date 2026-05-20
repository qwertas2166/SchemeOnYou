# Sprint 25 plan / issue-tree hygiene

Актуализировано: 2026-05-19 13:00 Europe/Moscow.

## Взято из `machine/issue/backlog`

| Issue | Status | Priority | Cost | Комментарий |
|---|---|---:|---:|---|
| `064-reconcile-active-issue-paths-and-dangling-references.md` | done | mid | S | Единственная backlog-задача; безопасная docs/planning hygiene, подходит под M/S/XS лимит. |

## Что сделано

- Зафиксирована policy: `backlog/` — только backlog; `work/` — optional active staging и может быть пуст; `sprint_N/` — canonical planning/history и может содержать active statuses при явном `cost.md`/progress контексте.
- Восстановлены minimal history stubs для отсутствующих canonical issues `024`, `027`, `030`, `032`, `033`.
- Обновлены `consistency-checklist.md`, `machine/current/progress.md` и affected sprint summaries с dated corrections.

## Блокеры

Нет. Product/code scope не менялся; тесты не запускались, потому что проход documentation-only.

## Scheduler-1 hourly analysis — 2026-05-19 13:05 Europe/Moscow

- `sprint_25` актуализирован как закрытый hygiene sprint: `064` остается `done` / mid / S.
- Доступный `machine/issue/backlog/` пуст, поэтому новый backlog item не выбран.
- Для продолжения взята ближайшая existing planned задача из open pool: `059-db-import-composite-foreign-key-policy.md`, перенесена из historical `sprint_24/` в новый active `sprint_26/`.
- Product/code scope не менялся; это planning-only rollover.
