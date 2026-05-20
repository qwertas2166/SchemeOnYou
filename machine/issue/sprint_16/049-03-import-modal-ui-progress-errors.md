summary - Реализовать modal UI для DB import profiles/schema/progress/errors

status - done
priority - high
cost - M
parent - 049-import-tables-from-database-connection.md

goal - Дать пользователю модальный UI для выбора/CRUD профиля, schema/catalog и запуска DB import с понятными progress/error состояниями.

description - Третий M-срез для `049`: JavaFX modal flow поверх profiles service и metadata importer. UI нужен для MVP; preview/diff не нужен. Перед destructive replace показывается отдельное модальное предупреждение.

scope -
- Entry point from app command/menu/Space-command as appropriate for DB import.
- Modal dialog for selecting saved profile and running full profile CRUD.
- Schema/catalog selection after successful connection or via default profile value.
- Import action with disabled controls while work is running.
- Progress/status surface: indeterminate stages `Connecting` / `Reading metadata` / `Importing` / `Done`, plus tables counter.
- User-facing errors for connection/schema/permissions/import failures.
- Password redaction in all labels/errors/log-like UI surfaces.
- Warning dialog before calling whole-diagram replace integration.

out of scope -
- Implementing metadata mapper internals beyond calling its service.
- Actual project/diagram replacement internals beyond invoking replace API.
- Preview/diff.
- Profiles file migration/version.

acceptance criteria -
- User can create/edit/delete/select profile from the import modal flow.
- User can choose schema/catalog and start import.
- UI remains responsive; long work shows stage progress/status and table counter.
- Import errors are visible and actionable without exposing password.
- Before replace, UI warns that all current content of the selected DB diagram will be deleted and replaced by import result.
- User can cancel before destructive replace.

notes -
- SEE decision: modal import UI required; preview/diff explicitly not required; progress is indeterminate by stage plus table counter.
- 2026-05-18 11:30 MSK scheduler-2: started implementation. Added `Import DB` top-bar/command-palette entry and JavaFX modal with local profile create/edit/delete/select, schema/catalog field, destructive replace warning, background PostgreSQL metadata import, indeterminate progress/status and password redaction for UI errors. Remaining validation: full Maven client test is blocked by existing `client/target/classes/.../theme.css` permission issue; manual javac compile of client sources passed.

progress - 2026-05-18 12:30 MSK scheduler-2: продолжил текущий high/M UI-срез при пустом backlog. Повторно проверил реализацию modal DB import: profile CRUD/select, schema/catalog field, destructive replace warning, background metadata import, progress/errors и redaction. Verification: `mvn -q -pl core test`, `mvn -q -pl client -am -DskipTests -Dmaven.resources.skip=true test-compile`, `git diff --check` passed; обычный client package/full test заблокирован существующим permission issue на `client/target/classes/see/schemeonyou/ui/theme.css`. Status set to done.
