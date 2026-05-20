summary - Добавить table-level composite constraints для join table и derived relation semantics

status - done
priority - high
cost - M

goal - Закрыть разрыв между DB design v07 и текущей моделью: many-to-many/join table должен иметь явный composite PK/unique marker, а one-to-one/one-to-many/many-to-many должны выводиться из FK + PK/unique/composite constraints без отдельной editable relationship metadata.

description - В `design/final-summary.md` и `design/db-diagram-ui-ux-v07.md` зафиксировано: relation semantics выводятся из FK/PK/unique/composite constraints; join table создается с composite PK/unique marker. Сейчас `DbColumn` поддерживает только per-column `primaryKey/unique`, а `CreateJoinTableCommandFactory` помечает обе FK-колонки как `primaryKey=true`, без table-level composite constraint. Это визуально похоже на composite PK, но storage/model/validation не различают реальный composite constraint и две независимые PK/unique колонки. Нужно добавить минимальную MVP-модель composite constraints на уровне таблицы, сохранить backward-compatible JSON, обновить join-table creation, validation/inspector/SVG/canvas labels.

acceptance criteria -
- `DbTable` хранит table-level composite constraint(s) минимум для composite PK или composite unique по списку column ids.
- Join table creation создает один composite PK/unique marker на две FK-колонки вместо семантически неоднозначных независимых flags, либо сохраняет column flags только как визуальный fallback с явным composite constraint в model/storage.
- JSON writer/reader детерминированно сохраняет/читает constraints; старые файлы без constraints открываются с безопасными defaults.
- Validator учитывает composite unique/PK при derived relation semantics и предупреждениях FK target/source uniqueness.
- Canvas/SVG/inspector показывают composite marker текстом/label, не только цветом.
- Добавлены core tests для join-table command, storage round-trip, validation relation semantics.

dependencies/risks -
- Related to `sprint_1/005-05-done-join-table-compound-ui.md`, но не дублирует UI-flow: здесь model/storage/validation semantics.
- Related to `sprint_2/016-harden-json-and-svg-string-escaping.md` because JSON format changes should keep deterministic writer coverage.
- Risk: не разрастить в full constraint editor; MVP нужен только минимальный composite marker для relation semantics и join-table sketches.

progress - 2026-05-19 14:00 MSK scheduler-2: взята в работу из sprint-пула, потому что `machine/issue/backlog/` пуст, а это highest-priority M-задача среди доступных planned. Добавлен core slice: `DbTableConstraint`/`DbTableConstraintType`, storage round-trip с backward-compatible отсутствующим `constraints`, composite PK marker при создании join table, базовая validation целостности constraint column refs, core tests. Осталось добить UI/canvas/SVG/inspector labels и полную relation-semantics часть acceptance.
- 2026-05-19 18:30 MSK scheduler-2: продолжен UI/export slice composite constraints. Canvas теперь рисует явный `PK(...)`/`UNQ(...)` label после колонок, SVG export добавляет такой же текстовый marker, inspector показывает read-only summary `Composite PK (...)`; добавлены regression tests для inspector/SVG. Проверка: `mvn -q test` и `git diff --check` OK. Задача оставлена `in_progress`: остаются полная derived relation semantics/validator acceptance и возможная canvas snapshot-проверка.
- 2026-05-19 19:00 MSK scheduler-2: продолжен validation slice. FK target uniqueness теперь проходит через `isIndividuallyUnique(...)`, чтобы composite PK/UNQ не считался uniqueness для одиночной FK-колонки; добавлен regression test `DatabaseConstraintValidationTest` на composite-target ambiguity. Проверка: `mvn -q -pl core test` и `git diff --check` OK. Задача остается `in_progress`: нужен финальный проход по full derived relation semantics/закрытию acceptance.
- 2026-05-19 19:30 MSK scheduler-2: финализирован semantic slice. Добавлен `DbRelationSemantics`/`DbRelationMeaning`: FK meaning теперь выводится из индивидуальной uniqueness source/target (`many-to-one` vs `one-to-one`, ambiguous target), а join-table many-to-many определяется по двум FK + composite PK/UNQ на source columns. `DiagramValidator` использует общий uniqueness helper, чтобы composite constraints не давали ложную uniqueness для одиночной FK-колонки. Добавлены regression tests на derived one-to-one/many-to-one и many-to-many join-table detection. Проверка: `mvn -q -pl core -Dtest=DatabaseConstraintValidationTest test`, `mvn -q -pl core test`, `git diff --check` OK. Статус: done.
