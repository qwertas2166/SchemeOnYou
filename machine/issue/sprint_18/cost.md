# Sprint 18 plan / hourly backlog analysis

Актуализировано: 2026-05-18 12:04 Europe/Moscow.

## Найдено в `machine/issue/backlog`

Backlog пуст: файлов задач в `machine/issue/backlog/` не найдено.

## Актуальный sprint_18

| Порядок | Issue | Status | Priority | Cost | Комментарий |
|---:|---|---|---:|---:|---|
| 1 | `056-lock-down-local-connection-profile-file-permissions.md` | done | mid | S | hardening локального profiles file; перенесено из `work/` в `sprint_18/` как закрытый S-slice |

## Соседний DB import sprint_16

| Порядок | Issue | Status | Priority | Cost | Комментарий |
|---:|---|---|---:|---:|---|
| 0 | `049-import-tables-from-database-connection.md` | planned | high | L | umbrella; не исполнять монолитно |
| 1 | `049-01-local-connection-profiles-crud.md` | done | high | M | profiles CRUD/storage slice выполнен |
| 2 | `049-02-postgresql-metadata-mapper.md` | done | high | M | PostgreSQL JDBC metadata mapper выполнен и core-tested |
| 3 | `049-04-whole-diagram-replace-integration.md` | done | high | M | replace integration выполнен |
| 4 | `049-03-import-modal-ui-progress-errors.md` | done | high | M | modal UI/progress/errors slice закрыт scheduler-2 2026-05-18 12:30 MSK |

## Что изменено

- Создан `machine/issue/sprint_18/`.
- `056-lock-down-local-connection-profile-file-permissions.md` перенесена из `machine/issue/work/` в `machine/issue/sprint_18/`.
- `machine/issue/work/` после переноса пуст.
- Backlog оставлен пустым: новых незапланированных задач для оценки/переноса нет.
- Подтверждено, что `sprint_16/049-03-import-modal-ui-progress-errors.md` был high/M/in_progress; закрыт scheduler-2 2026-05-18 12:30 MSK.

## Перемещено / оценено / приоритизировано

- Перемещено: `work/056` → `sprint_18/056`.
- Оценка подтверждена: `056` = S, priority mid, status done.
- Приоритеты подтверждены:
  1. `049-03` — high/M/done, UI gap DB import MVP закрыт.
  2. `056` — mid/S/done, hardening follow-up к `049-01`.
  3. `049` — high/L/planned parent umbrella, только для трекинга decomposed capability.

## L / вопросы

### `049-import-tables-from-database-connection.md`

Открытых уточняющих вопросов нет: L уже декомпозирована на M-срезы `049-01`..`049-04`, product/engineering decisions закрыты.

Закрытые решения:
1. PostgreSQL first; explicit JDBC dependency.
2. Local launcher-adjacent profiles file; plain-text password допустим для MVP.
3. Full profiles CRUD; version/migration profiles file не нужен.
4. Schema/catalog selection нужен.
5. Progress: `Connecting` / `Reading metadata` / `Importing` / `Done` + tables counter.
6. Repeated import полностью заменяет выбранную DB-диаграмму; preview/diff не нужен.
7. Если DB-диаграммы нет — создать автоматически.
8. MVP импортирует только tables/columns/types/nullable/PK/FK.

## XL-декомпозиция

XL-задач в backlog/current sprint не найдено; декомпозиция не выполнялась.

## Блокеры / неясности

- Структура backlog ясна: `backlog/` пуст, `work/` пуст, текущий закрытый hardening slice лежит в `sprint_18/`.
- Full Maven client test у соседних execution tasks остается заблокирован существующими правами на `client/target/classes/see/schemeonyou/ui/theme.css`; для backlog-анализа это не блокирует перенос/приоритизацию.
