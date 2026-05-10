# DB diagram UI/UX/Shortcut layout — v06

## Revision focus

v06 keeps the v05 MVP: a keyboard-first JavaFX desktop sketch editor for database diagrams with table, column, PK, FK, join-table support, deterministic auto-layout, undo/redo, Git-friendly text storage, and SVG export.

The v05 direction is sound, but three weak spots remain:

1. **The relation tray risks becoming a second status bar.** It solves hidden pin state, but it can also add persistent visual noise and split attention from the canvas/status line.
2. **FK direction can still be confused.** Developers often think in either direction: “orders belongs to users” or “users has many orders”. A selected/pinned pair alone does not always make source/target obvious.
3. **Preview dialogs can become repetitive.** They are important for compound mutations, but if every FK creation feels like a modal speed bump, keyboard-first flow suffers.

v06 improves this by replacing the always-described relation tray with a narrower **context action bar**, adding explicit **source/target role chips with Swap**, and introducing **safe repeat** for confirmed relation commands. This keeps v05’s safety while reducing clutter and confirmation fatigue.

No SQL import/export, crow's foot notation, required manual keyboard positioning, or full modeling-suite constraint editor is added.

## Goal

Fast keyboard-first construction of database table diagrams on a visual canvas, optimized for developers sketching schemas quickly.

The canvas is the primary workspace. Mouse actions may exist as conveniences, but every MVP action must be reachable from the keyboard.

## Main screen layout

```text
┌────────────────────────────────────────────────────────────────────────────┐
│ Top bar: Project · Active diagram · Dirty state · Zoom · Command hint      │
├───────────────┬──────────────────────────────────────────┬─────────────────┤
│ Project tree  │ Canvas                                   │ Inspector       │
│ - diagrams    │                                          │ selected object │
│ - tables      │   ┌ users ──────────────┐  ┌ orders ───┐ │ table/column/FK │
│               │   │ 🔑 id        uuid   │──│ user_id → │ │ validation      │
│               │   │ !◇ email     text   │  └───────────┘ │ quick actions   │
├───────────────┴──────────────────────────────────────────┴─────────────────┤
│ Context action bar: Source orders.user_id · Target users.id · Space A F     │
│ Status: Canvas · Enter edit · Space commands · F1 help                     │
└────────────────────────────────────────────────────────────────────────────┘
```

Primary areas:

1. Project tree.
2. Canvas.
3. Inspector/properties panel.
4. Context action bar/status strip.
5. Transient command palette, pickers, and dialogs.

The **context action bar** is not a permanent extra tray. It appears only when one of these is true:

- a relation pin exists;
- a relation command is active;
- a compound preview is awaiting confirmation;
- a high-impact undoable action just completed and exposes `Undo Ctrl+Z`.

When none of those apply, the footer collapses to a single ordinary status line.

## Focus, selection, and selection depth

v06 keeps v05's separation between application focus, diagram selection, and table/column selection depth.

### Application focus

- `F6` / `Shift+F6`: cycle project tree → canvas → inspector → back.
- `Tab` / `Shift+Tab`: normal traversal inside the focused panel, dialog, or picker.
- Canvas object navigation never uses plain `Tab`.

### Diagram selection

The canvas always shows:

- one keyboard focus ring around the active canvas;
- one primary selected object;
- optional relation pin badge on one table/column;
- selected FK edge highlight when an edge is selected;
- breadcrumb in the status area, e.g. `orders > user_id > FK to users.id`.

### Selection depth

A table card has two keyboard depths:

1. **Table depth**: table card selected as an object.
2. **Column depth**: one row inside the selected table selected.

| Shortcut | Context | Action |
|---|---|---|
| Arrow keys | Table depth | Move spatially to nearest table/edge in that direction |
| `Enter` | Table selected | Enter table and select first meaningful column, usually PK |
| `Up` / `Down` | Column depth | Previous/next column in same table |
| `Left` / `Right` | Column depth | Move to nearest related table via FK if available; otherwise leave table spatially |
| `Esc` | Column depth | Return selection to parent table |
| `Alt+Left` / `Alt+Right` | Canvas | Selection history back/forward |

## Context action bar instead of a persistent relation tray

v05's relation tray made pin state visible, which was correct. v06 narrows it into a more general **context action bar** so the footer does not become two competing status areas.

Examples:

```text
Pinned target: users.id · Select source column, Space A F create FK · Space U unpin
Source: orders.user_id · Target: users.id · Enter Create FK · X Swap · Esc Cancel
Created FK orders.user_id → users.id · Undo Ctrl+Z · R Repeat with same target
Pinned object incompatible · Enter choose target · Space U unpin
```

