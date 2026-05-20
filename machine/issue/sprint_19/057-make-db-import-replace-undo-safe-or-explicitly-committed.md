summary - Сделать DB import replace undo-safe или явно оформить committed destructive exception

status - done
priority - high
cost - M
parent - 049-import-tables-from-database-connection.md

goal - Согласовать whole-diagram DB import replace с общим safety rule: разрушительное изменение должно быть undo-safe либо иметь явно задокументированное исключение с сильной защитой UI.

description - После закрытия `049-04` и `049-03` DB import replacement вызывается из modal UI через `DbImportDiagramReplacer.replace(...)`, который напрямую очищает и пересоздает содержимое выбранной DB-диаграммы, а затем app-shell вызывает `markDirty()`. Это закрывает replace-flow, но не проходит через `UndoRedoStack`/command layer и не дает пользователю `Ctrl+Z` вернуть прежнюю диаграмму. Глобальная machine safety policy говорит, что mutating/compound/destructive operations должны идти через undo-safe commands; в `049-04` допускалось исключение только если явно задокументировать committed destructive action, но такого отдельного решения в коде/README не найдено.

acceptance criteria -
- Предпочтительный вариант: DB import replace оформлен как undoable command или compound command, сохраняющий snapshot прежнего содержимого выбранной DB-диаграммы и восстанавливающий его по Undo.
- `Ctrl+Z` после успешного import replace возвращает предыдущие tables/FKs/canvas bounds/selection-safe state выбранной DB-диаграммы; Redo снова применяет import result.
- Auto-create DB diagram path также имеет понятную undo semantics: либо удаляет созданную диаграмму при Undo, либо явно документирует ограничение.
- UI после import показывает result/context с явной подсказкой про Undo, если undo-safe вариант реализован.
- Если команда получается слишком рискованной для MVP, вместо этого принято и записано явное product/engineering decision: import replace является committed destructive action, защищенным modal warning; решение добавлено в `machine/README.md` и `049`/`049-04`, а warning text достаточно сильный.
- Headless tests покрывают undo/redo replace или, для exception-варианта, проверяют отсутствие ложной Undo-подсказки и наличие документации.
- Существующий profiles/modal/metadata import flow не регрессирует; `mvn -q -pl core test` и релевантный client compile/package gate проходят или blocker явно зафиксирован.

dependencies/risks -
- Depends on completed DB import slices `049-03` and `049-04`.
- Related to `006-done-delete-and-safe-destructive-actions.md`, но не дубликат: там covered selected delete; здесь whole-diagram import replace.
- Risk: snapshot whole diagram may touch storage/model copying semantics; keep first implementation M-sized by cloning only selected diagram content needed for undo.
- Risk: committed-exception вариант слабее общего safety rule; если выбран, нужен явный SEE/product decision, а не молчаливое отклонение.

notes -
- Обнаружено scheduler-3 project analysis/backlog refinement 2026-05-18 12:32 MSK после проверки `DbImportDialog` и `DbImportDiagramReplacer`.

progress - 2026-05-18 13:00 MSK scheduler-2: взято в работу; выбран undo-safe вариант через command layer, так как задача high/M и это предпочтительный acceptance path.
progress - 2026-05-18 13:00 MSK scheduler-2: реализован undoable `DbImportReplaceCommand` с snapshot/restore диаграмм, импорт подключен к `UndoRedoStack`, UI сообщает `Ctrl+Z to undo`, добавлены core tests на replace undo/redo и auto-created diagram undo/redo. Проверки: `mvn -q -pl core test` OK; `mvn -q -pl client -am -Dmaven.resources.skip=true test` OK. Обычный `mvn -q -pl client -am test` заблокирован правами на `client/target/classes/see/schemeonyou/ui/theme.css` (owned by see).

progress - 2026-05-18 13:06 MSK scheduler-1: backlog analysis актуализировал задачу как done по уже записанному progress/evidence (`DbImportReplaceCommand`, UI Ctrl+Z hint, core/client test notes) и перенес из `work/` в `sprint_19/`.

progress - 2026-05-18 17:03 MSK scheduler-1: hourly backlog analysis подтвердил `done`; backlog/work пусты, новых переносов/вопросов/XL-декомпозиции нет.
