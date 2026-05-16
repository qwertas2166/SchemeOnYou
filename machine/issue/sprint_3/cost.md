# Sprint 3 plan / status

Актуализировано: 2026-05-15 16:05 Europe/Moscow.

## Состав sprint_3

| Issue | Status | Priority | Cost | Комментарий |
|---|---|---:|---:|---|
| `020-integrate-delete-controller-for-column-and-fk-selection.md` | in_progress (`work/`) | high | M | В работе: delete controller подключен к table/column/FK flow; проверка Maven заблокирована окружением. |
| `021-sequence-domain-types-storage-validation.md` | planned | high | M | Базовый contract для sequence model/storage/validation; должен идти перед richer editing. |
| `022-sequence-keyboard-creation-and-inspector-editing.md` | planned | high | M | Sequence add/edit/select flows; согласовать с `021`. |
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
