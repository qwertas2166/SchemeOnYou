summary - Добавить drag-and-drop позиционирование элементов на canvas

status - done
priority - mid
cost - M

analysis - Текущий продукт keyboard-first, но не keyboard-only: все MVP-действия должны оставаться доступными с клавиатуры, при этом mouse/drag может быть удобным дополнительным способом работы. Сейчас canvas уже поддерживает mouse click selection, scroll/pan/zoom и deterministic layout, но ручное позиционирование таблиц/participants через drag-and-drop отсутствует. В дизайне допустимы mouse soft positions: пользовательские позиции можно сохранять как hints, а команда Layout diagram должна уметь нормализовать canvas обратно детерминированно.

problem - После auto-layout пользователь не может быстро визуально расставить таблицы или sequence participants мышью. Это снижает удобство презентационного/скетчевого сценария и делает SVG export менее управляемым, хотя редактор позиционируется как visual canvas first.

goal - Реализовать drag-and-drop перемещение canvas-элементов как convenience input без превращения MVP в mouse-first workflow.

scope -
- DB diagram: drag selected table card по canvas.
- Sequence diagram: drag participant card по canvas; сообщения/activation следуют за позициями participants при redraw/export.
- Mouse drag должен обновлять `CanvasState.boundsByElementId` для перемещаемого элемента.
- Перемещение должно быть undoable command или явно сгруппировано как одно undoable действие на mouse release.
- Во время drag учитывать viewport transform: zoom, viewportX, viewportY.
- После drag selection остается на перемещенном элементе.
- SVG export должен использовать пользовательские позиции.
- Команда Layout diagram (`Space L D`) должна сбрасывать/пересчитать layout детерминированно и тем самым нормализовать ручные позиции.
- Keyboard-first требования не меняются: создание/редактирование/удаление элементов не должны требовать мышь.

out_of_scope -
- Drag-and-drop создание новых таблиц/колонок из palette/tree.
- Rubber-band multi-select.
- Edge routing редактор и ручное перетаскивание FK/message edges.
- Grid snapping/align guides, если это потребует отдельной UI-сложности.
- Persisted пользовательские layout profiles.

acceptance -
- Пользователь может зажать таблицу DB diagram мышью, переместить и отпустить; позиция сохраняется в canvas state и видна после redraw.
- Пользователь может переместить sequence participant; lifeline/messages/SVG следуют новой X/Y позиции participant.
- Drag работает корректно при zoom != 1 и после pan.
- Drag не конфликтует с canvas pan: клик/drag по элементу двигает элемент, drag по пустому canvas pan-ит viewport как раньше.
- Одно перемещение откатывается одним Undo и возвращается одним Redo.
- Save/load round-trip сохраняет пользовательские позиции.
- `Space L D` возвращает элементы к deterministic layout.
- `mvn test` и `mvn -DskipTests package` проходят.

implementation_notes -
- Добавить MoveCanvasElementCommand(elementId, oldRect, newRect) в core command layer.
- В JavaFX canvas mouse pressed определить hit-test table/participant с учетом viewport transform.
- На mouse dragged временно обновлять bounds для live feedback; на release выполнить/зафиксировать command так, чтобы undo вернул oldRect.
- Для sequence сообщений не хранить ручные bounds как источник истины, если достаточно вычислять y по порядку, а x брать из participant bounds.
- Возможно понадобится отличать `draggingElementId` от существующих `selectedTableId/selectedColumnId`; для sequence может потребоваться отдельный selectedParticipantId/selectedMessageId.

risks -
- Текущий `redraw()` всегда вызывает `layout.layout(activeDiagram)`, что может перетирать ручные bounds. Нужно изменить layout policy: auto-layout только для элементов без bounds или по явной команде Layout diagram.
- Undo для live drag нельзя делать на каждый mouse move; нужно одно действие на release.
- Не сломать keyboard navigation, где геометрия таблиц используется для arrow selection.

progress - 2026-05-15T12:30:00+03:00 scheduler-2: backlog directory is empty, continued active M task 013. Added sequence participant selection state for click/drag: dragged participants stay selected, get highlighted on canvas, and show a simple inspector entry. This closes the sequence-side "selection remains on moved element" slice. Verification remains blocked locally: `mvn`, `java`, and `javac` are not installed in this runtime.

progress - 2026-05-17 13:05 MSK scheduler-1: moved from backlog to sprint_13; priority set to mid (M UX-slice, но после layout-policy проверки).

progress - 2026-05-18 20:33 MSK scheduler-3: current code review shows drag-and-drop positioning implemented: JavaFX canvas handles mouse press/drag/release with viewport transform, updates bounds live, commits one `MoveCanvasElementCommand` on release, supports DB tables and sequence participants via `CanvasHitTestPresenter`, preserves selection, and `DeterministicLayoutEngine.layout()` no longer overwrites existing bounds while `resetLayout()` clears/rebuilds them. Marked done after local verification.
