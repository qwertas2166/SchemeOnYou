# Sprint 12 plan / hourly backlog analysis

Актуализировано: 2026-05-17 02:04 Europe/Moscow.

## Найдено в `machine/issue/backlog` до переноса

| Issue | Status | Priority | Cost | Решение |
|---|---|---:|---:|---|
| `010-tests-and-release-gates.md` | backlog | mid | L | оставить в backlog; L требует уточнений/нарезки |
| `013-canvas-drag-and-drop-positioning.md` | backlog | mid | M | оставить в backlog; ниже срочность, есть риски layout policy |
| `026-04-inspector-editing-presenter-slice.md` | backlog | mid | M | оставить в backlog; полезно после завершения `045` |
| `028-expand-find-element-scope-and-go-linked-navigation.md` | backlog | high | M | перенести в sprint_12; высокий keyboard-first gap, уже есть первый slice |
| `046-align-central-area-to-canvas-edges.md` | backlog | mid | S | перенести в sprint_12; повысить до high как layout foundation |
| `047-zoom-with-mouse-wheel.md` | backlog | mid | S | перенести в sprint_12; оставить mid как малый UX-slice |
| `048-status-focus-shows-selected-ui-element.md` | backlog | mid | S | перенести в sprint_12; повысить до high, связан с focus/inspector work |
| `049-import-tables-from-database-connection.md` | backlog | high | L | оставить в backlog; L требует продуктовых решений |
| `050-refactor-manual-accessors-to-lombok.md` | backlog | low | M | оставить в backlog; refactor низкой срочности |
| `051-normalize-issue-tree-after-work-cleanup.md` | backlog | mid | S | перенести в sprint_12; повысить до high как scheduler hygiene |

## Состав нового `sprint_12`

| Порядок | Issue | Status | Priority | Cost | Комментарий |
|---:|---|---|---:|---:|---|
| 1 | `051-normalize-issue-tree-after-work-cleanup.md` | planned | high | S | сначала закрепить консистентность issue tree |
| 2 | `046-align-central-area-to-canvas-edges.md` | planned | high | S | layout foundation для canvas/central area |
| 3 | `048-status-focus-shows-selected-ui-element.md` | planned | high | S | visibility текущего selectable/focusable элемента |
| 4 | `028-expand-find-element-scope-and-go-linked-navigation.md` | done | high | M | keyboard-first search scope закрыт; help больше не рекламирует unwired `Space G T` |
| 5 | `047-zoom-with-mouse-wheel.md` | planned | mid | S | небольшой UX-срез после viewport/zoom base |

## Что изменено

- Создан `machine/issue/sprint_12/`.
- Из `machine/issue/backlog/` в `sprint_12/` перенесены: `028`, `046`, `047`, `048`, `051`.
- В перенесенных задачах статус изменен `backlog` → `planned`.
- Приоритеты актуализированы:
  - `046`: `mid` → `high`.
  - `048`: `mid` → `high`.
  - `051`: `mid` → `high`.
  - `028`: сохранен `high`.
  - `047`: сохранен `mid`.
- В каждую перенесенную задачу добавлена `progress`-запись о переносе.

## Оставшийся backlog после переноса

- `010-tests-and-release-gates.md` — L, mid.
- `013-canvas-drag-and-drop-positioning.md` — M, mid.
- `026-04-inspector-editing-presenter-slice.md` — M, mid.
- `049-import-tables-from-database-connection.md` — L, high.
- `050-refactor-manual-accessors-to-lombok.md` — M, low.

## L / вопросы

### `010-tests-and-release-gates.md`

1. Какие test suites обязательны для MVP-gate кроме уже согласованных `compile + tests + package`?
2. Нужно ли отдельно фиксировать `mvn -q test` и `mvn -q -DskipTests package` как два явных локальных contract command?
3. Какие области покрывать первыми: command layer, storage round-trip, validator, SVG/exporter или layout?
4. Нужен ли минимальный CI позже, если локальный Maven gate уже считается source of truth?

### `049-import-tables-from-database-connection.md`

1. Какие СУБД целевые для MVP: PostgreSQL, MySQL/MariaDB, SQLite, H2, другое?
2. Как пользователь задает connection settings: UI form, config file, JDBC URL или импорт из существующего проекта?
3. Поведение повторного импорта: merge, replace selected schema, create new diagram, или ask each time?
4. Нужно ли хранить credentials в проекте, или только transient session settings?
5. Должен ли MVP импортировать только tables/columns/PK/FK, или еще indexes/unique constraints/comments?

## XL-декомпозиция

XL-задач в `machine/issue/backlog` не найдено; декомпозиция не выполнялась.

## Блокеры / неясности

- Блокеров по структуре backlog нет.
- Есть историческая неясность по `041`: старый `sprint_11/cost.md` ссылался на задачу, но файла в текущем дереве нет.


## Актуализация 2026-05-17 13:05 Europe/Moscow

- `work/014`, `work/045`, `work/053` имели `status - done`; перенесены в `sprint_12/` как закрытые артефакты, чтобы `work/` содержал только активные задачи.
- `work/050-refactor-manual-accessors-to-lombok.md` остается единственной активной задачей (`in_progress`, low, M).
- Плановые задачи sprint_12 без изменения scope: `051`, `046`, `048`, `028`, `047`.
- После нового анализа backlog `013`, `026-04`, `052` перенесены в `sprint_13`; `010` и `049` оставлены в backlog как L.

## Коррекция 2026-05-17 16:03 Europe/Moscow

- `work/050-refactor-manual-accessors-to-lombok.md` имел `status - done`; перенесен в `sprint_12/050-refactor-manual-accessors-to-lombok.md` как закрытый артефакт.
- `work/` после cleanup не содержит активных задач.
- Это закрывает текущий обнаруженный path/status drift для `050`; product/code scope не менялся.


## Актуализация 2026-05-18 16:00 Europe/Moscow

Текущий status snapshot для `sprint_12` после cleanup:

| Issue | Status | Priority | Cost | Комментарий |
|---|---|---:|---:|---|
| `014-apply-modern-dark-ide-theme.md` | done | high | M | закрытый артефакт перенесен в sprint/history |
| `045-inspector-fields-view-edit-modes.md` | done | high | M | закрытый артефакт перенесен в sprint/history |
| `046-align-central-area-to-canvas-edges.md` | planned | high | S | актуальная малая layout-задача |
| `047-zoom-with-mouse-wheel.md` | planned | mid | S | актуальная малая UX-задача |
| `048-status-focus-shows-selected-ui-element.md` | done | high | S | закрыта ранее; status/file location синхронизированы |
| `050-refactor-manual-accessors-to-lombok.md` | done | low | M | закрыта и перенесена из `work/` |
| `051-normalize-issue-tree-after-work-cleanup.md` | done | high | S | текущий cleanup выполнен 2026-05-18 16:00 |
| `053-layer-left-menu-and-inspector-above-canvas.md` | done | mid | S | закрыта и находится в sprint/history |

Проверка политики: `work/` пуст, `backlog/` пуст, planned-файлы остались только в sprint/history, done-файлы не лежат в `work/`.
## Scheduler-2 update 2026-05-18 17:30 MSK

- `backlog/` пуст; выбрана highest-priority suitable planned задача `028` (high/M), потому что она уже была частично реализована и оставался безопасный shortcut/help drift.
- `028` закрыта: `Space G T` убран из advertised shortcuts/help как unwired command; `Space G S` отображается как Find element и мапится на `element.find`.
- Verification: `mvn -q -pl client -am test`, `git diff --check` — pass.
