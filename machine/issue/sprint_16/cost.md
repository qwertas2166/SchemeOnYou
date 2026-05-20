# Sprint 16 plan / hourly backlog analysis

Актуализировано: 2026-05-18 12:04 Europe/Moscow.

## Найдено в `machine/issue/backlog`

Backlog пуст: файлов задач в `machine/issue/backlog/` не найдено.

## Актуальный `sprint_15`

| Порядок | Issue | Status | Priority | Cost | Комментарий |
|---:|---|---|---:|---:|---|
| 1 | `054-define-db-import-replace-boundary.md` | done | high | S | закрыла blocker для `049`: repeated import заменяет всё содержимое выбранной DB-диаграммы |

## `sprint_16` / DB import decomposition

| Порядок | Issue | Status | Priority | Cost | Комментарий |
|---:|---|---|---:|---:|---|
| 0 | `049-import-tables-from-database-connection.md` | done | high | L | parent/umbrella закрыт после завершения M-срезов `049-01`..`049-04` и follow-ups `056`/`057` |
| 1 | `049-01-local-connection-profiles-crud.md` | done | high | M | headless local profiles CRUD/storage реализован и покрыт core-тестами |
| 2 | `049-02-postgresql-metadata-mapper.md` | done | high | M | explicit PostgreSQL JDBC dependency; headless metadata → tables/columns/types/nullable/PK/FK mapper implemented and core-tested |
| 3 | `049-03-import-modal-ui-progress-errors.md` | done | high | M | modal UI, stage progress + table counter, schema/catalog selection, errors, destructive warning; closed scheduler-2 2026-05-18 12:30 MSK |
| 4 | `049-04-whole-diagram-replace-integration.md` | done | high | M | auto-create DB diagram if missing; replace all selected DB diagram content; no subset metadata/merge/diff |

## Что изменено

- По решению SEE 2026-05-17 21:43 `049` нарезана на M-срезы.
- Созданы четыре child issue в `machine/issue/sprint_16/`:
  - `049-01-local-connection-profiles-crud.md`
  - `049-02-postgresql-metadata-mapper.md`
  - `049-03-import-modal-ui-progress-errors.md`
  - `049-04-whole-diagram-replace-integration.md`
- В parent `049` добавлена decomposition-запись; parent остается umbrella для полного MVP capability.
- `054` остается done prerequisite; product blocker закрыт.
- SEE 2026-05-17 22:21 закрыла оставшиеся engineering-вопросы по profiles path, JDBC dependency, progress granularity, empty project behavior и ignored constraints.

## Перемещено / оценено / приоритизировано

- Новых перемещений между каталогами не выполнялось в этом pass.
- Оценки:
  - `049` = L как umbrella/full capability.
  - `049-01` = M.
  - `049-02` = M.
  - `049-03` = M.
  - `049-04` = M.
  - `054` = S / done.
  - `055` = S / done в `sprint_17`.
- Приоритет исполнения для DB import:
  1. `049-01` profiles CRUD/storage — done.
  2. `049-02` PostgreSQL metadata mapper — done.
  3. `049-04` whole-diagram replace integration — done.
  4. `049-03` modal UI/progress/errors — done; закрывает последний executable child-slice DB import MVP.

## L / вопросы

### `049-import-tables-from-database-connection.md`

Product-блокеров нет: PostgreSQL, local profiles, password storage, schema/catalog selection, full profiles CRUD, no preview/diff, no profiles migration и whole-diagram replace уже зафиксированы.

L-size blocker снят через decomposition: реализация должна идти через M-срезы `049-01`..`049-04`.

Engineering-вопросы закрыты решением SEE 2026-05-17 22:21 MSK:
1. Profiles file хранить рядом с launcher для portable app behavior — `049-01`.
2. Добавить явную Maven dependency на PostgreSQL JDBC driver — `049-02`.
3. Progress: indeterminate `Connecting` / `Reading metadata` / `Importing` / `Done` + counter по tables — `049-03`.
4. Если DB-диаграммы нет, автоматически создать новую DB-диаграмму — `049-04`/`049-03`.
5. Игнорировать всё кроме tables/columns/types/nullable/PK/FK — `049-02`.

## XL-декомпозиция

XL-задач нет; декомпозиция выполнена для L-задачи `049` на M-срезы.

## Блокеры / неясности

- Blocking product-вопросов по DB import нет.
- Engineering decisions по L закрыты; M-срезы готовы к реализации без известных product/engineering blockers.

## Scheduler-2 execution 2026-05-17 23:00 MSK

- Backlog каталог пуст, поэтому взят первый high/M planned-срез из `sprint_16`: `049-01-local-connection-profiles-crud.md`.
- Статус `049-01`: done.
- Проверка: `mvn -q -pl core test` прошел. Полный `mvn -q test` заблокирован существующими правами на client target artifact owned by `see`.

