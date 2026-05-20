# Sprint 24 plan / hourly backlog analysis

Актуализировано: 2026-05-19 12:04 Europe/Moscow.

## Найдено в `machine/issue/backlog`

`machine/issue/backlog/` пуст: файлов задач не найдено.

`machine/issue/work/` тоже пуст. По `consistency-checklist.md`, при пустом backlog scheduler может выбирать из planned sprint issues, если это явно зафиксировано.

## Актуальный sprint_24

Цель sprint_24: закрыть следующий DB-import semantic correctness follow-up после завершенных `060` и `063`.

| Порядок | Issue | Status | Priority | Cost | Комментарий |
|---:|---|---|---:|---:|---|
| 1 | `059-db-import-composite-foreign-key-policy.md` | planned | mid | M | выбран как ближайший связанный follow-up: policy/detection/degradation для composite FK metadata import |

## Что изменено

- Создан новый `machine/issue/sprint_24/`.
- `059-db-import-composite-foreign-key-policy.md` перенесена из `sprint_22/` в `sprint_24/`, потому что `sprint_22`/`sprint_23` уже содержат закрытые DB-import hardening slices, а `backlog/` пуст.
- Оценки и приоритеты в issue-файле не менялись: `059` остается `planned` / `mid` / `M`.
- `machine/issue/sprint_22/cost.md` получил dated correction о переносе.

## Перемещено / оценено / приоритизировано

- Перемещено:
  - `sprint_22/059-db-import-composite-foreign-key-policy.md` → `sprint_24/059-db-import-composite-foreign-key-policy.md`.
- Оценено/подтверждено:
  - `059` — M: безопасный MVP-scope, если держаться degradation/policy path; full grouped-FK model нужно выносить отдельно, если начнет разрастаться.
  - `015` — M/high/work, `022` — M/high/work: уже активные work-задачи, не переносились, чтобы не ломать историю work/progress.
  - `025` — M/high/planned: важная table-level semantic task, но ниже в очереди для этого прохода, потому что `059` продолжает уже начатую DB-import линию и меньше зависит от broader model semantics.
- Приоритизация ближайшего пула:
  1. `sprint_24/059` — mid/M/planned: следующий executable DB-import follow-up.
  2. `sprint_3/022` — high/M/work: sequence keyboard/editing MVP, продолжать отдельным work-track.
  3. `sprint_2/015` — high/M/work: non-modal editor flows, продолжать отдельным work-track.
  4. `sprint_4/025` — high/M/planned: composite constraints semantics после/в связке с решением `059`.
  5. `sprint_13/026-04`, `sprint_13/052`, `sprint_3/019`, `sprint_12/047` — M/S follow-ups ниже текущей DB-import continuity.

## L / вопросы

L-задач в `backlog/`, `work/` и ближайшем open/planned пуле не найдено; уточняющие вопросы не требуются.

## XL-декомпозиция

XL-задач в `backlog/`, `work/` и ближайшем open/planned пуле не найдено; декомпозиция не выполнялась.

## Блокеры / неясности

- Структура ясна: доступный backlog — `machine/issue/backlog/` пуст; новый active sprint — `machine/issue/sprint_24/`.
- В дереве git уже есть много незакоммиченных изменений/перемещений из предыдущих работ; этот проход изменил только issue planning structure (`sprint_24`, перенос `059`, correction в `sprint_22/cost.md`).
- Production code не менялся; тесты не запускались, потому что это planning-only проход.

## Correction / rollover — 2026-05-19 13:05 Europe/Moscow

- `059-db-import-composite-foreign-key-policy.md` перенесена в новый `sprint_26/` как следующий executable DB-import semantic follow-up после закрытого hygiene `sprint_25`.
- `sprint_24/` теперь остается historical planning point; актуальный следующий план см. в `sprint_26/cost.md`.
