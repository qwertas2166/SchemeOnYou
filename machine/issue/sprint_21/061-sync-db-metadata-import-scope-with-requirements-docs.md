summary - Синхронизировать scope DB metadata import с requirements/design docs
status - done
priority - mid
cost - S

goal - Убрать противоречие между реализованным PostgreSQL metadata import и документами, где активный MVP still lists only “no SQL DDL import/export”.

description - После реализации `049` в продукте появился import таблиц из live PostgreSQL connection через JDBC metadata. При этом `requirements/requirements.md`, `machine/requirements.json`, `design/db-diagram-ui-ux-v07.md`, `design/final-summary.md` и связанные summaries по-прежнему формулируют только исключение `SQL DDL import/export` и не фиксируют новое разрешенное MVP capability как отдельное от DDL. Это создает planning/product drift: будущие проходы могут считать DB import нарушением scope или, наоборот, расширить его до DDL import/export. Нужно явно записать: MVP поддерживает PostgreSQL metadata import from connection, но SQL DDL text import/export остается out of scope.

acceptance criteria -
- Human requirements and machine requirements distinguish `PostgreSQL metadata import from live connection` from excluded `SQL DDL import/export`.
- Design/machine summaries keep DDL import/export excluded, but no longer imply that all DB import is out of MVP.
- `049`/DB import notes link or align with the updated scope wording.
- No new product promise for generic DB vendors, SQL parsing, DDL export, merge import, or installers is introduced.
- `git diff --check` passes for touched docs.

dependencies/risks -
- Depends on completed `sprint_16/049-import-tables-from-database-connection.md` and related DB import slices.
- Related to `sprint_15/054`, `sprint_19/057`, `sprint_20/059`, `sprint_20/060`, but not duplicate: this is documentation/scope synchronization, not import behavior.
- Risk: wording may accidentally widen MVP beyond PostgreSQL metadata import; keep text narrow.

progress - 2026-05-18 20:30 MSK scheduler-2: taken from planned sprint issues because `machine/issue/backlog/` is empty. Synced requirements/design/machine docs and machine summaries to distinguish allowed PostgreSQL live-connection metadata import from excluded SQL DDL text import/export; aligned `049` scope note. `git diff --check` passed.
