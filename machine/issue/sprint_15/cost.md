# Sprint 15 plan / hourly backlog analysis

Актуализировано: 2026-05-17 20:48 Europe/Moscow.

## Найдено в `machine/issue/backlog`

| Issue | Status | Priority | Cost | Решение |
|---|---|---:|---:|---|
| `049-import-tables-from-database-connection.md` | backlog | high | L | canonical implementation task; replace boundary закрыт решением SEE 20:48 |

## Актуальный `sprint_15`

| Порядок | Issue | Status | Priority | Cost | Комментарий |
|---:|---|---|---:|---:|---|
| 1 | `054-define-db-import-replace-boundary.md` | done | high | S | safety/product slice закрыт: whole selected DB diagram replace |

## Что изменено

- Актуализирован summary для `sprint_15` на 20:48 MSK.
- В `049` добавлена SEE decision-запись: repeated DB import заменяет полностью всё содержимое выбранной DB-диаграммы.
- В `054` зафиксировано решение, status `planned` → `done`.
- Новые задачи не создавались и файлы не перемещались: в backlog нет подходящих M/S/XS задач, а единственная backlog-задача имеет cost L.

## Перемещено / оценено / приоритизировано

- Перемещено в новый sprint: ничего в этом прогоне.
- Уже в текущем sprint: `054` — high / S, закрыта и разблокирует безопасный DB import.
- Оценки подтверждены:
  - `054` = S: короткое product/model решение по replace boundary и warning semantics; done.
  - `049` = L: DB connection UI + saved profiles CRUD + PostgreSQL metadata mapping + FK import + whole-diagram replace import + progress/error handling.
- Приоритеты:
  1. `049` — high / L: главный implementation item после закрытия `054`; пока остается в backlog до отдельного planning/work pass.
  2. `054` — high / S: done, boundary/metadata решение закрыто.

## L / вопросы

### `049-import-tables-from-database-connection.md`

Решено SEE 2026-05-17 20:48 MSK:
- При repeated DB import заменять полностью всё содержимое выбранной DB-диаграммы.
- Imported-subset replace не выбран.
- Metadata вроде `importSource/profile/schema/importBatchId` для MVP не требуется.
- Warning в UI должен явно предупреждать о полном удалении текущего содержимого выбранной DB-диаграммы.

## XL-декомпозиция

XL-задач в `machine/issue/backlog` не найдено; декомпозиция не выполнялась.

## Блокеры / неясности

- Структура backlog ясна: `backlog/`, `sprint_N/`, `work/` используются ожидаемо.
- Блокер для `049` по replace boundary снят: `sprint_15/054` закрыта. Остается только обычная L-size нарезка/планирование реализации.