Rules:

- The context action bar is short, single-line by default, and never steals focus.
- If the content would overflow, it opens a compact details popover with `Enter` or `F1`, not a taller permanent footer.
- It may contain action hints, but only for the current selection or active command.
- Toasts and relation pin messages use the same footer area, preventing duplicate feedback channels.

Why this is better than v05: the relation state remains visible, but the UI does not add a new always-present strip just for one workflow.

## Relation pin, source/target roles, and Swap

A relation pin remains **Pin for next relation**, not arbitrary multi-select.

| Shortcut | Context | Action |
|---|---|---|
| `Space`, `P` | Table/column selected | Pin selected object for next relation command |
| `Space`, `U` | Canvas | Unpin relation object |
| `Space`, `A F` | Table/column selected with optional pin | Add foreign key |
| `Space`, `A J` | Table selected with optional pinned table | Add join table |
| `X` | Relation preview/dialog | Swap source and target where valid |

Pin lifetime rules:

- A pin is cleared automatically after a successful FK or join-table creation, unless the user chooses safe repeat.
- A pin is cleared if its object is deleted.
- A pin survives ordinary navigation so the user can move across the canvas.
- If a command uses a stale or incompatible pin, the dialog explains the issue and offers `Use selected only`, `Choose other object`, or `Cancel`.
- `Esc` does not clear a pin, because users often press `Esc` to leave column depth; `Space U` is the explicit unpin.

### Source/target role chips

Whenever an FK is being created or previewed, endpoints are shown as explicit role chips:

```text
Create foreign key
Source column: [orders.user_id]  →  Target column: [users.id]
Meaning: many orders rows can reference one users row

[Create FK] [Swap] [Change source...] [Change target...] [Cancel]
```

Rules:

- `Source` always means the column that stores the FK.
- `Target` always means the referenced table/column.
- If the selected/pinned pair can be interpreted both ways, the preview must show both options or pick the stronger inferred option and highlight `Swap`.
- `Swap` is disabled if swapping would create an invalid source, but it still explains why.

This directly fixes a common diagramming mistake: creating the FK in the opposite direction.

## Safe repeat for relation commands

Preview dialogs remain mandatory for compound/high-impact actions, but v06 adds a bounded repeat path so repeated FK creation is not tedious.

After successfully creating an FK from a confirmed preview:

```text
Created FK orders.user_id → users.id · Undo Ctrl+Z · R Repeat with target users.id
```

If the user presses `R` while the feedback is visible:

- the previous target is temporarily reused as the relation pin;
- the user chooses or creates the next source column through the same source picker;
- a preview is still shown before creating the next FK;
- the repeat state expires after navigation away, timeout, another mutation, or `Esc`.

This does **not** create an unsafe “repeat last command blindly” feature. It only saves the re-pin step for a common workflow such as adding several `*_id` references to the same table.

## Canvas navigation

When canvas has focus:

| Shortcut | Action |
|---|---|
| Arrow keys | Move selection according to current selection depth |
| `Ctrl+Arrow` | Pan viewport |
| `Home` | Select first table in deterministic layout order |
| `End` | Select last table in deterministic layout order |
| `Ctrl+Plus` / `Ctrl+Minus` | Zoom in/out |
| `Ctrl+0` | Fit whole diagram to view |
| `Ctrl+1` | Actual size / 100% zoom |
| `Alt+Left` / `Alt+Right` | Selection history back/forward |

Mouse wheel and trackpad pan/zoom may exist. Holding `Space` for temporary mouse-pan is optional and must be disabled while text fields, dialogs, pickers, or overlays are active.

## Command entry model

There are two command paths:

1. Command palette for all commands.
2. Canvas command mode for fast diagram-editing chords.

### Command palette

- `Ctrl+Shift+P`: open command palette.
- `Ctrl+K Ctrl+P`: optional IDE-style alternate chord.
- Fuzzy search supports commands and small structured arguments.

### Canvas command mode

When canvas has focus and no editor/dialog is active:

- tap `Space`: open command-mode overlay near selection;
- type a chord while overlay is open;
- `Esc` closes overlay;
- `Space` in text fields, inspector controls, palette search, and dialogs remains normal input.

Overlay example:

```text
Canvas commands
A T  Add table        A C  Add column
A F  Add FK           A J  Add join table
P    Pin relation     U    Unpin
G T  Go to target     G S  Go to source
E    Select FK edge   L D  Layout diagram
L S  Layout cluster   ?    Show shortcuts
Esc  Close
```

