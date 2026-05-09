# AI Brief: SchemeOnYou

## Product

- Desktop sketch editor.
- Java + JavaFX.
- Visual canvas first.
- Keyboard-first, not keyboard-only.
- Mouse allowed, but all MVP actions must work from keyboard.
- MVP is not a strict UML/modeling suite.

## MVP includes

- Linux, Windows, macOS.
- Multiple diagrams per project.
- Sequence diagram.
- Database tables diagram.
- Command palette with fuzzy search.
- IDE-style hardcoded shortcuts.
- Simple hardcoded layout.
- Project save/open.
- Git-friendly text storage.
- Undo/redo.
- SVG export.
- Java JAR/script launch.

## MVP excludes

- Strict UML model.
- Class/use case/activity/state/component/deployment diagrams.
- PlantUML/Mermaid import/export.
- SQL DDL import/export.
- Sequence combined fragments: alt/opt/loop/par.
- Crow's foot ERD.
- Required manual keyboard positioning.
- User-configurable keymap/layout.
- Vim-like mode.
- IDE integration.
- CLI render/export.
- PNG/PDF export.
- Native installers.
- Heavy dependencies without explicit approval.

## Core architecture

- Separate project model, diagram model, canvas/layout state, UI state.
- All mutations go through command layer.
- Command layer supports keyboard invocation, undo/redo, tests, future macros.
- Layout engine is separate; MVP implementation can be simple/hardcoded.

## Storage

- Prefer JSON/YAML.
- Plain text.
- Stable ordering.
- Deterministic serialization.
- Include version, metadata, diagrams, model, canvas state/layout.
- Must be Git-friendly.

## UX rules

- Canvas is primary workspace.
- Command palette is primary command entry.
- Properties panel/inline editor for selected element.
- Selection/focus must be visually obvious.
- No mouse-only actions for MVP features.
- Destructive actions must be undo-safe; bulk delete asks confirmation.

## Open questions

- None currently.
