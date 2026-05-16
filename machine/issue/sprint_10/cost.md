# Sprint 10 plan / hourly backlog analysis

Актуализировано: 2026-05-17 00:03 Europe/Moscow.

## Найдено в `machine/issue/backlog`

| Issue | Status | Priority | Cost | Решение |
|---|---|---:|---:|---|
| `043-prune-unused-focus-traversal-cycle.md` | backlog | mid | S | перенести в новый `sprint_10`; безопасный cleanup устаревшей cyclic focus traversal model после закрытия sprint_9 |

Активных XL-задач в backlog не найдено.

## Актуализация текущего `sprint_9`

`sprint_9/` содержит только `cost.md`; ранее запланированные/взятые задачи `041`, `042`, `024`, `033` уже находятся в `work/` со статусом `done`. Активных planned/work задач в `sprint_9/` не осталось, поэтому sprint_9 считается закрытым для планирования.

## Состав нового `sprint_10`

| Порядок | Issue | Status | Priority | Cost | Комментарий |
|---:|---|---|---:|---:|---|
| 1 | `043-prune-unused-focus-traversal-cycle.md` | planned | mid | S | удалить или переписать неиспользуемый `FocusTraversal` под direct-area model; не трогать используемые `FocusArea` / `FocusAreaNavigator` |

## Что изменено

- Создан `machine/issue/sprint_10/`.
- `043-prune-unused-focus-traversal-cycle.md` перенесена из `machine/issue/backlog/` в `machine/issue/sprint_10/`.
- В `043` статус изменен `backlog` → `planned`; добавлена progress-запись о переносе.
- `machine/issue/backlog/` после переноса пуст.

## Перемещено / оценено / приоритизировано

- Перемещено: `043` → `sprint_10/`.
- Оценка сохранена: `S`.
- Приоритет сохранен: `mid`.
- Приоритизация sprint_10: сначала `043`, потому что это единственная backlog-задача и она маленькая/безопасная; выше нее по продуктовой важности остаются active high/M release gaps в `work/` и planned high/M semantics tasks, но они уже не являются backlog-кандидатами.

## L / вопросы

Новых L-задач в `machine/issue/backlog` нет.

Существующие L вне backlog:
- `work/010-tests-and-release-gates.md` — L/in_progress; уточнения уже зафиксированы: локальный Maven gate `compile + tests + package`, GitHub Actions/manual checklist не обязательны.
- `work/007-sequence-diagram-mvp-ui.md` — L/done; дополнительных вопросов нет.
- `sprint_8/026-extract-application-presenters-for-headless-ui-tests.md` — L/done; parent закрыт решением SEE, вопросов нет.

## XL-декомпозиция

XL-задач не найдено; декомпозиция не выполнялась.

## Блокеры / неясности

- Структура backlog понятна.
- Блокеров по планированию нет.


## Update 2026-05-17 00:30 MSK — scheduler-2

- `043-prune-unused-focus-traversal-cycle.md` moved from `sprint_10/` to `work/` and completed.
- Removed unused `client/src/main/java/see/schemeonyou/ui/FocusTraversal.java`; active focus flow remains `FocusArea` / `FocusAreaNavigator` plus direct `0`/`1`/`2`/`3` switching.
- Verification: `mvn -q -pl client -am test` pass with project-local JDK/Maven; `git diff --check` pass.
- `machine/issue/backlog/` remains empty.
