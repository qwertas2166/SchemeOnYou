summary - Нормализовать done FK-preview issue вне work после закрытия 068
status - done
result - `068-surface-derived-db-relation-meaning-in-fk-preview.md` перенесен из `machine/issue/work/` в `machine/issue/sprint_30/` без изменения result/check metadata; `work/` и `backlog/` больше не содержат done issue.
check - grep -R "^status - done" -n machine/issue/work machine/issue/backlog returned no matches (exit 1)
priority - high
cost - XS

goal - Вернуть `machine/issue/work/` к текущей issue-tree policy: done issue не должен оставаться в active staging.

description - После выполнения `068-surface-derived-db-relation-meaning-in-fk-preview.md` файл остался в `machine/issue/work/` со `status - done`. Это нарушает `machine/issue/consistency-checklist.md`: `work/` опционален для active staging и, если используется, содержит только `status - work` или `status - in_progress`; done issue должны жить в `sprint_N/` или history location. Ранее закрытые hygiene-задачи `062` и `064` зафиксировали policy и прошлые drift-состояния, но не покрывают новый post-068 drift.

acceptance criteria -
- `machine/issue/work/` не содержит `status - done` issue-файлов после cleanup.
- `068-surface-derived-db-relation-meaning-in-fk-preview.md` перенесен в подходящий `sprint_N/`/history location или иначе приведен к policy без потери result/check/progress metadata.
- Ближайший релевантный `cost.md` или progress note содержит короткую dated correction/history запись, если это нужно для понятной истории.
- `machine/issue/consistency-checklist.md` остается source of truth и не противоречит итоговому дереву.
- Проверка `grep -R "^status - done" -n machine/issue/work machine/issue/backlog` не находит done-файлов в active/backlog staging.

dependencies/risks -
- Depends on completed `work/068-surface-derived-db-relation-meaning-in-fk-preview.md`.
- Related to completed `sprint_21/062` and `sprint_25/064`, but not duplicate: это новый drift после закрытия 068.
- Риск: при переносе можно потерять result/check контекст 068; делать только file move / planning-summary sync, без изменения product/code scope.

progress - 2026-05-19 20:05 Europe/Moscow: moved from `backlog/` to `sprint_30/`, closed as hygiene cleanup; canonicalized completed `068` from `work/` to `sprint_30/`.
