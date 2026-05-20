# Sprint 23 plan / hourly backlog analysis

Актуализировано: 2026-05-19 12:00 Europe/Moscow.

## Выбор из backlog

В `machine/issue/backlog/` найдена одна подходящая задача: `063-ignore-local-connection-profile-secrets-in-git.md` — high / XS. Она выбрана как минимальная и самая приоритетная доступная задача не больше M.

## Статус

| Порядок | Issue | Status | Priority | Cost | Комментарий |
|---:|---|---|---:|---:|---|
| 1 | `063-ignore-local-connection-profile-secrets-in-git.md` | done | high | XS | закрыт VCS/privacy hardening для local connection profile secrets |

## Проверки

- `git check-ignore --stdin -v` подтвердил ignore для root и nested `schemeonyou-connection-profiles.local` / `.tmp`.
- `git diff --check -- .gitignore machine/release-verification.md machine/issue/sprint_23/063-ignore-local-connection-profile-secrets-in-git.md` прошел без замечаний.

## Блокеры / неясности

Блокеров нет. Дерево уже содержит много незакоммиченных изменений из других работ; этот проход ограничен `.gitignore`, `machine/release-verification.md` и issue metadata.

## Scheduler-3 backlog refinement — 2026-05-19 12:02 Europe/Moscow

## Проанализировано

- `machine/issue/backlog/` и `machine/issue/work/`: оба каталога пусты.
- Открытые planned/work issue по текущему дереву: `015`, `016`, `017`, `019`, `022`, `025`, `026-04`, `047`, `052`, `059`; `060` и `063` подтверждены как done.
- Requirements/design/machine docs: PostgreSQL metadata import scope синхронизирован, открытых product-вопросов в `requirements/questions.md` нет.
- Runtime/code hotspots: DB import lifecycle, `showAndWait` paths, Space-command drift, composite FK import policy, functional-area/inspector refactor seams.

## Решение

Новых backlog-задач не создано: обнаруженные gaps уже покрыты существующими canonical issue и создание новых файлов дублировало бы scope.

## Актуальные приоритеты без нового backlog

1. `sprint_22/059-db-import-composite-foreign-key-policy.md` — mid / M / planned: следующий DB-import semantic correctness follow-up после закрытых `060` и `063`.
2. `sprint_3/022-sequence-keyboard-creation-and-inspector-editing.md` — high / M / work: sequence MVP keyboard/editing gap уже активен.
3. `sprint_2/015-non-modal-keyboard-flows-for-frequent-actions.md` — high / M / work: покрывает оставшиеся blocking editor-flow dialogs.
4. `sprint_13/026-04` и `sprint_13/052` — planned M refactor seams для inspector/editing и UI area initialization.
5. `sprint_3/019`, `sprint_4/025`, `sprint_12/047` — planned S/M consistency/semantics/UX follow-ups.

## L / XL / вопросы

- L-задач в доступном backlog/work/актуальном planned follow-up pool не найдено; новых уточняющих вопросов нет.
- XL-задач не найдено; декомпозиция не выполнялась.

## Блокеры

- Блокеров для backlog refinement нет.
- Дерево git содержит много незакоммиченных изменений из предыдущих работ; этот проход изменил только planning summary `machine/issue/sprint_23/cost.md`.
