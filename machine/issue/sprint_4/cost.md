# Sprint 4 plan / status

Актуализировано: 2026-05-15 18:08 Europe/Moscow.

## Найдено в `machine/issue/backlog`

На момент анализа до переноса найдено 2 задачи:

| Issue | Priority | Cost | Решение |
|---|---:|---:|---|
| `026-extract-application-presenters-for-headless-ui-tests.md` | mid | L | оставить в `backlog/`; первый M-срез уже в `work/026-01-command-router-presenter-slice.md`; уточнения по первому seam получены, следующий cut нужен после завершения 026-01 |
| `031-normalize-issue-status-and-sprint-indexes.md` | mid | S | перенесена в `sprint_5/`, status -> planned: маленькая hygiene-задача для scheduler-планирования |

После переноса в `machine/issue/backlog` осталась L-задача `026`.

## Состав sprint_4

| Issue | Status | Priority | Cost | Комментарий |
|---|---|---:|---:|---|
| `027-wire-missing-mvp-global-shortcuts.md` | in_progress (`work/`) | high | S | Уже взята в работу; быстрый MVP keyboard gap: `Ctrl+Shift+P` и `Ctrl+N`. |
| `029-make-key-log-dev-only-for-release.md` | planned | high | S | Компактный release/privacy polish: скрыть или dev-only F12 key log из release UI/help. |
| `025-db-composite-constraints-and-relation-semantics.md` | planned | high | M | Model/storage/validation gap DB relation semantics; брать после стабилизации save/load writer/reader задач `001`/`016`. |

## Приоритет выполнения

1. `027` — high/S, in_progress: довести и закрыть, чтобы sprint_4 не держал stale planned entry.
2. `029` — high/S: маленький release/privacy риск, лучше закрыть до release-readiness checks.
3. `025` — high/M: важная семантика DB model/storage, но рискованнее из-за JSON/storage changes.

## L/XL

- L в backlog: `026-extract-application-presenters-for-headless-ui-tests.md`.
- L в активной работе вне sprint_4: `007-sequence-diagram-mvp-ui.md`, `010-tests-and-release-gates.md` — уже уточнены и идут через M/S-срезы.
- XL: не найдено.

## Вопросы по L

### `026`

- После `026-01` следующим seam подтверждаем `document lifecycle guard`, или сначала добиваем command palette/non-modal edge cases?
- Для document lifecycle достаточно решений `new/open/dirty/unsaved`, или включать save-as/current-file path в этот же M-срез?

### `007`

- Для sequence delete/edit: сначала message delete + undo, или participant delete с cascade preview?
- Нужно ли переносить оставшийся scope `007` полностью в подзадачи `021/022/030`, чтобы parent можно было закрывать как decomposed?

### `010`

- Минимальный gate уже определен как `compile + tests + package`; нужен ли отдельный manual release checklist-файл в рамках этой L-задачи?
- Какие зоны первыми покрывать после JSON/undo: validator, SVG export или presenter/controller headless tests?

## XL-декомпозиция

XL-задач не найдено; декомпозиция не потребовалась.

## Блокеры / неясности

- Рассинхрон path/status/index, найденный в прошлом проходе (`work/030` с `status - backlog`), исправлен в sprint_5 через `031`.
- Оставшийся риск: sprint summaries могут снова устаревать при ручных переносах; перед новым переносом сверять path/status/cost summary checklist из `sprint_5/031`.
