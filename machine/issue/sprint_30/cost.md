# Sprint 30 plan / hourly backlog analysis

Актуализировано: 2026-05-19 20:05 Europe/Moscow.

## Найдено в `machine/issue/backlog`

На входе найден 1 backlog item:

| Порядок | Issue | Status | Priority | Cost | Комментарий |
|---:|---|---|---:|---:|---|
| 1 | `069-normalize-done-fk-preview-issue-out-of-work.md` | backlog → done | high | XS | Hygiene cleanup: убрать закрытую `068` из active staging `work/` согласно consistency policy. |

После анализа `machine/issue/backlog/` пуст.

## Актуализация текущего sprint

- Предыдущий current/planning sprint: `sprint_29`; `067-sync-db-import-composite-key-policy-docs.md` остается `done` / `high` / `S`.
- Обнаружен post-068 drift: `machine/issue/work/068-surface-derived-db-relation-meaning-in-fk-preview.md` имел `status - done`, что противоречило `machine/issue/consistency-checklist.md`.
- Создан новый history/planning sprint: `sprint_30`.

## Новый active/history sprint_30

Цель sprint_30: нормализовать issue-tree после закрытия FK preview follow-up.

| Порядок | Issue | Status | Priority | Cost | Комментарий |
|---:|---|---|---:|---:|---|
| 1 | `068-surface-derived-db-relation-meaning-in-fk-preview.md` | done | mid | S | Перенесена из `work/` в canonical history location без изменения result/check metadata. |
| 2 | `069-normalize-done-fk-preview-issue-out-of-work.md` | done | high | XS | Перенесена из `backlog/` и закрыта как выполненная cleanup-задача. |

## Что изменено

- Создан `machine/issue/sprint_30/`.
- Перемещено: `work/068-surface-derived-db-relation-meaning-in-fk-preview.md` → `sprint_30/068-surface-derived-db-relation-meaning-in-fk-preview.md`.
- Перемещено: `backlog/069-normalize-done-fk-preview-issue-out-of-work.md` → `sprint_30/069-normalize-done-fk-preview-issue-out-of-work.md`.
- В `069` изменено `status - backlog` → `status - done`, добавлены `result`, `check` и progress note.
- Product/code scope не менялся.

## Перемещено / оценено / приоритизировано

- Перемещено в новый sprint: `068` (done/S/mid), `069` (done/XS/high).
- Оценка подтверждена: `069` — XS/high, так как это чистый file-tree hygiene без code scope.
- Приоритет ближайшего пула:
  1. `sprint_30/069` — high/XS/done: consistency policy восстановлена.
  2. `sprint_30/068` — mid/S/done: FK preview follow-up заархивирован в history sprint.
  3. Открытый пул вне backlog без перемещения: `sprint_4/025` high/M/in_progress, `sprint_3/022` high/M/work, `sprint_2/015` high/M/work, `sprint_2/016` mid/S/in_progress, `sprint_3/019` mid/S/planned.

## L / вопросы

L-задач в доступном backlog и новом sprint_30 нет; уточняющие вопросы не требуются.

## XL-декомпозиция

XL-задач в доступном backlog нет; декомпозиция не выполнялась.

## Блокеры / неясности

- Структура backlog ясна: `backlog/` содержит только доступные backlog-задачи; `work/` — только active staging; `sprint_N/` — planning/history.
- Блокеров нет.
- Проверка hygiene выполнена: `grep -R "^status - done" -n machine/issue/work machine/issue/backlog` не нашла done-файлов.

## Update 2026-05-19 21:04 MSK — scheduler-1

- `machine/issue/backlog/` повторно проверен: файлов нет, backlog count = 0.
- `machine/issue/work/` пуст; `grep -R "^status - done" machine/issue/work machine/issue/backlog` не нашел done-задач в active/backlog staging.
- Новых задач для переноса в sprint нет; `sprint_30` остается history/cleanup sprint без расширения.
- Открытый пул вне backlog, без перемещения: `sprint_2/015` high/M/work, `sprint_3/022` high/M/work, `sprint_1/011` mid/M/in_progress, `sprint_2/016` mid/S/in_progress, `sprint_2/017` mid/M/in_progress, `sprint_13/052` mid/M/planned.
- Приоритизация ближайшего пула: 1) `015`, 2) `022`, 3) `016`, 4) `017`/`011`, 5) `052`.
- L/XL в доступном backlog не обнаружены; вопросов и декомпозиции нет.

## Update 2026-05-19 22:00 MSK — scheduler-2

