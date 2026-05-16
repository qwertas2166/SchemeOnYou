# Sprint 7 plan / backlog normalization update

Актуализировано: 2026-05-16 16:00 Europe/Moscow.

## Что сделано

- Взята high/S задача `039-normalize-root-issue-backlog-into-machine-tree.md` из `machine/issue/backlog/`.
- Задача перенесена в `machine/issue/work/` и закрыта как `done`.
- Root-only задачи `issue/backlog/035-038` скопированы в canonical backlog `machine/issue/backlog/`.
- Root-копии `issue/backlog/035-038` помечены как `status - mirrored` и содержат ссылку на canonical issue, чтобы не было двух активных копий.

## Актуальный canonical backlog после нормализации

| Issue | Status | Priority | Cost | Комментарий |
|---|---|---:|---:|---|
| `035-keyboard-tab-stays-inside-functional-area.md` | backlog | high | S | Новое UX/focus требование SEE; связано с `037`. |
| `037-focus-first-object-on-functional-area-switch.md` | backlog | high | S | Новое UX/focus требование SEE; зависит от модели областей/Tab. |
| `036-empty-background-and-padded-functional-containers.md` | backlog | mid | S | Layout/structure polish; связан с fieldset/padding. |
| `038-functional-area-fieldsets-with-names.md` | backlog | mid | S | Visual grouping polish; связан с `036` и focus model. |
| `026-extract-application-presenters-for-headless-ui-tests.md` | done | mid | L | Parent уже закрыт по решению SEE; остается в backlog как historical umbrella до отдельной archival cleanup. |

## Блокеры / риски

- Runtime code не менялся.
- Остался отдельный hygiene-риск: `026` лежит в `backlog/`, но имеет `status - done`; не трогал вне scope `039`.

## Update 2026-05-16 16:05 MSK — hourly backlog analysis

- Sprint 7 актуализирован как завершенный normalization-sprint: active/planned задач в `sprint_7/` нет, `039` уже закрыта в `work/`.
- Новые UX/focus задачи из canonical backlog перенесены не в sprint_7, а в новый `sprint_8`, чтобы не смешивать закрытую normalization-работу с новым UI-slice.
