# Sprint 29 plan / hourly backlog analysis

Актуализировано: 2026-05-19 16:03 Europe/Moscow.

## Найдено в `machine/issue/backlog`

На входе найден 1 backlog item:

| Порядок | Issue | Status | Priority | Cost | Комментарий |
|---:|---|---|---:|---:|---|
| 1 | `067-sync-db-import-composite-key-policy-docs.md` | backlog → planned | mid → high | S | docs/scope sync после закрытых `059` и `066`: зафиксировать composite PK supported и composite FK skipped-with-warning MVP policy. |

После анализа `machine/issue/backlog/` пуст.

## Актуализация текущего sprint

- Последний завершенный sprint: `sprint_28`.
- `066-import-composite-primary-key-metadata.md` остается `done` / `mid` / `M`; targeted core tests по заметке sprint_28 пройдены.
- Так как `066` закрыт, зависимость для docs-sync задачи `067` снята.

## Новый active sprint_29

Цель sprint_29: убрать drift между фактическим поведением DB metadata import и requirements/design docs по composite keys.

| Порядок | Issue | Status | Priority | Cost | Комментарий |
|---:|---|---|---:|---:|---|
| 1 | `067-sync-db-import-composite-key-policy-docs.md` | done | high | S | Documentation-only slice closed: docs now distinguish single-column FK import, composite PK support, and composite FK skipped-with-warning MVP policy. |

## Что изменено

- Создан `machine/issue/sprint_29/`.
- `067-sync-db-import-composite-key-policy-docs.md` перенесена из `backlog/` в `sprint_29/`.
- В `067` изменены metadata: `status - backlog` → `status - planned`, `priority - mid` → `priority - high`; `cost - S` сохранен.
- В `067` добавлена progress note с причиной переноса/повышения приоритета.

## Перемещено / оценено / приоритизировано

- Перемещено:
  - `backlog/067-sync-db-import-composite-key-policy-docs.md` → `sprint_29/067-sync-db-import-composite-key-policy-docs.md`.
- Оценено/подтверждено:
  - `067` — S/high/planned: scope ограничен синхронизацией docs/requirements после уже закрытых implementation slices.
- Приоритизация ближайшего пула:
  1. `sprint_29/067` — high/S/planned: закрывает documentation drift по composite key import policy.
  2. Ранее закрытые `059`/`066` остаются source-of-truth для фактического поведения: composite FK skipped with warning; composite PK supported via table-level constraint.

## L / вопросы

L-задач в доступном backlog и новом sprint_29 нет; уточняющие вопросы не требуются.

## XL-декомпозиция

XL-задач в доступном backlog нет; декомпозиция не выполнялась.

## Блокеры / неясности

- Структура backlog ясна: `machine/issue/backlog/` содержит только доступные backlog-задачи; новый planning sprint — `machine/issue/sprint_29/`.
- Production/docs файлы вне `machine/issue` не менялись; это planning-only проход, поэтому Maven/tests не запускались.
- В git уже есть широкий незакоммиченный фон по issue-tree из предыдущих проходов; этот проход ограничен переносом `067` и созданием `sprint_29/cost.md`.

## scheduler-2 execution — 2026-05-19 16:30 Europe/Moscow

- Взята задача: `067-sync-db-import-composite-key-policy-docs.md` — high/S, единственная доступная small/medium planned задача после опустошения `backlog/`.
- Выполнено: синхронизированы requirements/design/machine notes по границе DB import: single-column FK импортируется; composite PK поддержан как table-level constraint; composite FK пропускается с warning в MVP, без promise grouped FK model.
- Проверки: JSON validation OK для `machine/requirements.json` и `machine/design.json`; `git diff --check` OK.
- Статус: done.


## Hourly backlog analysis — scheduler-1, 2026-05-19 17:03 Europe/Moscow

### Найдено в `machine/issue/backlog`

Backlog пуст: файлов задач в `machine/issue/backlog/` не найдено.

### Актуализация текущего sprint

- Последний активный/закрытый sprint: `sprint_29`.
- `067-sync-db-import-composite-key-policy-docs.md` подтвержден как `done` / `high` / `S`; scheduler-2 уже закрыл docs-sync и проверил JSON + `git diff --check`.
- Новых backlog-задач для `sprint_30` нет, поэтому новый sprint не создавался.

### Оценка / приоритезация / перенос

- Оценено новых backlog-задач: 0.
- Приоритизировано новых backlog-задач: 0.
- Перенесено в новый sprint: 0.
- Ближайший открытый пул вне пустого backlog без перемещения:
  1. `sprint_4/025-db-composite-constraints-and-relation-semantics.md` — high/M/in_progress: главный semantic follow-up вокруг composite constraints.
  2. `sprint_3/022-sequence-keyboard-creation-and-inspector-editing.md` — high/M/work: важный sequence MVP track.
  3. `sprint_2/015-non-modal-keyboard-flows-for-frequent-actions.md` — high/M/work: keyboard-first UX debt.
  4. `sprint_2/016-harden-json-and-svg-string-escaping.md` — mid/S/in_progress: небольшой hardening slice.
  5. `sprint_3/019-wire-or-hide-declared-space-navigation-layout-commands.md` — mid/S/planned: command/runtime consistency.

