status - done
summary - Вывести validation и context-line priority model в UI

goal - Сделать ошибки/предупреждения видимыми пользователю и сохранить предсказуемый footer/context behavior из дизайна v07.

description - DiagramValidator уже умеет находить проблемы FK, но результаты почти не представлены в UI. Нужно показывать validation в inspector/context line, выделять проблемные FK/колонки на canvas и соблюдать priority order: blocking confirmation, active preview/picker, relation pin state, recent result, selection tip. Это важно для FK-flow, delete confirmations и sketch-oriented предупреждений: пользователь должен понимать, когда действие опасно, когда можно продолжить, и что именно выбрано.

priority - mid
result - Validation surfaced in JavaFX inspector/context and canvas: FK issues color edges/badges, involved tables are marked, resolver has explicit priority order.
check - JAVA_HOME=/home/openclaw/workspace/java/openjdk-25.0.2 SEE_REPO=http://89.223.121.28:8181 /home/openclaw/workspace/java/apache-maven-3.9.11/bin/mvn -q -DskipTests compile; git diff --check
