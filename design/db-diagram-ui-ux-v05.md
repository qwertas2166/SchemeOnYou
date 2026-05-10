# DB diagram UI/UX/Shortcut layout — v05

## Revision focus

v05 keeps the v04 MVP shape: a keyboard-first JavaFX desktop sketch editor for database diagrams with table, column, PK, FK, join-table support, deterministic auto-layout, undo/redo, Git-friendly text storage, and SVG export.

The main v04 weakness is not missing power; it is **too much hidden state around marks, edge navigation, and context-sensitive Enter**. These features are useful, but if they are not bounded, they can create mode errors:

- a marked object can linger and accidentally affect the next FK/join command;
- `Space Shift+M` is awkward and easy to miss;
- edge navigation is useful but exposes a separate object type before users understand why;
- status-bar-only default actions may not be visible enough for high-impact commands;
- two-table workflows still rely on remembering an abstract mark rather than seeing a command-specific pairing.

v05 improves this by replacing the generic long-lived mark with a clearer **Pin for next relation** model, adding a tiny **relation tray**, and requiring confirmation previews for all compound relation commands. It does not add crow's foot notation, SQL import/export, required manual keyboard positioning, or a modeling-suite constraint system.

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
│ Relation tray: Pinned users.id · Selected orders.user_id · Space A F        │
│ Status: Canvas · Enter edit · Space commands · F1 help                     │
└────────────────────────────────────────────────────────────────────────────┘
```

Primary areas:

1. Project tree.
2. Canvas.
3. Inspector/properties panel.
4. Relation tray/status strip.
5. Transient command palette, pickers, and dialogs.

The relation tray is only visible when a relation pin exists or when a relation command is active.

## Focus, selection, and selection depth

v05 preserves v04's separation between application focus, diagram selection, and table/column selection depth.

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

## Relation pin and relation tray

v04's generic mark becomes a narrower, safer concept: **Pin for next relation**.

A relation pin can be set on a table or column. It exists only to help the next FK or join-table command. It is not arbitrary multi-select.

| Shortcut | Context | Action |
|---|---|---|
| `Space`, `P` | Table/column selected | Pin selected object for next relation command |
| `Space`, `U` | Canvas | Unpin relation object |
| `Space`, `A F` | Table/column selected with optional pin | Add foreign key |
| `Space`, `A J` | Table selected with optional pinned table | Add join table |

Pin lifetime rules:

- A pin is cleared automatically after a successful FK or join-table creation.
- A pin is cleared if its object is deleted.
- A pin survives ordinary navigation so the user can move across the canvas.
- If a command uses a stale or incompatible pin, the dialog shows the issue and offers `Use selected only`, `Choose other object`, or `Cancel`.
- `Esc` does not clear a pin, because users often press `Esc` to leave column depth; `Space U` is the explicit unpin.

Relation tray examples:

```text
Pinned for relation: users.id    Selected: orders.user_id    Space A F: create FK
Pinned for relation: users       Selected: roles             Space A J: create join table
Pinned object incompatible with selected FK command · Space U unpin
```

Why this is better than v04's mark: the feature name explains the intent, the tray makes state visible even when the pinned object is off-screen, and automatic clearing reduces accidental reuse.

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

v05 deliberately changes edge selection from `E N` / `E P` to a single `E` command that opens a tiny edge picker when needed. This reduces shortcut burden while keeping keyboard reachability.

## Shortcut layout v05

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
| `Enter` | Selection | Perform visible safe default action, or open action menu |
| `F2` | Selection | Rename selected table/column |
| `Delete` | Selection | Delete selected, undo-safe |
| `Space`, `E` | Canvas | Select FK edge attached to selection, or open FK edge picker |
| `Space`, `G T` | FK column/edge selected | Go to referenced target |
| `Space`, `G S` | FK edge/target selected | Go to source; if multiple, open picker |
| `Space`, `L D` | Canvas | Layout whole diagram |
| `Space`, `L S` | Selection | Layout selected cluster |
| `Space`, `?` | Canvas command mode | Show shortcut overlay |

## Default action and confirmation rules

`Enter` remains context-sensitive, but v05 divides actions into safe and committing actions.

Safe default actions may happen directly if visible in the status bar:

```text
Selected: users table · Enter: enter columns · F2 rename · Space A C add column
Selected: users.email · Enter: edit column · Esc back to table · Space A F add FK
Selected: FK orders.user_id → users.id · Enter: edit FK · Space G T go target
```

Committing compound actions must show a preview dialog with an explicit default button:

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
- Toast confirms: `Created table users · Undo Ctrl+Z`.

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
3. Show a preview of the structural meaning derived from constraints.

```text
Create foreign key
Source: orders.user_id
Target: users.id
Derived: many orders rows can reference one users row