### L / вопросы

L-задач в доступном backlog нет; уточняющие вопросы не требуются.

### XL-декомпозиция

XL-задач в доступном backlog нет; декомпозиция не выполнялась.

### Блокеры / неясности

- Структура backlog ясна: `machine/issue/backlog/` пуст, а `sprint_N/` хранит planning/history и часть active statuses.
- Есть широкий незакоммиченный фон в `machine/issue` от предыдущих проходов; этот hourly pass добавил только эту заметку в `sprint_29/cost.md`.

## Hourly backlog analysis — scheduler-1, 2026-05-19 18:03 Europe/Moscow

### Найдено в `machine/issue/backlog`

Backlog пуст: файлов задач в `machine/issue/backlog/` не найдено.

### Актуализация текущего sprint

- Последний planning/current sprint: `sprint_29`.
- `067-sync-db-import-composite-key-policy-docs.md` подтвержден как `done` / `high` / `S`; новых изменений по задаче нет.
- Новых backlog-задач для `sprint_30` нет, поэтому новый sprint не создавался.
- Ближайший открытый пул остается в старых sprint-директориях, не в backlog: `025` high/M/in_progress, `022` high/M/work, `015` high/M/work, `016` mid/S/in_progress, `019` mid/S/planned.

### Оценка / приоритезация / перенос

- Оценено новых backlog-задач: 0.
- Приоритизировано новых backlog-задач: 0.
- Перенесено в новый sprint: 0.
- Приоритет ближайшего открытого пула без перемещения:
  1. `sprint_4/025-db-composite-constraints-and-relation-semantics.md` — high/M/in_progress: базовая semantic/model задача для composite constraints.
  2. `sprint_3/022-sequence-keyboard-creation-and-inspector-editing.md` — high/M/work: важный sequence MVP keyboard/inspector track.
  3. `sprint_2/015-non-modal-keyboard-flows-for-frequent-actions.md` — high/M/work: keyboard-first UX debt.
  4. `sprint_2/016-harden-json-and-svg-string-escaping.md` — mid/S/in_progress: небольшой hardening slice.
  5. `sprint_3/019-wire-or-hide-declared-space-navigation-layout-commands.md` — mid/S/planned: command/runtime consistency.

### L / вопросы

L-задач в доступном backlog нет; уточняющие вопросы не требуются.

### XL-декомпозиция

XL-задач в доступном backlog нет; декомпозиция не выполнялась.

### Блокеры / неясности

- Структура backlog ясна: `machine/issue/backlog/` пуст; `sprint_N/` хранит planning/history и часть открытых задач.
- Есть широкий незакоммиченный фон в рабочем дереве; этот hourly pass изменил только `machine/issue/sprint_29/cost.md`.

## Hourly backlog analysis — scheduler-1, 2026-05-19 19:05 Europe/Moscow

### Найдено в `machine/issue/backlog`

Backlog пуст: файлов задач в `machine/issue/backlog/` не найдено.

### Актуализация текущего sprint

- Текущий planning/current sprint: `sprint_29`.
- `067-sync-db-import-composite-key-policy-docs.md` подтвержден как `done` / `high` / `S`; новых изменений по задаче нет.
- Новых backlog-задач для `sprint_30` нет, поэтому новый sprint не создавался.

### Оценка / приоритезация / перенос

- Оценено новых backlog-задач: 0.
- Приоритизировано новых backlog-задач: 0.
- Перенесено в новый sprint: 0.
- Ближайший открытый пул вне пустого backlog без перемещения:
  1. `sprint_4/025-db-composite-constraints-and-relation-semantics.md` — high/M/in_progress: базовая semantic/model задача для composite constraints.
  2. `sprint_3/022-sequence-keyboard-creation-and-inspector-editing.md` — high/M/work: sequence MVP keyboard/inspector track.
  3. `sprint_2/015-non-modal-keyboard-flows-for-frequent-actions.md` — high/M/work: keyboard-first UX debt.
  4. `sprint_2/016-harden-json-and-svg-string-escaping.md` — mid/S/in_progress: небольшой hardening slice.
  5. `sprint_3/019-wire-or-hide-declared-space-navigation-layout-commands.md` — mid/S/planned: command/runtime consistency.

### L / вопросы

L-задач в доступном backlog нет; уточняющие вопросы не требуются.

### XL-декомпозиция

XL-задач в доступном backlog нет; декомпозиция не выполнялась.

### Блокеры / неясности

- Структура backlog ясна: `machine/issue/backlog/` пуст; `sprint_N/` хранит planning/history и часть открытых задач.
- В git есть существующий незакоммиченный фон по удаленным из backlog/перенесенным issue-файлам; этот hourly pass изменил только `machine/issue/sprint_29/cost.md`.