## Shortcut layout v06

| Shortcut | Context | Action |
|---|---|---|
| `Ctrl+Shift+P` | Global | Command palette |
| `F6` / `Shift+F6` | Global | Next/previous major area |
| `Ctrl+F` | Global/canvas | Find table/column/FK |
| `F1` | Global | Shortcut overlay |
| `Space`, `A T` | Canvas command mode | Add table |
| `Space`, `A C` | Table/column selected | Add column to selected table |
| `Space`, `A F` | Table/column selected, optional relation pin | Add foreign key |
| `Space`, `A J` | Table selected, optional pinned table | Add join table |
| `Space`, `P` | Table/column selected | Pin selected object for next relation |
| `Space`, `U` | Canvas | Clear relation pin |
| `Enter` | Selection/dialog | Perform visible safe default action, or open action menu |
| `F2` | Selection | Rename selected table/column |
| `Delete` | Selection | Delete selected, undo-safe |
| `Space`, `E` | Canvas | Select FK edge attached to selection, or open FK edge picker |
| `Space`, `G T` | FK column/edge selected | Go to referenced target |
| `Space`, `G S` | FK edge/target selected | Go to source; if multiple, open picker |
| `Space`, `L D` | Canvas | Layout whole diagram |
| `Space`, `L S` | Selection | Layout selected cluster |
| `Space`, `?` | Canvas command mode | Show shortcut overlay |
| `X` | Relation preview | Swap FK source/target when valid |
| `R` | Post-FK feedback | Repeat with same target through safe preview flow |

`X` and `R` are local shortcuts only when the relation preview or post-action feedback explicitly shows them. They are not global hidden modes.

## Default action and confirmation rules

`Enter` remains context-sensitive, but only when the visible default action is safe or explicitly selected.

Safe default actions may happen directly if visible in the status bar:

```text
Selected: users table · Enter: enter columns · F2 rename · Space A C add column
Selected: users.email · Enter: edit column · Esc back to table · Space A F add FK
Selected: FK orders.user_id → users.id · Enter: edit FK · Space G T go target
```

Committing compound actions must show a preview with an explicit default button:

- Add FK.
- Add join table.
- Delete table/column with relations.
- Bulk delete.
- Any action that creates multiple objects at once.

If the status/default action is ambiguous, `Enter` opens a small action menu instead of guessing.

## Primary DB workflows

### 1. Create a DB diagram

- `Ctrl+Shift+P` → `New database diagram`.
- Dialog asks for diagram name.
- New blank canvas opens.

### 2. Add table

Shortcut: `Space`, `A T` with canvas focused.

Dialog:

```text
Add table
Name: users
Columns optional, one per line:
id uuid pk
email text not null unique
created_at timestamp not null
[ ] Create default id uuid pk if columns are empty
```

Constrained parser:

```text
<name> <type> [pk] [not null|null] [unique]
```

Rules:

- Unsupported tokens show inline errors and do not silently create fields.
- Empty column list is allowed only if the default-id checkbox is enabled.
- The table and parsed columns are created as one undoable command.
- Auto-layout places the table near current selection or in the next free grid slot.
- Selection moves to the new table.
- Footer confirms: `Created table users · Undo Ctrl+Z`.

### 3. Add column

Shortcut: `Space`, `A C` when table or one of its columns is selected.

Dialog:

```text
Add column to users
Name: email
Type: text
Nullable: No
Primary key: No
Unique: No
```

Fast structured palette input:

```text
Add column users.email text not null unique
```

Selection moves to the created column. If column depth was active, the new column is inserted after the selected row unless the user changes position in the dialog.

### 4. Edit selected

- Table selected + `Enter`: enter column depth.
- Table selected + `F2`: rename table.
- Column selected + `Enter`: inspector focuses column name field.
- FK edge selected + `Enter`: inspector focuses FK summary/target picker.

Commit/revert:

- `Ctrl+Enter`: apply current inspector/dialog edit.
- `Esc`: revert current field edit, close transient UI, or move from column depth back to table depth.
- Every accepted mutation is undoable.

### 5. Add foreign key

Shortcut: `Space`, `A F`.

#### Fast path from selected column

If a column is selected and its name looks like `<table>_id` or `<singular_table>_id`:

1. Suggest matching target tables first.
2. Default target column to target table primary key.
3. Show source/target chips and derived meaning.

```text
Create foreign key
Source column: orders.user_id
Target column: users.id
Meaning: many orders rows can reference one users row

[Create FK] [Swap] [Change target...] [Cancel]
```

`Enter` activates `Create FK` only because the dialog default button is visibly selected.

