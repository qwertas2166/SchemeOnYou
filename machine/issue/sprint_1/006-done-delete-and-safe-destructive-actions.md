status - done
result - Добавлены undo-safe команды удаления колонок и FK, preview последствий для table/column/FK, controller Delete selected с confirm/cancel и keymap Space D/Delete.
check - JAVA_HOME=/home/openclaw/workspace/java/openjdk-25.0.2 PATH=/home/openclaw/workspace/java/openjdk-25.0.2/bin:/snap/intellij-idea-community/743/plugins/maven/lib/maven3/bin:$PATH SEE_REPO=http://89.223.121.28:8181 mvn -q -DskipTests compile; custom trailing-whitespace check OK.

summary - Добавить удаление выбранных элементов с undo-safe подтверждением

goal - Закрыть требования безопасности изменений: нет silent destructive actions, удаление undo-safe, массовое удаление требует подтверждения.

description - В core есть DeleteTableCommand, но UI-сценарий Delete selected не реализован полноценно. Нужно добавить Delete для выбранной таблицы/колонки/FK edge, показывать preview последствий, особенно удаляемые FK при удалении таблицы или колонки, и требовать подтверждение для destructive/bulk cases. Все удаления должны идти через command layer и поддерживать undo/redo. Context line должен иметь высокий приоритет для подтверждения удаления, чтобы подсказки/тосты не скрывали рискованное действие.

priority - high
