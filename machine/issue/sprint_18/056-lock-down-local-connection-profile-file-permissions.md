summary - Ограничить права локального файла connection profiles

status - done
priority - mid
cost - S
parent - 049-import-tables-from-database-connection.md

goal - Снизить риск случайного раскрытия plain-text паролей из локального `schemeonyou-connection-profiles.local`, не усложняя MVP secure-storage модель.

description - В MVP plain-text password storage разрешен, а `049-01` уже отделил profiles file от project JSON/export и добавил redaction. Но `ConnectionProfileStore.write(...)` сейчас создает launcher-adjacent файл обычными правами umask/платформы и не пытается выставить owner-only permissions на POSIX-системах. Для Linux/macOS release это оставляет локальный секрет потенциально world/group-readable в зависимости от окружения. Нужен небольшой hardening-slice: best-effort owner-only permissions для profiles file/temp file и явная документация поведения на non-POSIX/Windows.

acceptance criteria -
- На POSIX FS новый/перезаписанный profiles file создается/остается readable/writable только owner-ом (`rw-------`) best-effort, включая temp-file до atomic move.
- Если POSIX permissions недоступны, приложение не падает; ограничение documented как best-effort для MVP.
- Существующий CRUD profiles flow продолжает работать, формат файла не меняется.
- Headless test покрывает POSIX-permission helper там, где окружение поддерживает POSIX file attributes; non-POSIX путь покрыт без platform-specific assumptions.
- `machine/README.md` или related DB import issue note явно фиксирует, что plain-text storage разрешен, но файл пытаемся держать owner-only.

dependencies/risks -
- Depends on completed `049-01-local-connection-profiles-crud.md`.
- Related to `049-03-import-modal-ui-progress-errors.md`, но не дубликат: UI/redaction уже покрыты, здесь только filesystem permission hardening.
- Risk: Windows ACL support сложнее; для MVP достаточно documented best-effort без новой heavy dependency.
