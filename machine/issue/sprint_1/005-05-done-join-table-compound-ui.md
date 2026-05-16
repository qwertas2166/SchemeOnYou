summary - Реализовать UI создания join table как compound action

goal - Закрыть many-to-many сценарий DB diagram через undo-safe join table creation.

description - Для `Space A J` добавить keyboard flow создания join table между выбранной и pinned таблицами. Действие должно использовать существующую CreateJoinTableCommandFactory или эквивалентный CompoundCommand: создать join table, две FK-колонки, PK/unique markers по MVP-правилам и две FK-связи. Перед созданием нужен preview с понятными участниками и confirm/cancel. Undo должен откатывать весь compound action целиком.

priority - mid
status - done
result - Space A J теперь показывает confirm preview для join table с участниками и создаваемыми колонками; создание выполняется одной CompoundCommand, FK-колонки помечаются как composite PK, pin очищается после создания.
check - SEE_REPO=http://89.223.121.28:8181 mvn -q -DskipTests compile
