summary - Обработать composite FK в DB import: поддержка или явная деградация
status - done
priority - mid
cost - M

goal - Убрать silent semantic loss при PostgreSQL import схем с multi-column foreign keys.

description - `PostgreSqlMetadataImporter.readForeignKeys()` читает `KEY_SEQ`, но `DbImportForeignKey` и `DbImportDiagramReplacer` фактически создают отдельный single-column `ForeignKey` на каждую строку metadata. Для composite FK это выглядит как несколько независимых связей, хотя в БД это один constraint. MVP-модель FK сейчас single-column, а `025` покрывает table-level composite PK/unique semantics, но не фиксирует policy для composite FK import. Нужно выбрать и реализовать безопасное MVP-поведение: либо добавить минимальную grouped representation, если модель к тому времени готова, либо явно детектировать composite FK и деградировать без молчаливой лжи (например warning/import note + deterministic per-column edges с общим label/group id, или skip with warning — по решению).

acceptance criteria -
- Composite FK groups детектируются по FK name/source table/target table/key sequence, а не теряются как независимые случайные edges.
- Принято и записано краткое policy decision: supported grouped FK vs MVP degradation behavior.
- Если grouped FK поддержан: model/storage/SVG/canvas/validation сохраняют и показывают multi-column relation без отдельной editable relationship metadata.
- Если выбран degradation path: пользователь получает понятный warning/import note, а результат не утверждает неверную cardinality/semantics; save/load остается валидным.
- Single-column FK import не регрессирует.
- Добавлены headless tests на composite FK metadata ordering и deterministic output.

dependencies/risks -
- Related to `sprint_4/025-db-composite-constraints-and-relation-semantics.md`, but not duplicate: `025` про table-level composite constraints and derived relation semantics; this task is specifically DB metadata import policy for multi-column FK constraints.
- Depends on completed DB import mapper/replacer slices `049-02` and `049-04`.
- Risk: full grouped-FK model may grow beyond M; if so, keep this task to explicit detection/degradation and split model support separately.


policy decision - MVP degradation path selected: composite foreign keys are detected as metadata groups and skipped with an import warning. The current model remains single-column FK only; creating per-column edges for a composite constraint would silently misrepresent database semantics. Full grouped FK model support remains out of scope for this M slice and should be handled separately if needed.

progress - 2026-05-19 13:30 MSK scheduler-2
- Took `059` from active `sprint_26` because `machine/issue/backlog/` is empty and `059` is the nearest planned M-or-less DB-import follow-up.
- Implemented deterministic composite FK grouping in `PostgreSqlMetadataImporter`: groups by FK name/source table/target table, orders columns by `KEY_SEQ`, imports only single-column FK groups.
- Added `DbImportResult.importWarnings()` and UI status surfacing; composite FK groups are skipped with a warning instead of being turned into misleading independent edges.
- Added headless tests for composite FK ordering/warning and mixed composite+single-column import; existing 3-arg `DbImportResult` constructor kept for compatibility.
- Verification: `mvn -q -pl core test` pass; `mvn -q -pl client -am test` pass; targeted trailing-whitespace check pass.
