summary - Провести sequence add/edit flows через command palette, Space commands и inspector
status - done
priority - high
cost - M

goal - Сделать sequence diagram реальным keyboard-first MVP, а не только canvas rendering/export slice.

description - В требованиях command palette содержит `Add participant` и `Add message`, а sequence MVP требует выбор/редактирование participant/message. Сейчас palette регистрирует DB add-команды, но не sequence add participant/message; пустой sequence canvas даже подсказывает `Add participant/message`, которых нет в `registerCommands()`. Inspector для selected participant показывает только read-only name, selected message state отсутствует, message properties не редактируются. Нужно добавить keyboard-accessible add participant/add message flows через command layer, selection для participant/message, inspector editing через undoable commands и понятные shortcuts/Space hints в sequence context.

acceptance criteria -
- Command palette содержит и исполняет `Add participant` и `Add message` для sequence diagram context.
- Добавление participant/message выполняется undoable commands, обновляет canvas, project tree/selection и inspector.
- Есть keyboard path для выбора sequence participant и message; мышь может дублировать, но не является единственным способом.
- Inspector редактирует participant name/type и message source/target/label/order/type/activation через undoable commands.
- Empty sequence hint, F1 overlay, ShortcutMap и Space hints не рекламируют несуществующие команды.
- Esc/Enter/focus behavior согласован с текущей inspector policy; не добавляются новые blocking dialogs для частых flows, если можно использовать existing picker/inspector.
- Добавлены presenter/core tests или manual checklist для add/edit/select participant/message + undo/redo.

dependencies/risks -
- Depends on or should align with `021-sequence-domain-types-storage-validation.md` for type/order fields.
- Related to `work/007-sequence-diagram-mvp-ui.md`; это декомпозиция его keyboard/editing части, не повтор rendering/export slice.
- Related to `sprint_2/015-non-modal-keyboard-flows-for-frequent-actions.md` if picker/input surfaces become non-modal.
- Risk: message source/target picker может стать L; если scope растет, first slice can default source/target from selected + nearest participant and leave richer picker as separate S/M task.

clarification - Решения SEE от 2026-05-15:
- Для первого релиза delete/edit нужны для всех sequence elements.
- MVP sequence: participant + message + activation достаточно.
- SVG semantic enough; не тратить scope на pixel-parity.


progress -
- 2026-05-15 scheduler-2: взята как приоритетный M-slice после закрытых 026-01/026-02; выбран sequence editing, потому что SEE уточнил этот seam как следующий после command routing/document lifecycle.
- 2026-05-15 scheduler-2: добавлены command palette/Space команды `Add participant` (`Space A P`) и `Add message` (`Space A M`) для sequence context; добавление идет через undoable core commands и выбирает созданный participant/message.
- 2026-05-15 scheduler-2: добавлена клавиатурная навигация по sequence participant/message (Home/End/Left/Right/Up/Down) и inspector editing для participant name, message label и activation через undoable `EditValueCommand`.
- 2026-05-15 scheduler-2: добавлены headless core tests `SequenceCommandTest` для add/edit + undo/redo.

validation -
- 2026-05-15 scheduler-2: `JAVA_HOME=/home/openclaw/workspace/java/openjdk-25.0.2 PATH=/home/openclaw/workspace/java/apache-maven-3.9.11/bin:/home/openclaw/workspace/java/openjdk-25.0.2/bin:$PATH mvn -pl client -am -Dtest=SequenceCommandTest,CommandRouterTest -Dsurefire.failIfNoSpecifiedTests=false test` — pass, 5 tests.
- 2026-05-15 scheduler-2: `JAVA_HOME=/home/openclaw/workspace/java/openjdk-25.0.2 PATH=/home/openclaw/workspace/java/apache-maven-3.9.11/bin:/home/openclaw/workspace/java/openjdk-25.0.2/bin:$PATH mvn -q -pl client -am test` — pass.
- 2026-05-19 21:02 MSK scheduler-3: analysis pass confirmed this active issue still covers the main remaining sequence MVP gap; current inspector edits label/type/order/activation but keeps message `From`/`To` read-only, so acceptance for editing source/target is not closed yet. Do not create a duplicate backlog issue; next implementation slice should add keyboard-accessible endpoint selection/editing or explicitly split it if picker scope grows beyond M.
- 2026-05-19 21:30 MSK scheduler-2: backlog/ пуст; продолжена highest-priority high/M активная задача. Закрыт оставшийся sequence message endpoint editing slice: inspector `From`/`To` стали keyboard-editable text fields, принимают participant id или name, изменения идут через undoable `EditValueCommand`; `SequenceMessage` получил undo-safe setters для endpoints. Verification: `mvn -q -pl client -am -Dtest=InspectorPresenterTest,CommandRouterTest,SequenceCommandTest -Dsurefire.failIfNoSpecifiedTests=false test`, `git diff --check` passed. Status set to done.
