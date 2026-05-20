# Sprint 6 plan / hourly backlog analysis

Актуализировано: 2026-05-16 15:03 Europe/Moscow.

## Найдено в `machine/issue/backlog`

Найдена 1 задача:

| Issue | Status | Priority | Cost | Решение |
|---|---|---:|---:|---|
| `026-extract-application-presenters-for-headless-ui-tests.md` | backlog | mid | L | оставить в `backlog/`; umbrella presenter-refactor уже разрезан и частично закрыт через `026-01`, `026-02`, `026-03`; перенос целиком в новый спринт рискован без выбора следующего seam |

XL-задач в backlog не найдено.

## Состав текущего sprint_6

| Issue | Status | Priority | Cost | Комментарий |
|---|---|---:|---:|---|
| `033-sync-human-requirements-shortcut-model.md` | planned | mid | S | Единственная planned задача sprint_6. Оставлена без изменения статуса: безопасный docs-consistency filler, но ниже active high/M release gaps. |

## Что изменено

- Обновлен этот отчет `sprint_6/cost.md` по состоянию на 2026-05-16 15:03 Europe/Moscow.
- Файлы задач не переносились и metadata задач не менялась.
- Текущий sprint_6 актуализирован: `033` остается единственной planned задачей sprint_6.

## Перемещено / оценено / приоритизировано

- Перемещено: ничего — в доступном `backlog/` нет S/M/XS-задач, безопасных для нового спринта.
- Новый sprint не создан: единственный backlog-кандидат `026` имеет размер L и требует решения по следующему seam перед переносом.
- Оценки сохранены: `026` = L, `033` = S.
- Приоритизация на ближайший проход:
  1. `work/001`, `work/020`, `work/024` — active high release gaps.
  2. `work/028`, `work/032` — active/work high MVP consistency gaps.
  3. `sprint_4/025` — planned high/M product semantics gap.
  4. `sprint_6/033` — planned mid/S documentation consistency, безопасный filler после high gaps.
  5. `backlog/026` — mid/L umbrella, держать в backlog до решения по следующему seam или закрытию parent.

## L/XL

- L в backlog: `026-extract-application-presenters-for-headless-ui-tests.md`.
- L в active work: `work/007-sequence-diagram-mvp-ui.md`, `work/010-tests-and-release-gates.md` — это parent-направления, не новые backlog-кандидаты.
- XL: не найдено.

## Вопросы по L

### `026`

- После уже закрытых `command routing`, `document lifecycle`, `sequence editing` и `canvas hit-test` нужен ли еще один presenter seam перед закрытием parent?
- Если нужен следующий seam: что важнее — inspector state, storage/export flow или validation/error surface?
- Держим `026` как umbrella backlog-задачу для будущих seams, или закрываем parent после фиксации выполненных child-slices?

### `007`

- Parent `007` закрываем после выполненных sequence-срезов `021/022/030`, или держим открытым до отдельного sequence UI polish?

### `010`

- Release gate фиксируем как `compile + tests + package`, или нужен отдельный manual release checklist в репозитории?

## XL-декомпозиция

XL-задач не найдено; декомпозиция не выполнялась.

## Блокеры / неясности

- Структура backlog понятна.
- В `backlog/` осталась только L-задача `026`; переносить ее целиком в новый спринт не стоит.
- Для следующего переноса нужен новый S/M backlog item или решение SEE по следующему seam внутри `026`.
- Верификационный риск сохраняется: scheduler может обновлять issue metadata, но build/test/package gate зависит от доступности локальных JDK/Maven путей.
## Update 2026-05-16 15:03 MSK — решение по `026`

SEE выбрал следующий seam для parent `026`: **inspector, editing**.

Добавлено в текущий sprint_6:

| Issue | Status | Priority | Cost | Комментарий |
|---|---|---:|---:|---|
| `026-04-inspector-editing-presenter-slice.md` | planned | mid | M | Новый child-slice из umbrella `026`; scope ограничен inspector model + editing decisions без file dialogs/errors/canvas rendering |

Parent `backlog/026` остается umbrella L-задачей в backlog; целиком в спринт не переносится.
## Update 2026-05-16 15:44 MSK — решения по parent/L вопросам

SEE принял решения:

- `026` — parent закрываем; дополнительных presenter seams после inspector/editing не требуется как условие parent.
- `007` — parent закрываем; дополнительный sequence UI polish не нужен как условие закрытия parent.
- `012` — достаточно как ранее зафиксировано; дополнительных работ не добавлять.

## 2026-05-19 correction — issue history paths

`033-sync-human-requirements-shortcut-model.md` was later completed; its canonical history stub is `machine/issue/sprint_9/033-sync-human-requirements-shortcut-model.md` with `status - done`. Older `sprint_6/033`, `backlog/033`, or `work/033` mentions are historical only. Older `work/024` and `work/032` mentions are also historical; see sprint_9 history stubs.
