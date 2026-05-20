summary - Реализовать PostgreSQL metadata mapper для DB import

status - done
priority - high
cost - M
parent - 049-import-tables-from-database-connection.md

goal - Прочитать PostgreSQL schema/catalog metadata и преобразовать таблицы, колонки, PK/nullable и FK в domain-модель без UI-зависимостей.

description - Второй M-срез для `049`: headless JDBC/PostgreSQL import service и mapper. Сервис принимает connection/profile параметры и выбранную schema/catalog, читает `DatabaseMetaData`, строит промежуточный import result/domain diagram data.

scope -
- PostgreSQL-only connection/import path with explicit Maven dependency on PostgreSQL JDBC driver.
- Schema/catalog-aware table listing for selected schema/catalog.
- Import tables, columns, SQL type names, nullable flags, primary key markers.
- Import foreign keys when available through JDBC metadata.
- Clear error model for connection failure, missing schema/catalog, insufficient rights and unsupported metadata.
- Headless mapper tests with mock/fake metadata or minimal test doubles.

out of scope -
- JavaFX modal UI.
- Saved profiles CRUD UI/storage, except consuming profile-like connection settings.
- Writing into active project/diagram.
- Whole-diagram replace command integration.
- Preview/diff.
- Non-MVP constraints/objects: unique, indexes, default values, check constraints, sequences, views/materialized views and anything beyond tables/columns/types/nullable/PK/FK.

acceptance criteria -
- Mapper returns deterministic tables/columns/types/nullable/PK/FKs for a selected schema/catalog.
- Nullable and PK markers are preserved.
- FK source/target table/column mapping is stable and explicit.
- Failures are represented as user-facing import errors without leaking password.
- Headless tests cover metadata mapping and error cases.

notes -
- SEE decision 2026-05-17 22:21 MSK: add explicit PostgreSQL JDBC Maven dependency; ignore everything except tables/columns/types/nullable/PK/FK for MVP.

progress - 2026-05-18 09:30 MSK scheduler-2:
- Backlog directory is empty, so continued the next highest-priority eligible planned slice: `049-02` high/M after completed `049-01`.
- Added explicit PostgreSQL JDBC Maven dependency (`org.postgresql:postgresql`).
- Implemented headless `PostgreSqlMetadataImporter` with JDBC `DatabaseMetaData` mapping for selected schema/catalog: tables, columns, SQL type names, nullable, PK and imported FKs.
- Added import result/error records and fake-metadata headless tests for deterministic mapping, required schema error and empty-schema/table error.
- Verification: `mvn -q -pl core test` passed with local Java/Maven toolchain; full `mvn -q test` remains blocked by existing client target permission error on `client/target/classes/see/schemeonyou/ui/theme.css` (Operation not permitted).
