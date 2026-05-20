# Sprint 21 plan / hourly backlog analysis

Актуализировано: 2026-05-19 10:00 Europe/Moscow.

## Найдено в `machine/issue/backlog`

На 2026-05-19 10:00 MSK найден и взят один high/XS hygiene item: `062-normalize-work-done-sequence-issue-after-implementation.md`. После выполнения `machine/issue/backlog/` снова пуст.

## Актуальный sprint_21

## Цель sprint_21

Малый follow-up после DB metadata import: синхронизировать product/design scope, чтобы PostgreSQL metadata import from live connection был явно отделен от по-прежнему excluded SQL DDL text import/export.

## Задачи sprint_21

| Порядок | Issue | Status | Priority | Cost | Комментарий |
|---:|---|---|---:|---:|---|
| 1 | `061-sync-db-metadata-import-scope-with-requirements-docs.md` | done | mid | S | scope/docs sync выполнен; issue-файл содержит progress и пройденный `git diff --check` |
| 2 | `062-normalize-work-done-sequence-issue-after-implementation.md` | done | high | XS | cleanup выполнен: `021` перенесен из `work/` в `sprint_3/`, summaries получили dated correction |

## Что изменено

- `062` взята из backlog как high/XS и закрыта тем же проходом; это planning hygiene, без изменений product/code scope.
- Актуализирован `sprint_21/cost.md`: статус `061` синхронизирован с issue-файлом (`done` вместо устаревшего `planned`).
- Состав sprint_21 дополнен закрытым hygiene item `062`.
- Новый sprint не создавался: после выполнения доступный backlog пуст.

## Перемещено / оценено / приоритизировано

- Перемещено: `062` из `backlog/` в `sprint_21/`; `021` из `work/` в `sprint_3/`.
- Оценено/подтверждено:
  - `061-sync-db-metadata-import-scope-with-requirements-docs.md` — S, done.
- Приоритизация:
  1. `062` — high/XS/done; выбран как единственная доступная backlog-задача и scheduler hygiene.
  2. `061` — mid/S/done; уже был закрыт ранее.

## L / вопросы

L-задач в доступном backlog и актуальном sprint_21 не найдено; `062` была XS и уже закрыта; уточняющие вопросы не требуются.

## XL-декомпозиция

XL-задач в доступном backlog не найдено; декомпозиция не выполнялась.

## Блокеры / неясности

- Структура ясна: актуальный sprint — `sprint_21/`, доступный backlog — `machine/issue/backlog/`.
- Блокеров по структуре нет.
- Планировочный проход production code не менял; тесты не запускались.