## Update 2026-05-18 09:30 MSK — scheduler-2

- `049-02-postgresql-metadata-mapper.md` выполнена: добавлен PostgreSQL JDBC dependency, headless metadata importer и тесты.
- `mvn -q -pl core test` passed.
- Full `mvn -q test` blocked by existing client target permissions on `client/target/classes/see/schemeonyou/ui/theme.css`.

## Update 2026-05-18 10:00 MSK — scheduler-2

- `049-04-whole-diagram-replace-integration.md` взята как следующий eligible high/M DB-import slice при пустом backlog и закрыта.
- Реализован headless whole-diagram replace service с auto-create DB diagram, no-duplicate repeated replace и save/load round-trip tests.
- Verification: `mvn -q -pl core test`, `mvn -q -pl core -am -DskipTests package`, `git diff --check` — passed. Full `mvn -q test` остается заблокирован существующим permission issue в `client/target/classes/.../theme.css`.

## Update 2026-05-18 10:06 MSK — scheduler-1 hourly backlog analysis

- Backlog пуст; новых переносов из `machine/issue/backlog/` нет.
- Приоритет DB import подтвержден: на тот момент оставшийся executable slice — `049-03-import-modal-ui-progress-errors.md` high/M/planned; закрыт в 12:30 MSK.
- Parent `049` остается high/L/planned umbrella, не исполнять монолитно; декомпозиция `049-01`/`049-02`/`049-03`/`049-04` done.
- Открытых вопросов по L нет; XL-задач нет.

## Update 2026-05-18 11:03 MSK — scheduler-1 hourly backlog analysis

- `machine/issue/backlog/` пуст; новых backlog-задач для оценки/переноса нет.
- `work/` пуст; новый sprint не создавался.
- Актуальный порядок DB import остается: `049-03-import-modal-ui-progress-errors.md` — тогда high/M/planned, закрыт scheduler-2 12:30 MSK.
- `049-01`, `049-02`, `049-04` подтверждены как done; parent `049` остается high/L/planned umbrella, не исполнять монолитно.
- L-вопросов нет: product/engineering decisions закрыты решениями SEE 2026-05-17 20:48 и 22:21.
- XL-задач нет; декомпозиция не выполнялась.

## Update 2026-05-18 11:30 MSK — scheduler-2

- Backlog пуст; взят следующий eligible executable item `049-03-import-modal-ui-progress-errors.md` — high/M/planned → in_progress.
- Реализован первый UI-срез DB import: top-bar/button + command-palette entry `db.import`, modal profile CRUD/select, schema/catalog field, destructive replace warning, background PostgreSQL metadata import, progress/status text and password redaction in UI errors.
- Verification: manual `javac --release 25` compile of all client sources passed; `mvn -q -pl core install` passed. Full `mvn -q -pl client -am test` remains blocked by existing permission issue copying `client/target/classes/see/schemeonyou/ui/theme.css`.

## Update 2026-05-18 12:04 MSK — scheduler-1 hourly backlog analysis

- `machine/issue/backlog/` пуст; новых backlog-задач для оценки/переноса нет.
- `machine/issue/work/056-lock-down-local-connection-profile-file-permissions.md` найден как done S follow-up и перенесен в новый `sprint_18`.
- Таблица DB import актуализирована: `049-03` на тот момент был high/M/in_progress; в 12:30 MSK закрыт scheduler-2.
- Parent `049` остается high/L/planned umbrella; открытых уточняющих вопросов нет.
- XL-задач нет.

## Update 2026-05-18 12:30 MSK — scheduler-2

- Backlog пуст; продолжен текущий high/M executable item `049-03-import-modal-ui-progress-errors.md`.
- Статус `049-03`: done. UI-срез покрывает entry point `db.import`, modal profile CRUD/select, schema/catalog input, destructive replace warning, background metadata import, progress/status, table counter, user-facing errors and password redaction.
- Verification: `mvn -q -pl core test`, `mvn -q -pl client -am -DskipTests -Dmaven.resources.skip=true test-compile`, `git diff --check` — passed.
- Full client package/test остается заблокирован существующим permission issue при копировании `client/target/classes/see/schemeonyou/ui/theme.css`.

## Update 2026-05-18 14:05 MSK — scheduler-1 hourly backlog analysis

- Parent `049` актуализирован `planned` → `done`: все executable child M-slices `049-01`..`049-04` закрыты, follow-up hardening/safety `056` и `057` закрыты.
- Backlog/work пусты; новых задач для переноса нет.
- Открытых L-вопросов и XL-задач нет.
