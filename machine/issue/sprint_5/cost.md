# Sprint 5 plan / hourly backlog analysis

Актуализировано: 2026-05-15 19:04 Europe/Moscow.

## Найдено в `machine/issue/backlog`

| Issue | Priority | Cost | Решение |
|---|---:|---:|---|
| `026-extract-application-presenters-for-headless-ui-tests.md` | mid | L | оставить в `backlog/`; parent L уже частично декомпозирован и 2 child-slice завершены (`026-01`, `026-02`) |

## Состав sprint_5

| Issue | Status | Priority | Cost | Комментарий |
|---|---|---:|---:|---|
| `031-normalize-issue-status-and-sprint-indexes.md` | done | mid | S | Выполнено: исправлен status у `work/030`, обновлены sprint summaries/checklist для path/status/cost consistency. |

## Что изменено

- `work/030-sequence-delete-undo-safe.md`: `status - backlog` -> `status - work`, потому что файл уже находится в `work/` и имеет active progress.
- `sprint_5/031-normalize-issue-status-and-sprint-indexes.md`: `planned` -> `done`; добавлен progress и manual checklist для будущих scheduler-проходов.
- `sprint_4/cost.md`: убрана stale-блокировка про `work/030`; заменена на актуальный residual risk про ручные переносы.
- `sprint_5/cost.md`: обновлен текущий отчет/приоритизация.

## Перемещено / оценено / приоритизировано

- Новых переносов из `backlog/` не выполнено: единственная доступная backlog-задача `026` имеет размер L и не подходит для прямого переноса в спринт без нового M/S-slice.
- Оценки сохранены: `026` = L, `031` = S, `030` = M.
- Приоритет сейчас:
  1. active high/M release gaps в `work/`: `030`, `024`, `020`, `027`, `028`.
  2. next planned high/S release polish: `sprint_4/029`.
  3. `026` держать как parent-refactor backlog до выбора следующего безопасного seam.

## L/XL

- L в backlog: `026-extract-application-presenters-for-headless-ui-tests.md`.
- L в active work: `007-sequence-diagram-mvp-ui.md`, `010-tests-and-release-gates.md` — уже идут через M/S-срезы.
- XL: не найдено.

## Вопросы по L

### `026`

- Следующий presenter seam после уже закрытых `command routing` и `document lifecycle`: `sequence editing flow` или `canvas selection/hit-testing`?
- Нужно ли создать отдельную M-задачу `026-03-sequence-editing-presenter-slice`, или sequence presenter лучше закрывать внутри уже существующей `022`?

### `007`

- Parent `007` закрываем после выполнения `021/022/030`, или держим открытым до полного визуального sequence UI polish?

### `010`

- Минимальный release gate фиксируем как `compile + tests + package`, или добавляем отдельный manual release checklist в репозиторий?

## XL-декомпозиция

XL-задач не найдено; декомпозиция не выполнялась.

## Блокеры / неясности

- Структура backlog понятна.
- Блокер верификации кода по `030` остается из issue progress: на host нет `mvn`/`javac` в PATH, поэтому scheduler может актуализировать metadata, но не подтвердить Java build/test gate.

## 2026-05-19 correction — issue history path

`030-sequence-delete-undo-safe.md` is no longer current `work/`; its canonical history stub is `machine/issue/sprint_5/030-sequence-delete-undo-safe.md` with `status - done`. Older `work/030` mentions are historical only and must not be used as active scheduler input.
