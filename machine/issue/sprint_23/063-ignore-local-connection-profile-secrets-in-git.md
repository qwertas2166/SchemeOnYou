summary - Защитить локальный profiles file от случайного попадания в Git
status - done
priority - high
cost - XS

goal - Закрыть release/privacy gap: plain-text `schemeonyou-connection-profiles.local` не должен случайно попасть в репозиторий при dev/run из корня проекта или portable launcher layout.

description - MVP допускает local plain-text password storage для PostgreSQL connection profiles, а `056` уже добавил best-effort owner-only filesystem permissions. Но `.gitignore` сейчас не содержит `schemeonyou-connection-profiles.local` и temp-вариант, при этом `ConnectionProfileStore.launcherAdjacentProfileFile(...)` размещает файл рядом с launcher path; в dev/portable сценариях это может оказаться внутри checkout. Это отдельный риск от POSIX permissions: файл с секретами может быть случайно добавлен в Git-friendly project/repo workflow. Нужен маленький hardening-slice: игнорировать local profiles file и temp file, добавить release/checklist note, что profiles file не является project artifact.

acceptance criteria -
- `.gitignore` игнорирует `schemeonyou-connection-profiles.local` и `schemeonyou-connection-profiles.local.tmp` независимо от места создания внутри checkout.
- Документация/checklist DB import или release verification явно фиксирует: profiles file local-only, не project JSON/export artifact и не должен коммититься.
- Существующие storage/profile tests не требуют присутствия файла в repo и не меняют формат profiles file.
- `git status --ignored` или эквивалентная manual check показывает, что sample local profiles file игнорируется.

dependencies/risks -
- Depends on completed `sprint_16/049-01-local-connection-profiles-crud.md` and `sprint_18/056-lock-down-local-connection-profile-file-permissions.md`.
- Not a duplicate of `056`: там filesystem permissions; здесь VCS/repo hygiene for secrets.
- Risk: слишком широкий ignore pattern не должен скрывать legitimate test fixtures; если нужны fixtures, хранить их под другим именем без real secrets.


completion notes -
- Added repository ignore rules for `schemeonyou-connection-profiles.local` and `schemeonyou-connection-profiles.local.tmp` so matching files are ignored at any checkout depth.
- Added release verification privacy note: connection profile files are local-only, not project/export artifacts, and must not be committed.
- Verified ignore behavior with `git check-ignore --stdin -v` for root and nested sample paths.
- Verified whitespace with `git diff --check -- .gitignore machine/release-verification.md machine/issue/sprint_23/063-ignore-local-connection-profile-secrets-in-git.md`.
