# Machine-readable project state

AI/Codex-friendly representation of SchemeOnYou requirements, design, and implementation planning.

## Canonical inputs

- `../requirements/requirements.md` — product/MVP/non-MVP requirements.
- `../requirements/questions.md` — resolved decisions.
- `../requirements/keyboard-only-analysis.md` — keyboard-first UX analysis.
- `../requirements/open-source-analysis.md` — open-source analysis notes.
- `../design/final-summary.md` — final DB diagram UX summary.
- `../design/db-diagram-ui-ux-v07.md` — latest recommended DB diagram design.

## Machine outputs

- `requirements.json` — structured requirements plus explicit DB design refinements.
- `design.json` — structured DB diagram UX/design state, latest version v07.
- `tasks.json` — phased implementation plan derived from requirements + design.
- `ai-brief.md` — compact human/agent context.
- `current/` — local plan and progress for the current analysis/update task.
- `release-verification.md` — canonical local Java/Maven build, test, package, and fallback verification contract.

## Current state

Updated: 2026-05-11 13:32 MSK.

- Requirements status: draft 0.4, no open questions recorded.
- Design status: DB diagram UX latest recommendation is v07.
- Important runtime refinement: current app navigation uses `0` top menu, `1` left menu, `2` canvas, `3` inspector for major UI areas, and reserves `Tab` / `Shift+Tab` for traversal inside panels/dialogs/pickers. The earlier DB diagram v07 `F6` / `Shift+F6` cycle is retained as replaced historical design context, not the active shortcut model.
- MVP boundaries preserved: PostgreSQL metadata import from live connection is allowed; SQL DDL text import/export, crow's foot notation, required manual keyboard positioning, configurable keymap/layout, full grouped/composite FK import/model, and full DB modeling suite are out of scope. DB import supports single-column FK edges and composite PK table-level constraints; composite FK metadata groups are skipped with warning in MVP.
- Safety rule: all mutating/compound/destructive operations go through undo-safe commands; FK/join-table/destructive flows require preview or confirmation.
- Release/dev tooling: F12 key log is dev-only; default/release runs do not show it in footer/F1/ShortcutMap. Enable only explicitly with `-Dschemeonyou.debug.keyLog=true` or `SCHEMEONYOU_DEBUG_KEY_LOG=true`.
- DB import MVP: local connection profiles intentionally remain launcher-adjacent and plain-text, outside project JSON/export; profile writes best-effort restrict the local file/temp file to owner-only permissions on POSIX filesystems, while non-POSIX/Windows ACL hardening is documented as out of MVP scope.

## Current task tracking

See `current/plan.md`, `current/progress.md`, and `current/consistency-check.md`.