- `machine/issue/backlog/` проверен: файлов нет, поэтому задача взята из ближайшего активного open pool по текущей policy.
- Взята: `sprint_2/015-non-modal-keyboard-flows-for-frequent-actions.md` — high/M/work; выбрана выше mid/S задач из-за приоритета high и потому что это уже активный keyboard-first UX track.
- Выполнено: `Rename selected` заменен с blocking `TextInputDialog.showAndWait()` на non-blocking canvas overlay с Enter/Esc, undoable `RenameTableCommand` и возвратом focus на canvas.
- Проверки: `mvn -q -pl client -am -Dtest=CommandRouterTest -Dsurefire.failIfNoSpecifiedTests=false test`; `git diff --check -- client/src/main/java/see/schemeonyou/SchemeOnYouApplication.java` — passed.
- Статус: `015` остается `work`; command palette/find/rename slices закрыты, delete selected и join-table confirmation еще modal.
- Блокеры: backlog пуст; структурных блокеров нет, есть широкий незакоммиченный фон в дереве, изменения этого прохода ограничены `SchemeOnYouApplication.java` и issue notes.

## Update 2026-05-19 22:05 MSK — scheduler-1 hourly backlog analysis

- `machine/issue/backlog/` проверен: файлов нет, backlog count = 0.
- `machine/issue/work/` проверен: файлов нет; `grep -R "^status - done" machine/issue/work machine/issue/backlog` не находит done-задач в active/backlog staging.
- Новых задач для переноса в новый sprint нет; `sprint_30` остается последним history/cleanup sprint, новый `sprint_31` не создавался из-за пустого backlog.
- Актуальный open pool вне backlog: `sprint_2/015` high/M/work; `sprint_1/011` mid/M/in_progress; `sprint_2/017` mid/M/in_progress; `sprint_13/052` mid/M/planned.
- Приоритизация ближайшего пула: 1) `015` — high/M/work, продолжать non-modal UX slices; 2) `017` — mid/M/in_progress, закрыть release verification parity; 3) `011` — mid/M/in_progress, packaging/release launchers; 4) `052` — mid/M/planned, refactor UI area initialization после стабилизации active UX/release tracks.
- Оценки подтверждены: все открытые задачи M; L/XL в доступном backlog и open pool не обнаружены.
- L-вопросы: не требуются.
- XL-декомпозиция: не выполнялась.
- Блокеры: backlog пуст; структурных блокеров нет. Есть широкий незакоммиченный фон в repo, поэтому этот проход ограничен issue-note актуализацией без code/file moves.

## Update 2026-05-19 22:30 MSK — scheduler-2

- `machine/issue/backlog/` проверен: файлов нет, поэтому продолжена ближайшая активная open pool задача.
- Взята: `sprint_2/015-non-modal-keyboard-flows-for-frequent-actions.md` — high/M/work; выбрана как самый приоритетный подходящий M-track, уже начатый в keyboard-first UX.
- Выполнено: `Delete selected` заменен с blocking `Alert.showAndWait()` на non-blocking canvas confirmation overlay с preview/context line, Enter confirm, Esc/Cancel cancel и focus restore.
- Проверки: `mvn -q -pl client -am -Dtest=CommandRouterTest -Dsurefire.failIfNoSpecifiedTests=false test`; `git diff --check -- client/src/main/java/see/schemeonyou/SchemeOnYouApplication.java` — passed.
- Статус: `015` остается `work`; command palette/find/rename/delete slices закрыты, join-table confirmation еще modal.
- Блокеры: backlog пуст; структурных блокеров нет. В repo остается широкий незакоммиченный фон, изменения этого прохода ограничены `SchemeOnYouApplication.java` и issue notes.

## Update 2026-05-19 23:00 MSK — scheduler-3 backlog refinement

### Проанализировано

- `machine/issue/backlog/`: пуст, backlog count = 0.
- `machine/issue/work/`: пуст; done-задач в `work/`/`backlog/` не найдено.
- Открытые issue по `status != done`: `sprint_2/015` high/M/work, `sprint_1/011` mid/M/in_progress, `sprint_2/017` mid/M/in_progress, `sprint_13/052` mid/M/planned.
- Requirements/design/machine docs: DB import scope, composite PK/FK policy, keyboard-first MVP, release verification notes.
- Runtime hotspots: `showAndWait`/modal flows, command/Space/help registration, sequence add/edit coverage, DB import warnings/lifecycle, large `SchemeOnYouApplication` extraction seam.

### Новые задачи / обновления

