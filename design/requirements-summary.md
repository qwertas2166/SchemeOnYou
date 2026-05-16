# Requirements summary for DB diagram UX

Source context: `requirements/requirements.md`, `requirements/keyboard-only-analysis.md`, `requirements/questions.md`, `requirements/open-source-analysis.md`, `machine/ai-brief.md`, `machine/requirements.json`.

## Product constraints

- Desktop sketch editor, not strict UML/modeling suite.
- Java + JavaFX.
- Visual canvas is primary workspace.
- Keyboard-first, not keyboard-only: mouse is allowed, but all MVP actions must be possible from keyboard.
- IDE-style hardcoded shortcuts.
- Command palette with fuzzy search is the primary command entry.
- MVP DB diagram supports table, column, primary key, foreign key.
- Relations: FK, one-to-one, one-to-many, many-to-many via join table.
- Crow's foot notation is explicitly out of MVP.
- SQL DDL import/export is out of MVP.
- Manual keyboard positioning is not required in MVP.
- Simple predictable auto-layout is acceptable.
- Undo/redo for all mutations.
- Delete is undo-safe; bulk delete asks confirmation.
- Storage is plain text, deterministic, Git-friendly.
- SVG export is in MVP.

## UX implications

- Optimize for developers building schema sketches quickly.
- Avoid mouse-first drag-and-drop dependency.
- Avoid overloading MVP with database modeling suite features.
- Make selection/focus obvious.
- Use command palette for rare commands and direct shortcuts for frequent schema-editing actions.
- Prefer structured mini-dialogs/fuzzy pickers over free-form canvas manipulation.
