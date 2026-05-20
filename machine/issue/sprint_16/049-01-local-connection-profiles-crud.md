summary - Реализовать local PostgreSQL connection profiles CRUD

status - done
priority - high
cost - M
parent - 049-import-tables-from-database-connection.md

goal - Дать DB import flow локальное хранение нескольких PostgreSQL connection profiles с полным CRUD без включения секретов в project JSON/export.

description - Первый M-срез для `049`: локальная модель/сервис хранения connection profiles и UI-facing API для create/read/update/delete. MVP допускает plain text password в локальном файле. Version/migration для profiles file не требуется.

scope -
- Profile model: display name, host, port, database, user, password, optional default schema/catalog, driver kind `postgresql`.
- Local profiles file outside project JSON/export/Git-friendly project storage, stored in the folder next to the launcher so the application remains portable.
- Full CRUD: create, list/select, edit/update, delete.
- Redaction helpers for UI/errors/logs: password never printed in clear text unless user edits the field.
- Basic validation for required fields and port format.
- Small service API that later modal UI can call without depending on JavaFX controls.

out of scope -
- JDBC connection and metadata import.
- Schema/catalog discovery.
- Whole-diagram replace.
- Preview/diff.
- Profiles file version/migration.

acceptance criteria -
- Multiple profiles can be created, loaded, updated and deleted from the launcher-adjacent local profiles file.
- Plain text password storage works for MVP, but password is redacted in `toString`/display/error helpers.
- Profiles are not written into project storage/export.
- Missing/empty profiles file is handled as an empty profile list.
- Corrupt profiles file returns a clear recoverable error and does not crash app startup.
- Headless tests cover CRUD persistence, redaction and validation.

notes -
- SEE decisions: PostgreSQL first; local file secrets allowed; multiple saved profiles; full CRUD; no profiles version/migration for MVP; profiles file lives beside launcher for portable app behavior.

- Взято scheduler-2 2026-05-17 23:00 MSK: backlog пуст, выбран первый high/M planned-срез из sprint_16; безопасен как headless service без UI/JDBC.
- Реализованы `ConnectionProfileDraft`, `ConnectionProfile`, redacted view, validation и `ConnectionProfileStore` с launcher-adjacent path helper.
- Локальный profiles file отделен от project storage/export; missing/empty file читается как empty list; corrupt file возвращает recoverable error без password leak.
- Добавлены headless тесты CRUD persistence, redaction, validation, corrupt file и launcher-adjacent path.
- `mvn -q -pl core test` проходит; полный `mvn -q test` заблокирован правами на `client/target/classes/.../theme.css` (owned by `see`).