- Новых backlog-задач не создано: найденные gaps уже покрыты существующими canonical issue.
- Файлы issue не перемещались и metadata не менялась; обновлен только этот planning summary.

### Оценки / приоритеты

1. `sprint_2/015-non-modal-keyboard-flows-for-frequent-actions.md` — high/M/work: главный ближайший gap; после command palette/find/rename/delete остается join-table preview/confirmation modal slice.
2. `sprint_2/017-release-readiness-environment-and-ci-parity.md` — mid/M/in_progress: закрыть release verification parity и согласовать gate notes с текущим `machine/release-verification.md`/`sprint_14/010`.
3. `sprint_1/011-packaging-and-cross-platform-launch.md` — mid/M/in_progress: довести portable launch/release README и smoke для scripts.
4. `sprint_13/052-extract-functional-area-initialization-classes.md` — mid/M/planned: `SchemeOnYouApplication` остается крупным (~2640 строк) и содержит UI-area wiring; refactor нужен, но ниже active UX/release tracks.

### L / XL / вопросы

- L и XL задач в доступном backlog/open pool не обнаружено; декомпозиция не выполнялась.
- Уточняющих вопросов по L нет.

### Блокеры

- Backlog пуст; структурных блокеров нет.
- В repo остается широкий незакоммиченный фон из предыдущих проходов, поэтому refinement не менял production code.

## Update 2026-05-19 23:03 MSK — scheduler-1 hourly backlog analysis

- `machine/issue/backlog/` проверен: файлов нет, backlog count = 0.
- `machine/issue/work/` проверен: файлов нет; done-задач в `work/`/`backlog/` не найдено.
- Текущий planning/history sprint: `sprint_30`; новых задач для переноса в новый sprint нет, `sprint_31` не создавался из-за пустого backlog.
- Актуальный open pool вне backlog:
  1. `sprint_2/015-non-modal-keyboard-flows-for-frequent-actions.md` — high/M/work: продолжать non-modal keyboard-first UX; ближайший остаток — join-table preview/confirmation modal slice.
  2. `sprint_2/017-release-readiness-environment-and-ci-parity.md` — mid/M/in_progress: release verification parity и gate notes.
  3. `sprint_1/011-packaging-and-cross-platform-launch.md` — mid/M/in_progress: portable launch/release README и packaging smoke.
  4. `sprint_13/052-extract-functional-area-initialization-classes.md` — mid/M/planned: refactor крупного `SchemeOnYouApplication`, ниже активных UX/release tracks.
- Оценки подтверждены: все открытые задачи M; L/XL в доступном backlog и open pool не обнаружены.
- L-вопросы: не требуются.
- XL-декомпозиция: не выполнялась.
- Изменено: обновлен только `sprint_30/cost.md`; issue-файлы, product code и перемещения не трогались.
- Блокеры: backlog пуст; структурных блокеров нет. В repo сохраняется широкий незакоммиченный фон, поэтому проход ограничен planning note.

## Update 2026-05-20 09:21 MSK — scheduler-1 hourly backlog analysis

- `machine/issue/backlog/` проверен: файлов нет, backlog count = 0.
- `machine/issue/work/` проверен: файлов нет; done-задач в `work/`/`backlog/` не обнаружено.
- Текущий planning/history sprint остается `sprint_30`; новый sprint не создавался, потому что доступный backlog пуст и переносить нечего.
- Актуализация open pool вне backlog:
  1. `sprint_2/017-release-readiness-environment-and-ci-parity.md` — mid/M/in_progress: ближайший release-parity track; синхронизировать verification contract, gate notes и ссылки из активных release/test issue.
  2. `sprint_1/011-packaging-and-cross-platform-launch.md` — mid/M/in_progress: packaging/portable launch продолжать после/параллельно release verification contract.
  3. `sprint_13/052-extract-functional-area-initialization-classes.md` — mid/M/planned: UI refactor ниже release tracks; брать после стабилизации gate/packaging.
- Важное изменение состояния: `sprint_2/015-non-modal-keyboard-flows-for-frequent-actions.md` уже `done`/high/M; он больше не входит в open pool.
- Оценки подтверждены: все открытые задачи M; L/XL в доступном backlog и open pool не найдено.
- Перемещено в новый sprint: ничего.
- L-вопросы: не требуются.
- XL-декомпозиция: не выполнялась.
- Изменено этим проходом: обновлен только `sprint_30/cost.md`; issue-файлы, product code и перемещения не трогались.
- Блокеры: backlog пуст; структурных блокеров нет. В repo сохраняется широкий незакоммиченный фон, поэтому проход ограничен planning note.
