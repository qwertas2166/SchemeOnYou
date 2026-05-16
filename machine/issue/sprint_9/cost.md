# Sprint 9 plan / hourly backlog analysis

Актуализировано: 2026-05-16 23:03 Europe/Moscow.

## Найдено в `machine/issue/backlog`

`machine/issue/backlog/` пуст: активных backlog-кандидатов для оценки и переноса нет.

Root `issue/backlog/` содержит только non-active mirror-файлы: `035`, `036`, `037`, `038`, `040`. Их canonical machine-задачи уже перенесены в `machine/issue/work/` и закрыты.

Активных XL-задач в backlog не найдено.

## Актуализация текущего `sprint_9`

| Issue | Location | Status | Priority | Cost | Комментарий |
|---|---|---|---:|---:|---|
| `041-prune-or-wire-unused-ui-shell-stubs.md` | `work/` | done | low | S | Закрыта: unused `AppShell`/`ProjectTreePanel` удалены, reference grep + Maven test + diff-check pass. |
| `042-normalize-machine-requirements-shortcut-labels.md` | `work/` | done | mid | XS | Закрыта: `Tab`/`Shift+Tab` labels в `machine/requirements.json` scoped как in-area traversal; JSON validation pass. |
| `024-dirty-state-current-file-and-unsaved-change-guard.md` | `work/` | done | high | M | Закрыта scheduler-2 в 22:00; client tests/package/diff-check pass. |

В `sprint_9/` активных planned/work задач не осталось.

## Что изменено

- Обновлен `machine/issue/sprint_9/cost.md` текущим hourly-анализом.
- Файлы задач не переносились и metadata задач не менялась: canonical backlog пуст.
- Новый sprint не создан: нет активных backlog-задач для планирования.

## Перемещено / оценено / приоритизировано

- Перемещено: ничего.
- Новых оценок нет: backlog пуст.
- Текущая приоритизация оставшейся активной работы без изменения файлов задач:
  1. High/M release gaps: `001-open-project-loading.md`, `028-expand-find-element-scope-and-go-linked-navigation.md`, `014-apply-modern-dark-ide-theme.md`.
  2. Planned high/M product semantics: `sprint_4/025-db-composite-constraints-and-relation-semantics.md`.
  3. High/M sequence foundation/editing: `sprint_3/021`, `sprint_3/022`.
  4. Mid/S/M consistency/release tasks: `sprint_2/015`, `sprint_2/016`, `sprint_2/017`, `sprint_3/019`, `sprint_3/023`, `sprint_6/033`, `work/013`, `work/026-04`.
  5. L umbrella/gate: `work/010-tests-and-release-gates.md` — держать in_progress и выполнять только через проверяемые M-or-less slices.

## L / вопросы

- Новых L-задач в `machine/issue/backlog` нет.
- `work/010-tests-and-release-gates.md` — L/in_progress; уточнения уже зафиксированы: локальный Maven gate `compile + tests + package`, GitHub Actions и manual checklist не обязательны. Новых вопросов нет.
- `work/007` и `sprint_8/026` — L/done; вопросов нет.

## XL-декомпозиция

XL-задач не найдено; декомпозиция не выполнялась.

## Блокеры / неясности

- Структура backlog понятна.
- Блокеров по планированию нет; canonical backlog пуст.

## Update 2026-05-16 23:34 MSK — scheduler-2

- `machine/issue/backlog/` пуст; взята ближайшая безопасная S-задача из planned sprint scope: `033-sync-human-requirements-shortcut-model.md` (`mid`/`S`).
- Перемещена из `sprint_6/` в `work/`, статус `done`.
- Обновлены `requirements/requirements.md`, `requirements/keyboard-only-analysis.md`, `requirements/README.md`: текущая навигация по крупным зонам зафиксирована как `0/1/2/3`, `Tab`/`Shift+Tab` оставлены только как scoped traversal внутри текущей области/поля/dialog/picker/sheet или legacy/refinement context.
- Проверки: grep по `requirements/` на `Tab`/`Shift+Tab`/`F6`/`focus area` показывает только допустимые scoped/legacy упоминания; `git diff --check` pass.
- Блокеров нет; код и scope/storage не менялись.

## Update 2026-05-17 00:03 MSK — hourly backlog analysis

- В `machine/issue/backlog/` найдена одна активная задача: `043-prune-unused-focus-traversal-cycle.md` (`mid`/`S`).
- `sprint_9/` активных planned/work задач не содержит; sprint_9 оставлен закрытым.
- Создан новый `sprint_10`; `043` перенесена туда со статусом `planned`.
- L: новых L в backlog нет; `010` остается L/in_progress с ранее уточненным gate, `007` и `026` done. XL не найдено.
