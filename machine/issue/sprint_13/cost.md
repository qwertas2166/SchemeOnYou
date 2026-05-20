# Sprint 13 plan / hourly backlog analysis

Актуализировано: 2026-05-17 15:00 Europe/Moscow.

## Найдено в `machine/issue/backlog`

| Issue | Status | Priority | Cost | Решение |
|---|---|---:|---:|---|
| `010-tests-and-release-gates.md` | backlog | mid | S | оставить в backlog как небольшой metadata/docs cleanup; обязательная test-suite нарезка снята решением SEE 2026-05-17 14:43 |
| `049-import-tables-from-database-connection.md` | backlog | high | L | оставить в backlog; high для MVP, но брать после текущих UI/refactor seams либо отдельным sprint slice |

## Актуальный `sprint_13`

| Порядок | Issue | Status | Priority | Cost | Комментарий |
|---:|---|---|---:|---:|---|
| 1 | `026-04-inspector-editing-presenter-slice.md` | planned | high | M | главный seam: headless inspector/editing; снижает риск дальнейших UI-изменений |
| 2 | `052-extract-functional-area-initialization-classes.md` | planned | mid | M | продолжает уменьшение `SchemeOnYouApplication` после presenter seam |
| 3 | `013-canvas-drag-and-drop-positioning.md` | done | mid | M | реализовано: drag DB table / sequence participant, one undoable move on release, layout policy preserves manual bounds |

## Приоритизация backlog

1. `049-import-tables-from-database-connection.md` — high / L: MVP capability, но слишком крупная для добавления поверх текущего sprint_13.
2. `010-tests-and-release-gates.md` — mid / S: небольшой cleanup для фиксации MVP gate без обязательных tests/UI smoke/CI.

## Перенос в новый спринт

- Новых переносов не сделано: `010` стал S, но `sprint_13` уже содержит три M-задачи; перенос лучше делать отдельным planning pass, не в scheduler-3 refinement.
- `sprint_13` оставлен без расширения, чтобы не перегрузить спринт.

## L / вопросы

### `049-import-tables-from-database-connection.md`

Решено SEE 2026-05-17 15:26 MSK:
- Первый slice делать вертикально end-to-end от UI до модели.
- Saved profiles: полный CRUD.
- Version/migration для локального profiles-file в MVP не требуется.

### `010-tests-and-release-gates.md`

- Больше не L: после решения SEE 2026-05-17 14:43 открытых L-вопросов нет.

## XL-декомпозиция

XL-задач в `machine/issue/backlog` не найдено; декомпозиция не выполнялась.

## Блокеры / неясности

- Структура backlog ясна: `backlog/`, `sprint_13/`, `work/` используются ожидаемо.
- Локальные Java/Maven доступны в `/home/openclaw/workspace/java`; старые notes про отсутствие `mvn/java/javac` больше не считаю актуальным блокером.

## Update 2026-05-17 15:04 MSK — scheduler-1

- `010-tests-and-release-gates.md` moved from `backlog/` to new `sprint_14/` as planned S cleanup.
- `sprint_13` composition unchanged: three planned M tasks remain enough for the current sprint.
- `backlog/` now contains only `049-import-tables-from-database-connection.md` (`high`/`L`).

## Update 2026-05-18 20:33 MSK — scheduler-3

- `013-canvas-drag-and-drop-positioning.md` marked `done` after code review and local verification.
- Remaining sprint_13 planned scope: `026-04` and `052`.
## Scheduler-2 execution — 2026-05-19 17:00 Europe/Moscow

- `machine/issue/backlog/` пуст, поэтому взята ближайшая уже спланированная подходящая задача: `026-04-inspector-editing-presenter-slice.md` — high/M.
- Причина выбора: самый приоритетный eligible planned item размера M/S; завершает уже начатый inspector/editing presenter seam.
- Сделано: проверена реализация `InspectorPresenter` и thin wiring в `SchemeOnYouApplication`; production code в этом проходе не менялся.
- Проверка: `mvn -q -pl client -am -Dtest=InspectorPresenterTest -Dsurefire.failIfNoSpecifiedTests=false test` OK на локальном Java/Maven toolchain.
- Статус: `026-04` переведена `planned → done`.
- Блокер: backlog пуст, новых backlog-задач для переноса нет.