[Create FK] [Change target...] [Cancel]
```

`Enter` activates `Create FK` only because the dialog default button is visibly selected.

#### Fast path using relation pin

If `users.id` is pinned and `orders.user_id` is selected, `Space A F` opens the same preview with both endpoints prefilled.

If a table is pinned and another table/column is selected, the source-column picker starts with likely create rows:

```text
+ Create orders.user_id uuid → users.id
+ Create orders.user_uuid uuid → users.id
```

Choosing a create row creates the source column and FK in one undoable compound command.

After successful creation, the pin clears automatically.

#### General path

If no clear source/target exists:

```text
Create foreign key
Source table:  orders
Source column: user_id
Target table:  users
Target column: id
Uniqueness:    source column is not unique
Derived:       many-to-one from orders to users

[Create FK] [Cancel]
```

Each source/target row is a fuzzy picker. `Tab` moves rows; typing filters the active picker.

### 6. FK edge selection and relation navigation

FK edges are selectable, but v05 avoids making users memorize next/previous edge shortcuts.

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

This preserves keyboard reachability while reducing shortcut surface area.

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

[Create join table] [Change...] [Cancel]
```

Behavior:

- Creates join table, two columns, composite PK/unique marker, and two FK edges as one undoable command.
- Default name is deterministic: `<singular_left>_<plural_or_singular_right>` if available, otherwise `<left>_<right>`.
- If a name conflict exists, suggest `_map` or `_2`, but do not silently rename.
- If no pin exists, the dialog opens with two fuzzy table pickers.
- After successful creation, the pin clears automatically.
- Visual rendering is still simple FK lines; no crow's foot notation.

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
- Incompatible relation pin: warn in relation tray and relation dialog; never silently use it.
- Deleting a column with FK edges: confirmation lists affected relations.
- Deleting a table with FK edges: confirmation lists affected columns/relations.
- Bulk delete: confirmation required.
- All delete/create/edit actions are undo-safe.

After every compound mutation, show a short non-modal toast:

```text
Created join table user_roles with 2 FKs · Undo Ctrl+Z
Created FK orders.user_id → users.id · Undo Ctrl+Z
Deleted column users.email and 1 FK · Undo Ctrl+Z
```

The toast does not steal focus. `Ctrl+Z` remains the real undo mechanism.

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

- Replaces v04's generic mark with a clearer relation-specific pin, reducing accidental mode errors.
- Adds a relation tray so off-screen pinned state is visible and understandable.
- Automatically clears pins after successful relation commands, preventing stale selections from affecting future work.
- Simplifies FK edge navigation from two next/previous shortcuts to one picker-oriented command.
- Keeps compound relation actions explicit through preview dialogs and visible default buttons.
- Preserves MVP scope and keyboard-first operation without adding modeling-suite features.

## Cons / risks

- `Pin for relation` is slightly narrower than v04's generic mark, so future arbitrary multi-object commands would need a separate design.
- The relation tray adds one more UI strip; it should appear only when useful to avoid visual clutter.
- `Space E` with a picker is one step slower than `E N` / `E P` for users repeatedly cycling edges.
- Auto-clearing pins after successful commands is safer, but users creating several related FKs from the same target may need to pin again.
- The preview dialogs add confirmation friction, but only for compound or relation-changing actions where mistakes are costly.

## Why this is better than v04

v04 made two-object workflows and FK edge selection reachable, but its generic mark and extra edge shortcuts could become hidden-state complexity. v05 narrows the mark into an explicit relation pin, makes that state visible in a relation tray, clears it after use, and replaces edge cycling shortcuts with a single picker command. The result is slightly less shortcut-heavy, less error-prone, and still fully aligned with the MVP constraints.
