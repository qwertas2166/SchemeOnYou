# Sprint 17 plan / hourly backlog analysis

Актуализировано: 2026-05-18 11:03 Europe/Moscow.

## Найдено в `machine/issue/backlog`

Backlog пуст: файлов задач в `machine/issue/backlog/` не найдено.

## Актуальный sprint / work

| Порядок | Issue | Status | Priority | Cost | Комментарий |
|---:|---|---|---:|---:|---|
| 1 | `055-scope-shortcut-help-to-active-diagram-context.md` | done | mid | S | shortcut/help drift закрыт; перенесено из `work/` в `sprint_17/` как завершенный S-slice |

## Соседний запланированный sprint_16 / DB import

| Порядок | Issue | Status | Priority | Cost | Комментарий |
|---:|---|---|---:|---:|---|
| 0 | `049-import-tables-from-database-connection.md` | planned | high | L | umbrella; не исполнять монолитно |
| 1 | `049-01-local-connection-profiles-crud.md` | done | high | M | profiles CRUD/storage slice выполнен |
| 2 | `049-02-postgresql-metadata-mapper.md` | done | high | M | PostgreSQL JDBC metadata mapper выполнен и core-tested |
| 3 | `049-04-whole-diagram-replace-integration.md` | done | high | M | service/integration slice закрыт; auto-create DB diagram + whole replace покрыты тестами |
| 4 | `049-03-import-modal-ui-progress-errors.md` | done | high | M | modal UI/progress/errors slice закрыт scheduler-2 2026-05-18 12:30 MSK |

## Что изменено

- Создан `machine/issue/sprint_17/`.
- `055-scope-shortcut-help-to-active-diagram-context.md` перенесена из `machine/issue/work/` в `machine/issue/sprint_17/`.
- Backlog оставлен пустым: подходящих незапланированных задач для переноса нет.
- Ежечасная актуализация 2026-05-18 09:36 MSK: новых backlog/work задач не найдено; sprint_17 остается закрытым по единственной S-задаче.
- Scheduler-3 2026-05-18 09:37 MSK: синхронизирован соседний DB import статус после выполнения `049-02`; backlog по-прежнему пуст.

## Перемещено / оценено / приоритизировано

- Перемещено: `work/055` → `sprint_17/055`.
- Оценка подтверждена: `055` = S, priority mid, status done.
- Приоритеты подтверждены:
  1. `049-01` — high/M/done.
  2. `049-02` — high/M/done.
  3. `049-04` — high/M/done.
  4. `049-03` — high/M/planned, следующий executable UI slice.
  5. `055` — mid/S/done, больше не кандидат на работу.

## L / вопросы

### `049-import-tables-from-database-connection.md`

Открытых product-вопросов нет; L уже декомпозирована на M-срезы `049-01`..`049-04`.

Закрыто в выполненных M-срезах:
1. physical path для local profiles file — `049-01` done.
2. PostgreSQL JDBC dependency/loading и ignored non-MVP constraints — `049-02` done.
3. empty project / create-vs-select DB diagram behavior — `049-04` done.

Остается в planned M-срезах:
1. progress granularity/stage UI, profile/schema modal flow и destructive warning — `049-03`.

## XL-декомпозиция

XL-задач в backlog/current sprint не найдено; декомпозиция не выполнялась.

## Блокеры / неясности

- Структура backlog ясна: `backlog/` пуст, `work/` после переноса пуст, planned DB import лежит в `sprint_16/`, завершенный shortcut/help slice — в `sprint_17/`.
- Product-блокеров нет.

## Update 2026-05-18 10:06 MSK — scheduler-1 hourly backlog analysis

- `machine/issue/backlog/` снова проверен: файлов задач нет.
- `work/` пуст; новых задач для переноса в новый sprint нет.
- Тогда актуальная незакрытая работа: `sprint_16/049-03-import-modal-ui-progress-errors.md` — high/M/planned; закрыта scheduler-2 2026-05-18 12:30 MSK.
- L-задача `sprint_16/049-import-tables-from-database-connection.md` остается umbrella high/L/planned и уже декомпозирована; открытых уточняющих вопросов нет.
- XL-задач не найдено.

## Update 2026-05-18 11:03 MSK — scheduler-1 hourly backlog analysis

- `machine/issue/backlog/` проверен: файлов задач нет.
- `machine/issue/work/` пуст; переносить в новый sprint нечего.
- Текущий актуальный executable item: `sprint_16/049-03-import-modal-ui-progress-errors.md` — high/M/done; UI slice DB import MVP закрыт scheduler-2 2026-05-18 12:30 MSK.
- `049-04-whole-diagram-replace-integration.md` синхронизирован как done в соседней таблице sprint_17/cost.md.
- Приоритет подтвержден: `049-03` выше закрытого `055`, потому что закрывает оставшийся UI gap DB import MVP.
- L-задача `sprint_16/049-import-tables-from-database-connection.md` остается parent umbrella high/L/planned; вопросы закрыты, реализация идет через M-slices.
- XL-задач не найдено; декомпозиция не требовалась.

## Update 2026-05-18 10:00 MSK — scheduler-2

- `049-04-whole-diagram-replace-integration.md` взята как следующий eligible high/M DB-import slice при пустом backlog и закрыта.
- Реализован headless whole-diagram replace service с auto-create DB diagram, no-duplicate repeated replace и save/load round-trip tests.
- Verification: `mvn -q -pl core test`, `mvn -q -pl core -am -DskipTests package`, `git diff --check` — passed. Full `mvn -q test` остается заблокирован существующим permission issue в `client/target/classes/.../theme.css`.
