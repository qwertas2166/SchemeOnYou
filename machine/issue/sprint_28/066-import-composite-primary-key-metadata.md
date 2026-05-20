summary - Согласовать PostgreSQL import composite primary keys с table-level constraints model
status - done
priority - mid
cost - M

goal - Не искажать DB metadata import для таблиц с multi-column primary key после появления table-level composite constraints.

description - `025` добавляет `DbTableConstraint`/`DbTableConstraintType` и storage для table-level composite constraints, а `059` уже запретил silent semantic loss для composite FK. Но `PostgreSqlMetadataImporter.readPrimaryKeys()` сейчас собирает только `Set<String>` колонок PK и `readColumns()` помечает каждую PK-колонку `primaryKey=true`, без учета `PK_NAME`/`KEY_SEQ` и без создания table-level composite PK constraint в import result/replacer. Для composite primary key это может выглядеть как набор независимых column PK flags и не использовать новую constraint-модель. Нужен import/replacer slice: либо создавать deterministic composite PK constraint для multi-column PK, либо явно записать MVP degradation policy, если UI semantics еще не готова.

acceptance criteria -
- PostgreSQL primary keys читаются с deterministic ordering по `KEY_SEQ` и stable fallback ordering.
- Single-column PK import не регрессирует.
- Multi-column PK не теряет семантику молча: создается table-level `COMPOSITE_PRIMARY_KEY` constraint в imported diagram либо пользователь получает явный import warning/degradation note.
- `DbImportResult`/`DbImportDiagramReplacer` поддерживают выбранное поведение без записи connection/profile secrets в project JSON/export.
- JSON round-trip/validation сохраняют валидность импортированной диаграммы.
- Добавлены headless tests на multi-column PK metadata ordering и deterministic output.

dependencies/risks -
- Depends on core model/storage part of `sprint_4/025-db-composite-constraints-and-relation-semantics.md`; can start after that slice is stable.
- Related to `sprint_26/059`, but not duplicate: `059` covers composite foreign keys; this task covers primary key metadata mapping.
- Risk: if full UI labels/relation semantics from `025` are incomplete, keep this task to import data/model correctness and split UI polish separately.

progress - 2026-05-19 14:30 MSK: taken by scheduler-2; selected as the only backlog task and size M within allowed M/S/XS scope.

progress - 2026-05-19 14:30 MSK: implemented importer/replacer support for ordered composite primary-key metadata on current table-level constraint model; targeted core tests passed.
