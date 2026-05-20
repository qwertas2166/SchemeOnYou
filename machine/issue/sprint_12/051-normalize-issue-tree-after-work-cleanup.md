summary - Нормализовать issue tree после cleanup work/backlog

status - done
priority - high
cost - S

goal - Вернуть планировочные артефакты `machine/issue` в консистентное состояние после массового cleanup/move, чтобы scheduler не опирался на устаревшие статусы и sprint summaries.

description - После cleanup 2026-05-17 часть metadata снова разошлась с фактическим деревом. Изначально: `work/001` был `done`, но лежал в `work/`; `sprint_11/cost.md` утверждал, что backlog пуст, хотя в `machine/issue/backlog/` были активные файлы; там же упоминался отсутствующий `041`. После последующих проходов дрейф повторился: `machine/issue/work/014-apply-modern-dark-ide-theme.md`, `045-inspector-fields-view-edit-modes.md` и `053-layer-left-menu-and-inspector-above-canvas.md` имели `status - done`, но всё ещё лежали в `work/`, а `sprint_11/cost.md` всё ещё говорил, что `work/` содержит активные `014`/`045`. После Lombok-среза новый аналогичный дрейф: `machine/issue/work/050-refactor-manual-accessors-to-lombok.md` теперь `status - done`, но остаётся в `work/`. Историческая задача `031` уже закрыта и описывала предыдущий дрейф, поэтому этот малый hygiene-срез должен зафиксировать текущую политику и привести дерево/summary в консистентное состояние.

acceptance criteria -
- В `machine/issue/work/` остаются только реально активные задачи со статусом `work` или `in_progress`, либо явно выбранная политика для `done` в work документирована и применена единообразно.
- Done-задачи `001`, `014`, `045`, `053`, `050`, `055` перемещены из `work/` в подходящие sprint/history locations или статус/расположение приведены к выбранной политике без потери history/progress.
- `machine/issue/backlog/` содержит только файлы со `status - backlog`; список backlog в актуальных `cost.md`/planning summaries не противоречит фактическому дереву.
- `sprint_11/cost.md` обновлен: удалены/помечены устаревшие утверждения про пустой backlog и отсутствующую `041`, либо добавлена свежая секция с текущим состоянием.
- Добавлена короткая проверка/checklist для будущих cleanup-проходов: path/status/backlog summary consistency.
- Не меняется product/code scope и не переоцениваются задачи, кроме явного исправления metadata.

dependencies/risks -
- Related to historical `sprint_5/031-normalize-issue-status-and-sprint-indexes.md`, but not duplicate: `031` закрыта и покрывала старое состояние до cleanup 2026-05-17.
- Risk: можно случайно потерять progress/history при перемещении done-файлов; делать только file moves/status summaries, без переписывания смысла задач.

progress - 2026-05-17 02:04 MSK: moved from backlog to sprint_12 during hourly backlog analysis; priority set to high (scheduler hygiene; prevents stale planning decisions).
progress - 2026-05-17 12:32 MSK scheduler-3: актуализирован scope после нового дрейфа: `work/014`, `work/045` и `work/053` уже `done`, но остаются в `work/`; `sprint_11/cost.md` всё ещё называет `014`/`045` активными. Новую задачу не создавал: это прямое расширение существующего hygiene-среза `051`.
progress - 2026-05-17 13:02 MSK scheduler-3: повторно подтвержден drift: `work/050` теперь единственная фактическая активная задача, но `sprint_11/cost.md` всё ещё описывает активные `014`/`045`, а `sprint_12/cost.md` перечисляет `050` в backlog после переноса в work. Новую задачу не создавал: это остается scope `051`.
progress - 2026-05-17 14:01 MSK scheduler-3: после завершения Lombok-среза `work/050` стал `status - done`, но всё ещё лежит в `work/`; добавлено в acceptance/scope `051`, без создания дубликата.
progress - 2026-05-17 15:00 MSK scheduler-3: подтвержден новый planning drift после уточнения MVP gate: `sprint_13/cost.md` всё ещё описывает `010` как L/test-suite slicing, хотя `010` теперь scope-reduced до S release-gate metadata/docs normalization; это остается частью hygiene-среза `051`, новую задачу не создавал.
progress - 2026-05-17 16:03 MSK scheduler-1: `work/050-refactor-manual-accessors-to-lombok.md` имел `status - done`; перенесен в `sprint_12/050-refactor-manual-accessors-to-lombok.md`, чтобы `work/` снова содержал только активные задачи.

