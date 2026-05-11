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

## Current state

Updated: 2026-05-11 13:32 MSK.

- Requirements status: draft 0.4, no open questions recorded.
- Design status: DB diagram UX latest recommendation is v07.
- Important refinement: DB diagram v07 uses `F6` / `Shift+F6` for major UI areas and reserves `Tab` / `Shift+Tab` for traversal inside panels/dialogs/pickers. This refines older base requirements that listed `Tab` for focus areas.
- MVP boundaries preserved: no SQL DDL import/export, no crow's foot notation, no required manual keyboard positioning, no configurable keymap/layout, no full DB modeling suite.
- Safety rule: all mutating/compound/destructive operations go through undo-safe commands; FK/join-table/destructive flows require preview or confirmation.

## Current task tracking

See `current/plan.md`, `current/progress.md`, and `current/consistency-check.md`.
