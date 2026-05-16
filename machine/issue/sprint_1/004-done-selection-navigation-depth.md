summary - Доработать keyboard-навигацию и selection depth на canvas

goal - Сделать canvas реально keyboard-first, а не только отображающим и принимающим отдельные команды.

description - Сейчас selectedTableId меняется в основном через левый список, а canvas не реализует полноценную навигацию по объектам. Нужно добавить table depth и column depth: Home/End, стрелки между таблицами, Enter вход в колонки, Up/Down по колонкам, Esc назад к таблице, выделение FK edge, визуальные focus/selection indicators. Это требуется для сценариев `выбрать элемент`, `изменить элемент`, `создать связь` без мыши и без обязательного использования project tree. Навигация должна быть детерминированной и работать с auto-layout.

priority - high
clarification - Решения SEE от 2026-05-15:
- Стартовый selection/focus при открытии диаграммы: левое меню.
- Стрелки на canvas выбирают таблицу геометрически по направлению.
- `Enter` на таблице входит в column depth и выбирает первую колонку.
- `Esc` из column depth возвращает selection на таблицу.
- В этом спринте достаточно table/column selection; FK edge selection можно отложить.

status - done
result - Canvas now starts focus on left menu and supports keyboard-first table/column selection: Home/End choose first/last table, arrows move geometrically between tables, Enter enters first-column depth, Up/Down move between columns, Esc returns to table depth; mouse table click and visual table/column selection indicators were added.
check - JAVA_HOME=/home/openclaw/workspace/java/openjdk-25.0.2 PATH=/home/openclaw/workspace/java/openjdk-25.0.2/bin:/home/openclaw/workspace/java/apache-maven-3.9.11/bin:$PATH SEE_REPO=http://89.223.121.28:8181 mvn -q -DskipTests compile; custom whitespace check for changed files OK; global git diff --check still reports pre-existing machine/work/cost_rule.md blank line at EOF.
