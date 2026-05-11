# Current machine update plan

Started: 2026-05-11 13:28 MSK
Scope: analyze and structure `requirements/` and `design/`; update `machine/` so it reflects current requirements and design state.

## Work breakdown

1. Inventory sources
   - Read `requirements/requirements.md`, `requirements/questions.md`, `requirements/keyboard-only-analysis.md`.
   - Read `design/final-summary.md`, `design/db-diagram-ui-ux-v07.md`, `design/revision-log.md`.
   - Read existing `machine/*.json` and `machine/ai-brief.md`.

2. Normalize requirements
   - Keep MVP/product/platform/storage/architecture requirements machine-readable.
   - Preserve explicit exclusions and dependency policy.
   - Flag where design v07 refines older requirements text.

3. Normalize design
   - Add machine-readable DB diagram UX state: latest design version, layout, shortcuts, context-line rules, relation pin/FK flow, risks.
   - Connect design decisions back to requirements.

4. Update implementation task structure
   - Split implementation into phases/slices.
   - Make DB diagram design tasks visible after the generic MVP skeleton/domain/canvas layers.

5. Consistency pass
   - Check requirements vs design for conflicts.
   - Record contradictions, resolutions, and follow-up checks.

## Status

- [x] Reminder scheduled for 2026-05-11 13:45 MSK.
- [x] Source inventory completed.
- [x] Machine artifacts updated.
- [x] Initial consistency pass completed; 13:45 follow-up completed.
