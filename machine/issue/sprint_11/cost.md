# Sprint 11 plan / hourly backlog analysis

Актуализировано: 2026-05-17 01:06 Europe/Moscow.

## Найдено в `machine/issue/backlog`

| Issue | Status | Priority | Cost | Решение |
|---|---|---:|---:|---|
| `041-pin-left-menu-and-inspector-to-window-edges.md` | backlog | mid | S | перенести в `sprint_11`; повысить до high как layout-базу для functional areas |
| `042-separate-table-names-with-divider-bar.md` | backlog | mid | S | перенести в `sprint_11`; оставить mid как visual readability polish |
| `044-highlight-functional-area-outline-on-select.md` | backlog | mid | S | перенести в `sprint_11`; повысить до high как UX/focus visibility fix |

Активных L/XL-задач в backlog не найдено.

## Актуализация текущего спринта

`sprint_10/` содержит только `cost.md`; единственная задача sprint_10 (`043-prune-unused-focus-traversal-cycle.md`) уже перенесена в `work/` и закрыта в 2026-05-17 00:30 MSK. Поэтому sprint_10 считается завершенным для планирования.

## Состав нового `sprint_11`

| Порядок | Issue | Status | Priority | Cost | Комментарий |
|---:|---|---|---:|---:|---|
| 1 | `041-pin-left-menu-and-inspector-to-window-edges.md` | planned | high | S | сначала стабилизировать layout left menu / inspector / canvas |
| 2 | `044-highlight-functional-area-outline-on-select.md` | planned | high | S | затем сделать активную functional area визуально очевидной |
| 3 | `042-separate-table-names-with-divider-bar.md` | planned | mid | S | после layout/focus polish улучшить читаемость таблиц |

## Что изменено

- Создан `machine/issue/sprint_11/`.
- Из `machine/issue/backlog/` в `sprint_11/` перенесены задачи `041`, `044`, `042`.
- В перенесенных задачах статус изменен `backlog` → `planned`.
- Приоритеты актуализированы:
  - `041`: `mid` → `high`.
  - `044`: `mid` → `high`.
  - `042`: сохранен `mid`.
- В каждую задачу добавлена `progress`-запись о переносе и причине приоритета.
- `machine/issue/backlog/` после переноса пуст.

## Перемещено / оценено / приоритизировано

- Перемещено: `041`, `044`, `042` → `sprint_11/`.
- Оценки сохранены: все три задачи `S`.
- Приоритизация sprint_11: `041` → `044` → `042`.

## L / вопросы

Новых L-задач в `machine/issue/backlog` нет; уточняющие вопросы не требуются.

Существующие L вне backlog:
- `work/010-tests-and-release-gates.md` — L/in_progress; вопросы уже были зафиксированы ранее.
- `work/007-sequence-diagram-mvp-ui.md` — L/done; вопросов нет.
- `sprint_8/026-extract-application-presenters-for-headless-ui-tests.md` — L/done; parent закрыт, вопросов нет.

## XL-декомпозиция

XL-задач в `machine/issue/backlog` не найдено; декомпозиция не выполнялась.

## Блокеры / неясности

- Структура backlog понятна: `backlog/` для новых задач, `sprint_N/` для планирования, `work/` для взятых/закрытых задач.
- Блокеров по планированию нет.
