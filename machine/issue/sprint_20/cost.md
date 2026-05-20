# Sprint 20 plan / hourly backlog analysis

Актуализировано: 2026-05-18 20:06 Europe/Moscow.

## Найдено в `machine/issue/backlog`

На входе найдено 1 задача:

| Порядок | Issue | Status | Priority | Cost | Комментарий |
|---:|---|---|---:|---:|---|
| 1 | `061-sync-db-metadata-import-scope-with-requirements-docs.md` | backlog → planned | mid | S | docs/scope sync после DB import; подходит в следующий sprint как малый follow-up |

После анализа `machine/issue/backlog/` пуст.

## Актуальный sprint_20

| Порядок | Issue | Status | Priority | Cost | Комментарий |
|---:|---|---|---:|---:|---|
| 1 | `058-collision-safe-db-import-generated-ids.md` | done | high | S | закрыт риск duplicate IDs/invalid project JSON |
| 2 | `060-db-import-running-cancel-timeouts-and-close-safety.md` | planned | high | M | следующий release-safety slice: cancel/close/timeout для background import lifecycle |
| 3 | `059-db-import-composite-foreign-key-policy.md` | planned | mid | M | затем semantic correctness для composite FK import |

## Что изменено

- `061-sync-db-metadata-import-scope-with-requirements-docs.md` перенесена из `backlog/` в новый `sprint_21/`.
- Статус `061` изменен с `backlog` на `planned`; priority `mid`, cost `S` подтверждены.
- `sprint_20/cost.md` обновлен текущим hourly-анализом.
- `sprint_20` оставлен без изменения состава: `058` done, `060` planned, `059` planned.

## Перемещено / оценено / приоритизировано

- Перемещено:
  - `backlog/061-sync-db-metadata-import-scope-with-requirements-docs.md` → `sprint_21/061-sync-db-metadata-import-scope-with-requirements-docs.md`
- Оценено:
  - `061` — S: точечная синхронизация requirements/design/machine docs без production-code изменений.
  - `060` — M подтверждено: lifecycle safety для running import.
  - `059` — M подтверждено: policy + tests для composite FK metadata import.
  - `058` — S подтверждено, уже done.
- Приоритизация текущего sprint_20:
  1. `058` — high/S/done.
  2. `060` — high/M/planned: выше `059`, потому что закрывает риск hidden background mutation/hanging import.
  3. `059` — mid/M/planned: semantic correctness composite FK важна, но ниже lifecycle safety.

## L / вопросы

L-задач в доступном backlog, `sprint_20` и новом `sprint_21` не найдено; уточняющие вопросы не требуются.

## XL-декомпозиция

XL-задач в доступном backlog, `sprint_20` и новом `sprint_21` не найдено; декомпозиция не выполнялась.

## Блокеры / неясности

- Структура ясна: активный sprint — `sprint_20/`; доступный backlog — `machine/issue/backlog/`; новый следующий sprint — `sprint_21/`.
- `sprint_21` создан только для подходящего малого follow-up `061`, чтобы не перегружать `sprint_20`, где уже есть две planned M-задачи.
- Исполнительные Maven-gates не запускались: это backlog/planning-анализ без изменения production code.

## Correction / rollover — 2026-05-19 10:04 Europe/Moscow

- `060-db-import-running-cancel-timeouts-and-close-safety.md` перенесена в `sprint_22/` как highest-priority carry-over после закрытия `sprint_21`.
- `059-db-import-composite-foreign-key-policy.md` перенесена в `sprint_22/` как связанный DB import semantic follow-up.
- `sprint_20/` теперь содержит только закрытую `058`; актуальный план продолжения см. в `sprint_22/cost.md`.
