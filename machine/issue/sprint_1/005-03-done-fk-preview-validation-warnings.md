summary - Подключить validation warnings к FK preview

goal - Предупреждать пользователя о сомнительных FK до создания связи, не блокируя sketch-oriented workflow без необходимости.

description - Использовать существующий DiagramValidator/validation model в FK preview: показывать type mismatch, missing/invalid endpoint, target not PK/unique и другие применимые предупреждения. Blocking ошибки должны требовать исправления или cancel; warnings должны позволять продолжить осознанно. Сообщения должны попадать в context line/preview по priority model и не скрываться обычными подсказками.

priority - mid
status - done
result - FK preview теперь показывает validation block в inspector/context: blocking errors не дают создать FK, warnings по type mismatch и target not PK/unique видны до подтверждения, но позволяют продолжить.
check - SEE_REPO=http://89.223.121.28:8181 mvn -q -DskipTests compile
