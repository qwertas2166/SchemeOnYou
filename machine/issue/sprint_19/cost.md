# Sprint 19 plan / hourly backlog analysis

Актуализировано: 2026-05-18 17:03 Europe/Moscow.

## Найдено в `machine/issue/backlog`

Backlog пуст: файлов задач в `machine/issue/backlog/` не найдено.

## Актуальный sprint_19

| Порядок | Issue | Status | Priority | Cost | Комментарий |
|---:|---|---|---:|---:|---|
| 1 | `057-make-db-import-replace-undo-safe-or-explicitly-committed.md` | done | high | M | safety follow-up к DB import replace; undo-safe command layer реализован, задача закрыта |

## Связанный DB import capability

| Порядок | Issue | Status | Priority | Cost | Комментарий |
|---:|---|---|---:|---:|---|
| 0 | `sprint_16/049-import-tables-from-database-connection.md` | done | high | L | parent/umbrella закрыт: child-срезы `049-01`..`049-04` закрыты, follow-ups `056`/`057` закрыты |
| 1 | `sprint_16/049-01-local-connection-profiles-crud.md` | done | high | M | profiles CRUD/storage |
| 2 | `sprint_16/049-02-postgresql-metadata-mapper.md` | done | high | M | PostgreSQL metadata mapper |
| 3 | `sprint_16/049-03-import-modal-ui-progress-errors.md` | done | high | M | modal UI/progress/errors/destructive warning |
| 4 | `sprint_16/049-04-whole-diagram-replace-integration.md` | done | high | M | whole-diagram replace + auto-create DB diagram |
| 5 | `sprint_18/056-lock-down-local-connection-profile-file-permissions.md` | done | mid | S | profiles file permission hardening |
| 6 | `sprint_19/057-make-db-import-replace-undo-safe-or-explicitly-committed.md` | done | high | M | replace undo/redo safety |

## Что изменено

- `machine/issue/backlog/` проверен: пуст.
- `machine/issue/work/` проверен: пуст.
- Новые задачи в новый sprint не переносились: подходящих задач в доступном backlog нет.
- `sprint_19/057` подтвержден как `done`; материальных изменений с прошлого hourly-прогона не найдено.
- `sprint_19/cost.md` обновлен текущим hourly-анализом 17:03.

## Перемещено / оценено / приоритизировано

- Перемещено: ничего.
- Оценки подтверждены:
  - `049` = L, закрытый umbrella/full capability.
  - `049-01`..`049-04` = M, done.
  - `056` = S, done.
  - `057` = M, done.
- Приоритизация текущего состояния:
  1. `049`/`057` — high, DB import MVP + destructive replace safety закрыты.
  2. `056` — mid, локальный hardening profiles file закрыт.
  3. Новых backlog-кандидатов нет.

## L / вопросы

### `049-import-tables-from-database-connection.md`

Открытых уточняющих вопросов нет. L была декомпозирована на `049-01`..`049-04`; все child-срезы закрыты. Product/engineering решения по PostgreSQL, local profiles, password storage, schema/catalog, full CRUD, no preview/diff, whole-diagram replace, progress, auto-create DB diagram и ignored non-MVP constraints уже зафиксированы.

## XL-декомпозиция

XL-задач в `backlog/`, `work/` и текущем sprint не найдено; декомпозиция не выполнялась.

## Блокеры / неясности

- Структура backlog ясна: `backlog/` пуст, `work/` пуст, текущий sprint_19 закрыт.
- Full Maven client/package у execution tasks ранее блокировался правами на `client/target/classes/see/schemeonyou/ui/theme.css` (owned by `see`); для backlog-анализа это не блокер.
