# Sprint 11 актуализация / hourly backlog analysis

Актуализировано: 2026-05-17 02:04 Europe/Moscow.

## Текущий sprint_11

Фактический состав `machine/issue/sprint_11`:

| Issue | Status | Priority | Cost | Комментарий |
|---|---|---:|---:|---|
| `001-open-project-loading.md` | done | high | M | перенесен из `work/`, т.к. задача закрыта и `work/` должен содержать только активные задачи |
| `044-highlight-functional-area-outline-on-select.md` | done | high | S | закрыта; active functional-area outline реализован и проверен |
| `042-separate-table-names-with-divider-bar.md` | planned | mid | S | остается visual readability polish |

## Что изменено

- Удалено устаревшее утверждение, что backlog пуст.
- Удалены устаревшие ссылки на `041`: файла больше нет в `sprint_11`, поэтому он не считается частью текущего состава.
- `work/001-open-project-loading.md` перенесен в `sprint_11/001-open-project-loading.md`, статус сохранен `done`.
- `work/` теперь содержит только активные `in_progress` задачи: `014`, `045`.

## Блокеры / неясности

- История `041` не восстановлена автоматически: файла нет в `backlog/`, `work/` или `sprint_11/` на момент анализа.
- Остальная структура понятна: `backlog/` для невыбранных задач, `sprint_N/` для планирования/истории, `work/` для активной работы.


## Коррекция 2026-05-17 13:05 Europe/Moscow

- Предыдущее утверждение про активные `work/014` и `work/045` устарело: обе задачи теперь `done` и перенесены в `sprint_12/`.
- Текущая политика подтверждена: `work/` содержит только активные задачи; закрытые задачи лежат в sprint/history директориях.


## Актуализация 2026-05-18 16:00 Europe/Moscow

- Текущий `sprint_11`: `001` и `044` — `done`; `042` — `planned`.
- `machine/issue/work/` пуст: активных задач сейчас нет.
- `machine/issue/backlog/` пуст: все прежние backlog-файлы уже распределены по sprint/history.
- Исправлена metadata/content-гигиена: статус `044` в таблице синхронизирован с issue-файлом; stray-префикс в `042` удален.
