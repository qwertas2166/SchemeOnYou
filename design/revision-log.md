# DB diagram design revision log

## Stop criteria

Stop creating new versions if:
- current time reaches 2026-05-10 14:30 Europe/Moscow;
- ideas are exhausted;
- the next version would mostly add complexity and worsen UX;
- only cosmetic changes remain.

## Versions

### v01 — 2026-05-10 13:12 MSK

Initial UI/UX/shortcut layout based on collected requirements.

Key decisions:
- Canvas is visual primary workspace.
- Data entry is keyboard-first through command palette, chords, dialogs, fuzzy pickers.
- Chords: `A T`, `A C`, `A F`, `A J`, `G T`, `G S`.
- DB table creation supports optional constrained multiline columns.
- FK creation uses source/target fuzzy picker instead of mouse targeting.
- Simple deterministic auto-layout is preferred over manual keyboard positioning.

### v02 — 2026-05-10 13:22 MSK

Critiqued v01 and created `db-diagram-ui-ux-v02.md`.

Key fixes:
- Removed `Tab` ambiguity by using `F6`/`Shift+F6` for focus areas and arrow keys for canvas selection.
- Reworked bare letter chords into explicit `Space` canvas command mode to avoid collisions with text input and future type-to-search.
- Replaced multi-step FK wizard with a fast inferred path plus one compact fuzzy-picker dialog.
- Moved layout shortcuts from `Ctrl+L` to `Space L D` / `Space L S` to reduce common shortcut conflicts.
- Clarified relationship hints as metadata only; MVP visual notation remains simple FK lines with no crow's foot.

Pros: safer keyboard model, better discoverability, fewer FK steps, clearer undo-safe compound actions, still within JavaFX/MVP constraints.
Cons: command mode adds one keystroke; `F6` may need onboarding; FK inference must be confirmed clearly.

### v03 — 2026-05-10 13:32 MSK

Critiqued v02 and created `db-diagram-ui-ux-v03.md`.

Key fixes:
- Added explicit table-vs-column selection depth so keyboard users can reliably move inside table cards, edit rows, and create FKs without mouse hit-testing.
- Replaced editable relationship hints with derived relation meaning from FK/PK/unique structure, avoiding inconsistent metadata while preserving one-to-one, one-to-many, and many-to-many intent.
- Clarified `Space` command mode activation and optional hold-to-pan behavior to avoid conflicts with text input and controls.
- Made default `id uuid pk` creation visible via an opt-in checkbox instead of a silent default for empty table creation.
- Added unique/composite constraint handling only where needed for relation semantics and join-table sketches.

Pros: stronger keyboard UX for column-level work, less semantic ambiguity, clearer command-mode behavior, still within MVP constraints.
Cons: selection depth adds a learnable concept; derived relations rely on clear unique/PK indicators; tap-vs-hold Space should be skipped if fragile in JavaFX.

### v04 — 2026-05-10 13:42 MSK

Critiqued v03 and created `db-diagram-ui-ux-v04.md`.

Key fixes:
- Added an explicit `Space M` selection mark so two-table workflows such as Add join table and FK prefill work from the keyboard without arbitrary multi-select complexity.
- Added keyboard FK edge selection via `Space E N` / `Space E P`, plus command-palette/find coverage for FK edges.
- Tightened overloaded `Enter` behavior with a strict visible default-action rule in the status bar.
- Added non-modal undo feedback to compound mutations such as Add FK, Add join table, and relation-affecting deletes.
- Kept relation meaning derived from FK/PK/unique structure and preserved MVP exclusions: no SQL import/export, no crow's foot notation, no required manual keyboard positioning.

Pros: clearer two-object UX, better relation-edge reachability, less surprising default actions, stronger user trust after compound edits.
Cons: mark state adds one learnable concept; edge navigation adds shortcuts; toasts must stay informational and not steal focus.

### v05 — 2026-05-10 13:52 MSK

Critiqued v04 and created `db-diagram-ui-ux-v05.md`.

Key fixes:
- Replaced the generic long-lived selection mark with a narrower `Pin for relation` model to reduce stale hidden-state mistakes.
- Added a relation tray so pinned state remains visible even when the pinned object is off-screen.
- Added automatic pin clearing after successful FK/join-table creation, with explicit `Space U` unpin.
- Simplified FK edge reachability from `Space E N` / `Space E P` to a single `Space E` edge picker command.
- Strengthened compound-action safety by requiring preview dialogs and visible default buttons for FK, join-table, relation-affecting delete, and bulk actions.

Pros: less mode/error risk than v04, clearer relation workflow, fewer edge-selection shortcuts, better visibility of two-object state, still keyboard-first and MVP-bounded.
Cons: relation pin is narrower than arbitrary marking; the relation tray is another UI element; repeated FK creation from one target may require re-pinning; picker-based edge selection is slightly slower for rapid edge cycling.

### v06 — 2026-05-10 14:02 MSK

Critiqued v05 and created `db-diagram-ui-ux-v06.md`.

Key fixes:
- Replaced the dedicated relation tray with a narrower context action bar so relation state, previews, and undo feedback do not compete as separate footer/status surfaces.
- Added explicit FK Source/Target role chips plus a local `Swap` action to reduce accidental reversed foreign-key direction.
- Added bounded safe repeat after confirmed FK creation, letting users reuse the same target through a visible preview flow without blindly repeating mutations.
- Kept new shortcuts local and visible (`X` only in relation preview, `R` only in post-FK feedback) rather than adding more global commands.
- Preserved MVP exclusions: no SQL import/export, no crow's foot notation, no required manual keyboard positioning, no full constraint editor.

Pros: cleaner footer model than v05, safer FK direction handling, less repeated-FK friction, still keyboard-first and MVP-bounded.
Cons: context action bar needs careful priority rules; `Swap` adds preview UI; safe repeat must expire aggressively to avoid becoming hidden state.

### v07 — 2026-05-10 14:12 MSK

Critiqued v06 and created `db-diagram-ui-ux-v07.md`.

Key fixes:
- Added strict priority rules for the single context line so blocking confirmations, active previews, relation pin state, undo feedback, and selection tips do not compete unpredictably.
- Removed the transient `R Repeat` FK shortcut because it could behave like hidden state; replaced it with an explicit `Keep target pinned after create` option in FK previews.
- Upgraded Space command mode into a progressive command sheet that shows chord prefixes, valid next keys, `Backspace`, and invalid-key feedback.
- Kept FK source/target role chips and local `Swap`, preserving direction safety without adding global shortcuts.
- Preserved MVP exclusions: no SQL import/export, no crow's foot notation, no required manual keyboard positioning, no full constraint editor.

Pros: less hidden state, clearer footer behavior, better shortcut learnability, safer repeated FK workflow, still MVP-bounded.
Cons: command sheet is slightly more UI than a bare overlay; keep-pinned checkbox must default off; context-line priority needs disciplined implementation.