progress - 2026-05-17 22:02 MSK scheduler-3: подтвержден свежий drift: `machine/issue/work/055-scope-shortcut-help-to-active-diagram-context.md` имеет `status - done`, но лежит в `work/`. Новую задачу не создавал: это тот же path/status hygiene scope `051`.

progress - 2026-05-17 23:03 MSK scheduler-3: подтвержден новый planning metadata drift после выполнения `049-01`: `sprint_16/cost.md` всё ещё говорит, что в backlog найден `055`, хотя `backlog/` пуст и `055` уже перенесена в `sprint_17/`; `sprint_17/cost.md` в соседнем DB import разделе всё ещё показывает `049-01` как planned, хотя файл `sprint_16/049-01-local-connection-profiles-crud.md` уже `status - done`. Новую задачу не создавал: это тот же scope path/status/planning-summary consistency.
progress - 2026-05-18 09:37 MSK scheduler-3: после выполнения `049-02` найден свежий summary drift: `sprint_16/cost.md` и `sprint_17/cost.md` ещё показывали `049-02` как planned. Исправлено в existing cost summaries без новой backlog-задачи; это тот же hygiene-scope `051`.
progress - 2026-05-18 10:32 MSK scheduler-3: подтвержден свежий planning-summary drift после scheduler-2 release/build work и DB import updates: `sprint_2/cost.md` всё ещё показывает `015`/`016`/`017` как planned, хотя issue-файлы имеют `015` status work, `016`/`017` in_progress; `sprint_17/cost.md` верхняя таблица всё ещё показывает `049-04` planned, хотя issue уже done. Новую задачу не создавал: это тот же hygiene-scope path/status/planning-summary consistency.
progress - 2026-05-18 12:04 MSK scheduler-3: подтвержден новый path/status drift после hardening-среза `056`: файл `machine/issue/work/056-lock-down-local-connection-profile-file-permissions.md` уже `status - done`, но всё ещё лежит в `work/`. Новую задачу не создавал: это тот же hygiene-scope `051`.
progress - 2026-05-18 15:00 MSK scheduler-3: подтвержден свежий planning/content hygiene drift: `sprint_11/cost.md` всё ещё показывает `044-highlight-functional-area-outline-on-select.md` как planned, хотя issue уже `status - done`; в конце файла `044` обнаружены stray characters `яё`. Новую задачу не создавал: это тот же S-scope issue-tree/planning-summary/content-cleanup `051`.

progress - 2026-05-18 16:00 MSK scheduler-2: взято как highest-priority suitable S task после обнаружения пустого `backlog/`; выбрана вместо product/code задач, потому что она прямо устраняет drift, мешающий schedulers выбирать задачи корректно.
progress - 2026-05-18 16:00 MSK scheduler-2: выполнен текущий cleanup: `work/` и `backlog/` проверены как пустые; `sprint_11/cost.md` синхронизирован с `044 status - done`; удален stray-префикс из `sprint_11/042`; добавлен `machine/issue/consistency-checklist.md`; `sprint_12/cost.md` получил свежий status snapshot. Product/code scope не менялся.
validation - 2026-05-18 16:00 MSK scheduler-2: `find machine/issue/work -maxdepth 1 -type f` и `find machine/issue/backlog -maxdepth 1 -type f` не вернули файлов; metadata spot-check для измененных issue/cost файлов пройден.
