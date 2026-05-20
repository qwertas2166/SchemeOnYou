# Sprint 3 plan / status

Актуализировано: 2026-05-15 16:05 Europe/Moscow.

## Состав sprint_3

| Issue | Status | Priority | Cost | Комментарий |
|---|---|---:|---:|---|
| `020-integrate-delete-controller-for-column-and-fk-selection.md` | in_progress (`work/`) | high | M | В работе: delete controller подключен к table/column/FK flow; проверка Maven заблокирована окружением. |
| `021-sequence-domain-types-storage-validation.md` | done (`sprint_3/`) | high | M | Выполнен 2026-05-18; 2026-05-19 перенесен из `work/` в sprint history. |
| `022-sequence-keyboard-creation-and-inspector-editing.md` | done (`sprint_3/`) | high | M | Sequence add/edit/select flows закрыты; final endpoint editing slice выполнен 2026-05-19. |
| `019-wire-or-hide-declared-space-navigation-layout-commands.md` | planned | mid | S | Быстрый consistency-fix declared-vs-runtime Space commands. |
| `023-mvp-scale-performance-smoke.md` | planned | mid | M | Нефункциональный MVP gate; лучше после стабилизации save/load/export. |

## Актуальная приоритезация sprint_3

1. `020` — high/M, уже in_progress: довести проверку/закрытие delete для column/FK.
2. `021` — high/M: foundation для sequence storage/validation.
3. `022` — high/M: sequence keyboard editing, после/параллельно `021` малыми срезами.
4. `019` — mid/S: короткий shortcut consistency fix, можно брать filler-задачей.
5. `023` — mid/M: scale smoke, после стабилизации save/load/export и gate contract.

## Что изменилось за проход

- `020` актуализирована как `work/in_progress`, не как просто planned в sprint folder.
- Новый backlog разобран отдельно: `025` и `027` вынесены в новый `sprint_4/`, `026` оставлена в backlog как L до уточнения.

## L/XL

В активных задачах `sprint_3/` L и XL не найдено.

## Заметки по зависимостям

- `022` зависит от/должна быть согласована с `021`.
- `020` финально согласовать с `sprint_2/015` по non-modal confirmation.
- `023` согласовать с `work/010` и `sprint_2/017`, чтобы smoke не дублировал release gate.
## Коррекция 2026-05-19 10:00 MSK

- `021-sequence-domain-types-storage-validation.md` уже закрыт (`status - done`) и перенесен из `machine/issue/work/` в `machine/issue/sprint_3/`; product/code scope не менялся.

## Update 2026-05-19 20:30 MSK — scheduler-2

- `machine/issue/backlog/` пуст; взята ближайшая подходящая planned mid/S задача из sprint scope: `019-wire-or-hide-declared-space-navigation-layout-commands.md`.
- Статус `019` установлен в `done`.
- Закрыт drift Space commands: active hints/ShortcutMap/Space sheet больше не рекламируют unwired `G T`/`L S`; `Space E` синхронизирован как edit selected; `Backspace` в Space command mode откатывает последний chord token.
- Проверки: `mvn -q -pl client -am -Dtest=CommandRouterTest -Dsurefire.failIfNoSpecifiedTests=false test`; `git diff --check`.

## Update 2026-05-19 21:30 MSK — scheduler-2

- `machine/issue/backlog/` пуст; продолжена ближайшая high/M active sprint-задача: `022-sequence-keyboard-creation-and-inspector-editing.md`.
- Закрыт оставшийся acceptance gap: inspector `From`/`To` для sequence message теперь keyboard-editable и undoable, participant выбирается по id или name.
- Статус `022` установлен в `done`.
- Проверки: `mvn -q -pl client -am -Dtest=InspectorPresenterTest,CommandRouterTest,SequenceCommandTest -Dsurefire.failIfNoSpecifiedTests=false test`; `git diff --check`.
