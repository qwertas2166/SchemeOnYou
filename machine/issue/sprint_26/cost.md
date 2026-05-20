# Sprint 26 plan / hourly backlog analysis

Актуализировано: 2026-05-19 13:05 Europe/Moscow.

## Найдено в `machine/issue/backlog`

`machine/issue/backlog/` пуст: файлов задач не найдено.

`machine/issue/work/` тоже пуст. По текущей policy из `consistency-checklist.md`, `work/` является optional staging, а active/planned задачи могут жить в `sprint_N/`, если `cost.md` явно фиксирует состояние.

## Актуализация текущего sprint

`sprint_25` закрыт: `064-reconcile-active-issue-paths-and-dangling-references.md` остается `done` / mid / S. Новых незакрытых задач в `sprint_25` нет.

## Новый active sprint_26

Цель sprint_26: закрыть ближайший DB-import semantic correctness follow-up после закрытых DB import hardening/hygiene slices.

| Порядок | Issue | Status | Priority | Cost | Комментарий |
|---:|---|---|---:|---:|---|
| 1 | `059-db-import-composite-foreign-key-policy.md` | done | mid | M | закрыт MVP degradation path: composite FK groups skipped with warning; single-column FK import preserved |

## Что изменено

- Создан новый `machine/issue/sprint_26/`.
- `059-db-import-composite-foreign-key-policy.md` перенесена из `sprint_24/` в `sprint_26/`.
- `sprint_24/cost.md` получил dated correction о переносе.
- `sprint_25/cost.md` получил hourly rollover note.
- Оценки и приоритеты issue-файлов не менялись.

## Перемещено / оценено / приоритизировано

- Перемещено:
  - `sprint_24/059-db-import-composite-foreign-key-policy.md` → `sprint_26/059-db-import-composite-foreign-key-policy.md`.
- Оценено/подтверждено:
  - `059` — M / mid / planned: M сохраняется только при MVP degradation/detection path; full grouped FK support нужно выделять отдельно, если scope начнет расти.
  - `015` — M / high / work: важный UX debt, но уже active work-track; не переносился, чтобы не ломать историю.
  - `022` — M / high / work: важный sequence MVP track; не переносился по той же причине.
  - `025` — M / high / planned: связан с composite semantics, но шире DB-import policy и ниже для текущего DB-import continuity.
- Приоритизация ближайшего пула:
  1. `sprint_26/059` — mid/M/planned: следующий executable DB-import follow-up.
  2. `sprint_3/022` — high/M/work: sequence keyboard/editing MVP, продолжать отдельным work-track.
  3. `sprint_2/015` — high/M/work: non-modal keyboard flows, продолжать отдельным work-track.
  4. `sprint_4/025` — high/M/planned: composite constraints semantics после/в связке с решением `059`.
  5. `sprint_13/026-04`, `sprint_13/052`, `sprint_3/019`, `sprint_12/047` — M/S follow-ups ниже текущей DB-import continuity.

## L / вопросы

L-задач в `backlog/`, `work/` и ближайшем open/planned пуле не найдено; уточняющие вопросы не требуются.

## XL-декомпозиция

XL-задач в `backlog/`, `work/` и ближайшем open/planned пуле не найдено; декомпозиция не выполнялась.

## Блокеры / неясности

- Структура ясна: доступный backlog — `machine/issue/backlog/` пуст; новый active sprint — `machine/issue/sprint_26/`.
- В дереве git уже есть много незакоммиченных изменений/перемещений из предыдущих работ; этот проход изменил только issue planning structure (`sprint_26`, перенос `059`, corrections в `sprint_24`/`sprint_25`).
- Production code не менялся; тесты не запускались, потому что это planning-only проход.


## Scheduler-2 execution — 2026-05-19 13:30 Europe/Moscow

- `machine/issue/backlog/` пуст, поэтому взята active planned задача из `sprint_26`: `059-db-import-composite-foreign-key-policy.md` (`mid`/`M`).
- Policy decision: для MVP выбран degradation path — composite FK groups детектируются и пропускаются с import warning; single-column FK импортируется как раньше.
- Реализовано: grouping по FK name/source table/target table с ordering по `KEY_SEQ`, `DbImportResult.importWarnings()`, отображение первого warning в DB import dialog status.
- Проверки: `mvn -q -pl core test`, `mvn -q -pl client -am test`, targeted trailing-whitespace check — pass.
- Блокеров нет; full grouped-FK model оставлен вне scope этой M-задачи.