#### Fast path using relation pin

If `users.id` is pinned and `orders.user_id` is selected, `Space A F` opens the same preview with both endpoints prefilled.

If a table is pinned and another table/column is selected, the source-column picker starts with likely create rows:

```text
+ Create orders.user_id uuid → users.id
+ Create orders.user_uuid uuid → users.id
```

Choosing a create row creates the source column and FK in one undoable compound command after preview.

After successful creation, the pin clears automatically unless the user presses `R` to repeat with the same target.

#### General path

If no clear source/target exists:

```text
Create foreign key
Source table:  orders
Source column: user_id
Target table:  users
Target column: id
Uniqueness:    source column is not unique
Meaning:       many orders rows can reference one users row

[Create FK] [Swap] [Cancel]
```

Each source/target row is a fuzzy picker. `Tab` moves rows; typing filters the active picker.

### 6. FK edge selection and relation navigation

FK edges are selectable without requiring next/previous edge shortcuts.

| Command | Behavior |
|---|---|
| `Space`, `E` | If one FK edge is attached to selection, select it. If several exist, open attached-edge picker. If none exist, open all-edge picker. |
| `Ctrl+F` → `fk ...` | Fuzzy-select any FK edge. |
| `Space`, `G T` | From FK source/edge, select target column/table. |
| `Space`, `G S` | From FK edge/target, select source; if multiple, open picker. |

Edge picker example:

```text
Select FK edge for orders
> orders.user_id → users.id
  orders.coupon_id → coupons.id
  order_items.order_id → orders.id
```

### 7. Relation meaning in MVP

Relationship labels are derived from structure, not edited as separate metadata.

- FK source column not unique → many source rows can reference one target row.
- FK source column unique → at most one source row can reference one target row.
- Join table with two FKs and a combined uniqueness/PK rule → many-to-many sketch.

Visual rendering remains simple directional FK lines. The inspector may display derived text, but it is not separate editable truth.

### 8. Add join table

Shortcut: `Space`, `A J`.

Best keyboard flow:

1. Select `users`.
2. `Space`, `P` to pin it for relation.
3. Find or arrow to `roles`.
4. `Space`, `A J`.

Preview dialog:

```text
Add join table
Left table:  users
Right table: roles
Name:       user_roles
Columns:
  user_id uuid -> users.id
  role_id uuid -> roles.id
Constraint:
  primary key (user_id, role_id)

[Create join table] [Swap left/right] [Change...] [Cancel]
```

Behavior:

- Creates join table, two columns, composite PK/unique marker, and two FK edges as one undoable command.
- Default name is deterministic: `<singular_left>_<plural_or_singular_right>` if available, otherwise `<left>_<right>`.
- If a name conflict exists, suggest `_map` or `_2`, but do not silently rename.
- If no pin exists, the dialog opens with two fuzzy table pickers.
- After successful creation, the pin clears automatically.
- Visual rendering is still simple FK lines; no crow's foot notation.

The `Swap left/right` option is mainly for naming and column order; it does not change the derived many-to-many meaning.

### 9. Find/select

- `Ctrl+F`: fuzzy find tables, columns, and FK edges.
- Result examples:
  - `table users`
  - `column users.email`
  - `fk orders.user_id -> users.id`
- `Enter`: select and center result.
- `Alt+Left` / `Alt+Right`: return through selection history.

For dense diagrams, `Ctrl+F` remains the reliable escape hatch when spatial navigation is noisy.

### 10. Layout

Manual keyboard positioning is not required in MVP.

Layout commands:

- `Space`, `L D`: layout whole diagram.
- `Space`, `L S`: layout selected cluster.
- Command palette: `Layout diagram`, `Layout selected cluster`.

Deterministic rules:

1. Tables with no outgoing FKs are placed in the first layer.
2. Tables depending on them are placed in later layers.
3. Join tables are placed between their two main related tables when possible.
4. Cycles are broken deterministically by stable table id, then name.
5. Within a layer, order by stable table id, then name.
6. Existing mouse positions may be persisted as soft hints, but `Layout diagram` can normalize the canvas.

This keeps SVG/export diffs stable and avoids required manual keyboard positioning.

## Visual object design

Table card:

```text
┌ users ───────────────┐
│ 🔑 id          uuid  │
│ !◇ email       text  │
│ →  role_id     uuid  │
└──────────────────────┘
```

Markers:

- `🔑` primary key.
- `!` not null.
- `◇` unique.
- `→` foreign-key source column.
- `📌` pinned for next relation command.

FK edges:

- Simple directional line from source table/column to target table/column.
- Label may show `orders.user_id → users.id` when selected or hovered.
- Selected edge gets a high-contrast stroke and endpoint dots.
- No crow's foot notation in MVP.

## Inspector/properties panel

Table selected:

```text
Table
Name: users
Columns: 4
Incoming FKs: 2
Outgoing FKs: 1
[Add column] [Add FK] [Pin relation] [Rename]
```

Column selected:

```text
Column
Table: users
Name: email
Type: text
Nullable: false
Primary key: false
Unique: true
Foreign key: none
[Add FK] [Pin relation] [Rename]
```

FK selected:

```text
Foreign key
Source: orders.user_id
Target: users.id
Derived relation: many-to-one from orders to users
[Go source] [Go target] [Delete FK]
```

Inspector keyboard behavior:

- `Tab` / `Shift+Tab`: field traversal.
- `Enter`: edit focused field or press focused button.
- `Ctrl+Enter`: apply.
- `Esc`: revert current field edit.

## Command palette command set

- New database diagram
- Add table
- Add column
- Add foreign key
- Add join table
- Pin for relation
- Clear relation pin
- Rename selected
- Edit selected
- Delete selected
- Find table/column/FK
- Select FK edge
- Go to FK target
- Go to FK source
- Layout diagram
- Layout selected cluster
- Fit diagram
- Export SVG
- Show shortcuts

Commands with arguments support small structured text:

```text
Add table users
Add column users.email text not null unique
Add foreign key orders.user_id users.id
Add join table users roles
```

The structured input remains intentionally small and must not grow into hidden SQL/DDL import.

## Validation, undo, and feedback UX

- Duplicate table name: inline warning, suggest deterministic alternative, but do not auto-rename silently.
- Duplicate column name in table: block create/apply until resolved.
- Invalid multiline column token: highlight exact token and show accepted grammar.
- FK target missing primary key: allow choosing any column, warn clearly.
- FK source/target type mismatch: warn but allow, because this is a sketch editor.
- Incompatible relation pin: warn in context action bar and relation dialog; never silently use it.
- Possible reversed FK direction: show source/target chips and `Swap` before create.
- Deleting a column with FK edges: confirmation lists affected relations.
- Deleting a table with FK edges: confirmation lists affected columns/relations.
- Bulk delete: confirmation required.
- All delete/create/edit actions are undo-safe.

After every compound mutation, show a short non-modal footer message:

```text
Created join table user_roles with 2 FKs · Undo Ctrl+Z
Created FK orders.user_id → users.id · Undo Ctrl+Z · R Repeat with target users.id
Deleted column users.email and 1 FK · Undo Ctrl+Z
```

The footer does not steal focus. `Ctrl+Z` remains the real undo mechanism.

## Onboarding and discoverability

First empty canvas:

```text
Press Space A T to add a table
Press Ctrl+Shift+P for all commands
Press F1 for shortcuts
Press Enter to drill into a selected table
```

When a table or column is selected and no pin exists:

```text
Tip: Space P pins this object for the next FK or join-table command
```

When a pin exists:

```text
Pinned users.id · select a source column and press Space A F, or Space U to unpin
```

`F1` opens a compact shortcut overlay grouped by:

- focus/navigation;
- relation pin and two-object actions;
- table/column editing;
- FK edge and relation navigation;
- layout/export.

## Pros

- Keeps v05's relation pin but avoids a persistent extra relation tray by folding it into a contextual footer.
- Makes FK direction harder to get wrong through explicit Source/Target chips and a local `Swap` action.
- Reduces repeated-FK friction with safe repeat while still requiring preview before each structural mutation.
- Keeps shortcut additions local and visible (`X` in preview, `R` in post-action feedback), avoiding new global hidden modes.
- Preserves MVP constraints and keyboard-first operation.

## Cons / risks

- The context action bar now carries multiple feedback roles; implementation must prevent message churn and priority conflicts.
- `Swap` adds one more visible control to relation previews, though it addresses a high-cost mistake.
- Safe repeat could feel like hidden state if the footer disappears too slowly; it should expire aggressively and be visibly tied to the last created FK.
- The design still depends on clear visual indicators for selected object, pinned object, and FK direction.

## Why this is better than v05

v05 fixed hidden relation state but did so by adding a dedicated relation tray and still left FK orientation somewhat implicit. v06 keeps the safety benefits while simplifying the chrome: one context action bar handles pin state, previews, and undo feedback. It also makes FK creation safer through source/target chips and `Swap`, then restores speed through bounded safe repeat. The result is cleaner, less error-prone, and still clearly within MVP scope.
