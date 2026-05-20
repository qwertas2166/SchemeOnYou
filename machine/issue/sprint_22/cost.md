# Sprint 22 plan / hourly backlog analysis

Актуализировано: 2026-05-19 11:06 Europe/Moscow.

## Найдено в `machine/issue/backlog`

`machine/issue/backlog/` пуст: файлов задач не найдено.

`machine/issue/work/` тоже пуст. Согласно текущей практике из `consistency-checklist.md`, при пустом backlog scheduler актуализирует текущий sprint и выбирает следующую planned-задачу из активного sprint, а не создает лишний новый sprint.

## Актуальный sprint_22

Цель sprint_22: закрыть оставшиеся release-safety и semantic-correctness gaps вокруг PostgreSQL metadata import.

| Порядок | Issue | Status | Priority | Cost | Комментарий |
|---:|---|---|---:|---:|---|
| 1 | `060-db-import-running-cancel-timeouts-and-close-safety.md` | done | high | M | закрыто: cancel/close guard, late-result ignore, bounded JDBC timeouts; full `mvn -q test` ранее прошел |
| 2 | `059-db-import-composite-foreign-key-policy.md` | planned | mid | M | следующий executable item: policy/detection/degradation для composite FK metadata import |

## Что изменено

- `sprint_22/cost.md` обновлен текущим hourly-анализом на 11:06 MSK.
- Файлы задач не перемещались: `backlog/` пуст, а в `sprint_22` еще есть подходящая planned M-задача `059`.
- Новый sprint не создавался: `sprint_22` остается активным до закрытия `059`.
- Оценки и приоритеты подтверждены без изменения metadata в issue-файлах.

## Перемещено / оценено / приоритизировано

- Перемещено: ничего.
- Оценено/подтверждено:
  - `060` — M / high / done: lifecycle safety закрыта и больше не является кандидатом на работу.
  - `059` — M / mid / planned: scope ограничен DB-import policy для composite FK; если full grouped-FK model начнет разрастаться, нужно идти degradation path или выделять отдельную задачу.
- Приоритизация sprint_22:
  1. `060` — high/M/done, уже закрытый release-safety blocker.
  2. `059` — mid/M/planned, следующий по очереди semantic-correctness follow-up.

## L / вопросы

L-задач в `backlog/`, `work/` и активном `sprint_22` не найдено; уточняющие вопросы не требуются.

## XL-декомпозиция

XL-задач в `backlog/`, `work/` и активном `sprint_22` не найдено; декомпозиция не выполнялась.

## Блокеры / неясности

- Структура ясна: доступный backlog — `machine/issue/backlog/` пуст, active sprint — `machine/issue/sprint_22/`, active work — `machine/issue/work/` пуст.
- Планировочный проход production code не менял; тесты не запускались.
- В дереве git уже есть много незакоммиченных изменений/перемещений из предыдущих работ; этот проход изменил только `machine/issue/sprint_22/cost.md`.

## Correction / rollover — 2026-05-19 12:04 Europe/Moscow

- `059-db-import-composite-foreign-key-policy.md` перенесена в новый `sprint_24/` как следующий executable DB-import semantic follow-up после закрытых `060` и `063`.
- `sprint_22/` теперь содержит закрытую `060`; актуальный следующий план см. в `sprint_24/cost.md`.
