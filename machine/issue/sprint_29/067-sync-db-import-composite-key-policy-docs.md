summary - Синхронизировать docs/scope с composite key DB import policy
status - done
priority - high
cost - S

goal - Убрать planning/documentation drift после `059`/`066`: PostgreSQL import теперь поддерживает composite primary keys через table-level constraints, но composite foreign keys для MVP детектируются и пропускаются с warning.

description - После закрытия DB import follow-ups изменилось фактическое поведение metadata import: `066` добавил ordered multi-column PK import в `COMPOSITE_PRIMARY_KEY`, а `059` выбрал MVP degradation path для composite FK — constraint group не превращается в несколько misleading edges, а пропускается с import warning. Human/machine requirements и summaries сейчас в основном говорят об импорте PK/FK generic и о PostgreSQL metadata import vs excluded SQL DDL, но не фиксируют эту важную границу. Нужно точечно синхронизировать requirements/design/machine notes, не расширяя MVP до full grouped FK model.

acceptance criteria -
- `requirements/requirements.md` и/или related machine requirements явно различают single-column FK import, composite PK import и composite FK skipped-with-warning MVP policy.
- `design/final-summary.md` / актуальные machine summaries не обещают full grouped/composite FK import в MVP.
- Notes в `049`/`059`/`066` остаются согласованными: composite PK supported; composite FK degradation documented; SQL DDL import/export всё ещё out of scope.
- Не добавляется новый product promise для generic DB vendors, FK group model/storage, SQL parsing, DDL export или persistent import report.
- `git diff --check` проходит для измененных docs/issue files.

dependencies/risks -
- Depends on completed `sprint_26/059-db-import-composite-foreign-key-policy.md` and `sprint_28/066-import-composite-primary-key-metadata.md`.
- Related to `sprint_21/061-sync-db-metadata-import-scope-with-requirements-docs.md`, but not duplicate: `061` separated live PostgreSQL metadata import from excluded SQL DDL; this task captures the newer composite key behavior boundary.
- Risk: wording may accidentally imply full composite FK support; keep text narrow and explicit about MVP degradation.


progress - 2026-05-19 16:03 MSK: moved from backlog to sprint_29 by scheduler-1 hourly backlog analysis; priority raised to high because 059/066 are done and docs now risk drifting from implemented DB import composite-key behavior.

progress - 2026-05-19 16:30 MSK scheduler-2: taken because it is the only available high-priority S-sized planned item after backlog was emptied by hourly planning. Synchronized `requirements/requirements.md`, `design/final-summary.md`, `machine/requirements.json`, `machine/design.json`, `machine/ai-brief.md`, and `machine/README.md`: single-column FK import remains supported, composite PK import is documented as table-level constraint support, composite FK import is explicitly skipped with warning in MVP. Verification: JSON validation OK; `git diff --check` OK.
