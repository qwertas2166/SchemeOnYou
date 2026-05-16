status - done
result - Command palette now shows filterable command list and Enter/double-click executes registered UI actions. Wired MVP actions for new/save/open notice, diagrams, add table/column/FK/join table, rename/edit via undoable RenameTableCommand, delete selected, find, export, shortcuts, undo/redo, viewport controls.
check - JAVA_HOME=/home/openclaw/workspace/java/openjdk-25.0.2 PATH=/home/openclaw/workspace/java/openjdk-25.0.2/bin:/home/openclaw/workspace/java/apache-maven-3.9.11/bin:$PATH SEE_REPO=http://89.223.121.28:8181 mvn -q -DskipTests compile; git diff --check scoped to changed Java files OK (global git diff --check still reports pre-existing machine/work/cost_rule.md blank line at EOF).
summary - Превратить command palette из поиска в исполняемый инструмент

goal - Сделать все MVP-команды доступными с клавиатуры через command palette, как указано в требованиях.

description - Текущая palette умеет искать зарегистрированные команды, но результат показывается информационным окном и не запускает выбранную команду. Нужно заменить этот flow на неблокирующую palette/overlay или диалог с выбором результата, где Enter выполняет выбранную команду. Команды должны быть связаны с command layer и UI actions: new/open/save, new DB/sequence diagram, add table/column/FK/join table, rename/edit/delete selected, find element, export SVG, show shortcuts. Выполнение изменяющих действий должно идти через undoable commands, а не напрямую мутировать модель.

priority - mid