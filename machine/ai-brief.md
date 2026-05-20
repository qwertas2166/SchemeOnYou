# AI Brief: SchemeOnYou

Updated: 2026-05-11 13:32 MSK

## Product

- Desktop sketch editor for developers.
- Java + JavaFX.
- Visual canvas first.
- Keyboard-first, not keyboard-only.
- Mouse allowed, but all MVP actions must work from keyboard.
- MVP is a fast sketch editor, not a strict UML/modeling suite.

## MVP includes

- Linux, Windows, macOS.
- Multiple diagrams per project.
- Sequence diagram.
- Database tables diagram.
- PostgreSQL metadata import from live connection for DB diagrams: tables, columns, single-column FKs, and PK metadata; composite PK becomes a table-level constraint, composite FK is skipped with an import warning in MVP.
- Command palette with fuzzy search.
- IDE-style hardcoded shortcuts.
- Simple deterministic layout.
- Project save/open.
- Git-friendly text storage.
- Undo/redo for mutating operations.
- SVG export.
- Java JAR/script launch.

## MVP excludes

- Strict UML model.
- Class/use case/activity/state/component/deployment diagrams.
- PlantUML/Mermaid import/export.
- SQL DDL text import/export.
- Full grouped/composite foreign-key import/model; MVP skips composite FK metadata groups with warnings instead of creating misleading per-column edges.
- Sequence combined fragments: alt/opt/loop/par.
- Crow's foot ERD notation.
- Required manual keyboard positioning.
- User-configurable keymap/layout.
- Vim-like mode.
- IDE integration.
- CLI render/export.
- PNG/PDF export.
- Native installers.
- Full database modeling-suite constraint editor.
- Heavy dependencies without explicit approval.

## Current DB diagram design

Canonical design: `design/db-diagram-ui-ux-v07.md`.

Key decisions:

- Main screen: top bar, project tree, canvas, inspector/properties panel, single context line + status hint.
- Current runtime major-area navigation is direct: `0` top menu, `1` left menu, `2` canvas, `3` inspector.
- `Tab` / `Shift+Tab` traverse inside panels/dialogs/pickers, not between major areas or canvas objects.
- Canvas navigation uses arrows, selection depth, `Enter` to enter table columns, `Esc` to return to table depth.
- `Space` opens progressive command sheet on canvas.
- Main active DB chords: `Space A T`, `Space A C`, `Space A F`, `Space A J`, `Space P`, `Space U`, `Space E` (edit selected), `Space G S` (find element), `Space L D` (layout diagram). Future/non-MVP: `Space G T`, `Space L S`, FK edge picker on a non-conflicting chord.
- Relation pin is explicit and visible; `Keep target pinned after create` exists only as an FK-preview checkbox and defaults off.
- FK creation uses explicit Source/Target role chips and local `X Swap` while preview is visible.
- Relation meaning is derived from FK + PK/unique/composite constraints, not independent editable relationship metadata. DB import currently supports composite PK constraints but not grouped composite FK constraints.
- Join table is one undoable compound action.
- Visual notation stays simple: table cards, markers, directional FK lines; no crow's foot notation in MVP.

## Core architecture

- Separate project model, diagram model, canvas/layout state, UI state.
- All mutations go through command layer.
- Command layer supports keyboard invocation, undo/redo, tests, future macros/scripting.
- Layout engine is separate; MVP implementation can be simple/deterministic.

## Storage

- Prefer JSON/YAML.
- Plain text.
- Stable ordering.
- Deterministic serialization.
- Include version, metadata, diagrams, model, canvas state/layout.
- Must be Git-friendly.

## UX and safety rules

- Canvas is primary workspace.
- Command palette is global command entry; Space command sheet is fast DB diagram command entry.
- Properties panel/inline editor edits selected element.
- Selection, focus, relation pin, FK direction and Source/Target roles must be visually obvious.
- No mouse-only actions for MVP features.
- Destructive and compound actions must be undo-safe.
- Bulk delete and relation-affecting delete require confirmation.
- `Enter` only performs a visible safe default action; ambiguous cases open an action menu.

## Open questions

- None currently recorded.

## Machine files

- `machine/requirements.json` — requirements + design refinements.
- `machine/design.json` — DB diagram v07 structured design state.
- `machine/tasks.json` — phased implementation plan.
- `machine/current/` — active analysis plan/progress/check log.
