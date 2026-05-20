summary - Нормализовать issue tree после закрытия sequence domain task в work
status - done
priority - high
cost - XS

goal - Вернуть `machine/issue/work/` в консистентное состояние после завершения sequence-domain задачи, чтобы scheduler не считал done-файл активной работой.

description - После выполнения `021-sequence-domain-types-storage-validation.md` файл остался в `machine/issue/work/` со `status - done`. Это нарушает `machine/issue/consistency-checklist.md`: `work/` должен содержать только активные `status - work` или `status - in_progress` issue. Историческая hygiene-задача `051` уже закрыта и описывает предыдущие drift-состояния; текущий drift появился позже и требует отдельного минимального cleanup-среза без изменения product/code scope.

acceptance criteria -
- `machine/issue/work/` не содержит done issue-файлов после cleanup.
- `021-sequence-domain-types-storage-validation.md` перенесен в подходящий `sprint_N/`/history location или иначе приведен к единой path/status policy без потери progress/history.
- Ближайшие `cost.md`/planning summaries не противоречат новому расположению `021` или имеют короткую dated correction note.
- `machine/issue/consistency-checklist.md` остается source of truth; product/code scope не меняется.
- Проверка `find machine/issue/work -maxdepth 1 -type f -name '*.md'` и metadata spot-check подтверждают консистентность.

dependencies/risks -
- Related to completed `sprint_12/051-normalize-issue-tree-after-work-cleanup.md`, but not duplicate: `051` закрыла более ранний drift; этот файл фиксирует новый post-sequence drift.
- Risk: при переносе можно потерять progress/history `021`; делать только file move / planning-summary sync, без переписывания смысла задачи.
progress - 2026-05-19 10:00 MSK scheduler-2: taken from backlog as highest-priority suitable task (high/XS). Moved completed `021` from `work/` to `sprint_3/`, added dated planning corrections, and verified `work/` has no done issue files. Product/code scope unchanged.
