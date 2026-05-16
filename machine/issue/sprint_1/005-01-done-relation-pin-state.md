status - done
result - Реализован relation pin/unpin: Space P закрепляет выбранную таблицу или колонку, Space U очищает pin, команды доступны в palette/shortcuts, pin виден badge на canvas, context line показывает pin с приоритетом над selection/validation и очищается при смене диаграммы/удалении pinned table/column.
check - JAVA_HOME=/home/openclaw/workspace/java/openjdk-25.0.2 PATH=/home/openclaw/workspace/java/openjdk-25.0.2/bin:/home/openclaw/workspace/java/apache-maven-3.9.11/bin:$PATH SEE_REPO=http://89.223.121.28:8181 mvn -q -DskipTests compile; git diff --check -- touched files OK (global git diff --check still reports pre-existing machine/work/cost_rule.md blank line at EOF).
summary - Реализовать relation pin / unpin и видимое состояние pin

goal - Подготовить основу FK-flow: пользователь должен явно закреплять target/source объект для следующей связи и видеть это состояние.

description - Добавить рабочий UI-flow для relation pin на выбранной таблице/колонке: `Space P` закрепляет выбранный объект, `Space U` очищает pin. Pin должен быть виден на canvas как badge/marker и отражаться в context line по priority model. Pin должен переживать обычную навигацию, но очищаться при удалении pinned объекта. В этом срезе не требуется полноценное создание FK preview; цель — надежное состояние pin и понятная обратная связь.

priority - mid
