# DB diagram UI/UX/Shortcut layout — v04

## Revision focus

v04 keeps the v03 keyboard-first canvas, command mode, and column-depth model, but fixes four workflow gaps that would become painful in real use:

- **Two-table actions were underspecified.** v03 says Add join table works when two tables are selected, but it does not define a keyboard-safe way to build that selection.
- **FK/edge selection was still too indirect.** Users can select columns and tables, but dense diagrams need a predictable way to select relation edges without pixel hunting.
- **`Enter` was overloaded.** v03 uses `Enter` for drill-in, edit, and picker activation; that can work, but only if the status bar and default action are strict.
- **Validation and undo feedback were mostly passive.** A sketch tool still needs immediate reassurance after compound operations such as Add FK and Add join table.

The result remains an MVP sketch editor: table, column, PK, FK, join table, deterministic layout, undo/redo, Git-friendly text storage, SVG export. No SQL import/export, no crow's foot notation, and no required manual keyboard positioning.

## Goal

Fast keyboard-first construction of database table diagrams on a visual canvas, optimized for developers sketching schemas rather than maintaining a strict UML/modeling suite.

The canvas remains the primary workspace. Mouse selection and dragging may exist as conveniences, but every MVP action must be reachable by keyboard.

## Main screen layout

```text
┌────────────────────────────────────────────────────────────────────────────┐
│ Top bar: Project · Active diagram · Dirty state · Zoom · Command hint      │
├───────────────┬──────────────────────────────────────────┬─────────────────┤
│ Project tree  │ Canvas                                   │ Inspector       │
│ - diagrams    │                                          │ selected object │
│ - tables      │   ┌ users ──────────────┐  ┌ orders ───┐ │ table/column/FK │
│               │   │ 🔑 id        uuid   │──│ user_id → │ │ validation      │
│               │   │ ! email     text   │  └───────────┘ │ quick actions   │
├───────────────┴──────────────────────────────────────────┴─────────────────┤
│ Status: Canvas · orders.user_id · Enter edit · Space commands · F1 help    │
└────────────────────────────────────────────────────────────────────────────┘
```

Primary areas:

1. Project tree.
2. Canvas.
3. Inspector/properties panel.
4. Transient command palette, pickers, and dialogs.

## Focus, selection, and selection depth

v04 keeps v03's separation between **application focus**, **diagram selection**, and **selection depth**, then adds an explicit **selection mark** for two-object workflows.

### Application focus

- `F6` / `Shift+F6`: move focus between major areas: project tree → canvas → inspector → back.
- `Tab` / `Shift+Tab`: normal field/control traversal inside the focused area or dialog.
- Canvas object navigation never uses plain `Tab`.

### Diagram selection

The canvas always shows:

- one keyboard focus ring around the active canvas area;
- one primary selection highlight;
- optional marked-object badge for two-object commands;
- a small breadcrumb in the status bar, for example `orders > user_id > FK to users.id`.

### Selection depth

A table card has two keyboard depths:

1. **Table depth**: the table card is selected as one object.
2. **Column depth**: a row inside the selected table is selected.

Rules:

| Shortcut | Context | Action |
|---|---|---|
| Arrow keys | Table depth | Move spatially to nearest table/FK in that direction |
| `Enter` | Table selected | Enter table and select first meaningful column, usually PK |
| `Up` / `Down` | Column depth | Previous/next column in same table |
| `Left` / `Right` | Column depth | Move to nearest related table via FK if available; otherwise leave table spatially |
| `Esc` | Column depth | Return selection to parent table |
| `Alt+Left` / `Alt+Right` | Canvas | Selection history back/forward |

### Selection mark for two-object commands

Instead of full arbitrary multi-select, MVP uses a lightweight mark:

| Shortcut | Context | Action |
|---|---|---|
| `Space`, `M` | Table/column/FK selected | Mark selected object for the next command |
| `Space`, `Shift+M` | Canvas | Clear mark |
| `Space`, `A J` | Table selected with another table marked | Add join table between marked table and current table |
| `Space`, `A F` | Column/table selected with compatible mark | Pre-fill FK dialog using marked/current objects |

Marked state is visible as a small numbered badge `①` on the object and in the status bar:

```text
Marked: users · Selected: roles · Space A J: join table
```

Why this improves v03: it supports two-table operations from the keyboard without introducing mouse-style bulk selection, rubber bands, or complex selection sets.

## Canvas navigation

When the canvas has focus:

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

Mouse wheel and trackpad pan/zoom may exist. Holding `Space` for temporary mouse-pan is not required for MVP; if implemented, it must be disabled whenever text fields, dialogs, or overlays are active.

## Command entry model

There are two command paths:

1. **Command palette** for all commands.
2. **Canvas command mode** for fast diagram editing chords.

### Command palette

- `Ctrl+Shift+P`: open command palette.
- `Ctrl+K Ctrl+P`: optional IDE-style alternate chord.
- Palette supports fuzzy command names and small structured arguments.

`Ctrl+K` alone is not bound directly, leaving it available as an IDE-style chord prefix.

### Canvas command mode

When the canvas itself has focus and no editor/dialog is active:

- tap `Space`: open command-mode overlay near the selection;
- type a chord while the overlay is open;
- `Esc` closes the overlay;
- `Space` in text fields, inspector controls, palette search, and dialogs remains normal text/control input.

Overlay example:

```text
Canvas commands
A T  Add table        A C  Add column
A F  Add FK           A J  Add join table
M    Mark selected    ⇧M   Clear mark
G T  Go to target     G S  Go to source
E N  Next FK edge     E P  Previous FK edge
L D  Layout diagram   L S  Layout selected cluster
?    Show shortcuts   Esc  Close
```

## Shortcut layout v04

| Shortcut | Context | Action |
|---|---|---|
| `Ctrl+Shift+P` | Global | Command palette |
| `F6` / `Shift+F6` | Global | Next/previous major area |
| `Ctrl+F` | Global/canvas | Find table/column/FK |
| `F1` | Global | Shortcut overlay |
| `Space`, `A T` | Canvas command mode | Add table |
| `Space`, `A C` | Table/column selected | Add column to selected table |
| `Space`, `A F` | Table/column selected, optional mark | Add foreign key |
| `Space`, `A J` | Current table plus marked table, or picker fallback | Add join table |
| `Space`, `M` | Selection | Mark selected object |
| `Space`, `Shift+M` | Canvas | Clear mark |
| `Enter` | Selection | Perform the status-bar default action |
| `F2` | Selection | Rename selected table/column |
| `Delete` | Selection | Delete selected, undo-safe |
| `Space`, `E N` / `E P` | Canvas | Select next/previous FK edge attached to current table/column |
| `Space`, `G T` | FK column/edge selected | Go to referenced target |
| `Space`, `G S` | FK edge/target selected | Go to source |
| `Space`, `L D` | Canvas | Layout whole diagram |
| `Space`, `L S` | Selection | Layout selected cluster |
| `Space`, `?` | Canvas command mode | Show shortcut overlay |

## Strict default action rule

`Enter` is allowed to be context-sensitive only if the status bar states the exact default action before it happens.

Examples:

```text
Selected: users table · Enter: enter columns · F2: rename · Space A C: add column
Selected: users.email · Enter: edit column · Esc: back to table · Space A F: add FK
Selected: FK orders.user_id → users.id · Enter: edit FK · Space G T: go target
```

If the default action is ambiguous, `Enter` opens a small action menu instead of guessing.

This keeps the shortcut set small while avoiding destructive or surprising behavior.

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
- A non-modal toast confirms the result: `Created table users · Undo Ctrl+Z`.

### 3. Add column

Shortcut: `Space`, `A C` when a table or one of its columns is selected.

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

Selection moves to the created column. If column depth was active, the new column is inserted after the currently selected row unless the user changes position in the dialog.

### 4. Edit selected

- Table selected + `Enter`: enter column depth.
- Table selected + `F2`: rename table.
- Column selected + `Enter`: inspector focuses the column name field.
- FK edge selected + `Enter`: inspector focuses FK summary/target picker.

Commit/revert:

- `Ctrl+Enter`: apply current inspector/dialog edit.
- `Esc`: revert current field edit, close transient UI, or move from column depth back to table depth.
- Every accepted mutation is an undoable command.

### 5. Add foreign key

Shortcut: `Space`, `A F`.

#### Fast path from selected column

If a column is selected and its name looks like `<table>_id` or `<singular_table>_id`:

1. Suggest matching target tables first.
2. Default target column to the target table's primary key.
3. Summarize the structural meaning derived from constraints.

```text
Create foreign key
orders.user_id  →  users.id
Derived relation: many orders rows can reference one users row
[Create] [Change target...] [Cancel]
```

`Enter` creates the FK only because the dialog default button is visibly `Create`.

#### Fast path using mark

If `users.id` is marked and `orders.user_id` is selected, `Space A F` opens the same dialog with both endpoints prefilled.

If two tables are involved, the source-column picker starts with likely missing-column rows:

```text
+ Create orders.user_id uuid → users.id
+ Create orders.user_uuid uuid → users.id
```

Choosing a create row creates the source column and FK in one undoable compound command.

#### General path

If no clear source or target exists, use one compact dialog:

```text
Create foreign key
Source table:  orders
Source column: user_id
Target table:  users
Target column: id
Uniqueness:    source column is not unique
Derived:       many-to-one from orders to users
```

Each source/target row is a fuzzy picker. `Tab` moves rows; typing filters the active picker.

### 6. FK edge selection and relation navigation

Edges are valid selectable objects, but keyboard users should not need pixel precision.

| Command | Behavior |
|---|---|
| `Space`, `E N` | Select next FK edge attached to selected table/column; if none, next edge in layout order |
| `Space`, `E P` | Select previous FK edge attached to selected table/column; if none, previous edge in layout order |
| `Ctrl+F` → `fk ...` | Fuzzy-select any FK edge |
| `Space`, `G T` | From FK source/edge, select target column/table |
| `Space`, `G S` | From FK edge/target, select source; if multiple, open picker |

This fixes the v03 gap where FK edges existed visually but were not easy to reach deliberately from the keyboard.

### 7. Relation meaning in MVP

v04 keeps v03's rule: relationship labels are derived from structure, not edited as separate metadata.

- FK source column not unique → many source rows can reference one target row.
- FK source column unique → at most one source row can reference one target row.
- Join table with two FKs and a combined uniqueness/PK rule → many-to-many sketch.

Visual rendering remains simple directional FK lines. The inspector may display derived text, but it is not a separate editable truth.

### 8. Add join table

Shortcut: `Space`, `A J`.

Best keyboard flow:

1. Select `users`.
2. `Space`, `M` to mark it.
3. Find or arrow to `roles`.
4. `Space`, `A J`.

Dialog:

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
```

Behavior:

- Creates join table, two columns, composite PK/unique marker, and two FK edges as one undoable command.
- Default name is deterministic: `<singular_left>_<plural_or_singular_right>` if available, otherwise `<left>_<right>`.
- If a name conflict exists, suggest `_map` or `_2` but do not silently rename.
- If no mark exists, the dialog opens with two fuzzy table pickers instead of failing.
- Visual rendering is still simple FK lines; no crow's foot notation.

### 9. Find/select

- `Ctrl+F`: fuzzy find tables, columns, and FK edges.
- Result examples:
  - `table users`
  - `column users.email`
  - `fk orders.user_id -> users.id`
- `Enter`: select and center result.
- `Alt+Left` / `Alt+Right`: return through selection history.

For dense diagrams, `Ctrl+F` is the reliable escape hatch when spatial navigation becomes noisy.

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
- `①` marked object for next two-object command.

FK edges:

- Simple directional line from source table to target table.
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
[Add column] [Add FK] [Mark] [Rename]
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
[Add FK] [Mark] [Rename]
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
- Mark selected
- Clear mark
- Rename selected
- Edit selected
- Delete selected
- Find table/column/FK
- Select next FK edge
- Select previous FK edge
- Go to FK target
- Go to FK source
- Layout diagram
- Layout selected cluster
- Fit diagram
- Export SVG
- Show shortcuts

Commands with arguments support structured text where practical:

```text
Add table users
Add column users.email text not null unique
Add foreign key orders.user_id users.id
Add join table users roles
```

The structured input remains intentionally small; it must not grow into a hidden SQL/DDL language in MVP.

## Validation, undo, and feedback UX

- Duplicate table name: inline warning, suggest deterministic alternative, but do not auto-rename silently.
- Duplicate column name in table: block create/apply until resolved.
- Invalid multiline column token: highlight exact token and show accepted grammar.
- FK target missing primary key: allow choosing any column, warn clearly.
- FK source/target type mismatch: warn but allow, because this is a sketch editor.
- Deleting a column with FK edges: confirmation lists affected relations.
- Deleting a table with FK edges: confirmation lists affected columns/relations.
- Bulk delete: confirmation required.
- All delete/create/edit actions are undo-safe.

After every compound mutation, show a short toast with an undo affordance:

```text
Created join table user_roles with 2 FKs · Undo Ctrl+Z
Created FK orders.user_id → users.id · Undo Ctrl+Z
Deleted column users.email and 1 FK · Undo Ctrl+Z
```

The toast is informational only; `Ctrl+Z` remains the real undo mechanism.

## Onboarding and discoverability

First empty canvas shows:

```text
Press Space A T to add a table
Press Ctrl+Shift+P for all commands
Press F1 for shortcuts
Press Enter to drill into a selected table
```

When a table is selected and no mark exists, status can teach the two-object flow:

```text
Tip: Space M marks this table; then select another table and Space A J creates a join table
```

Status bar always shows context actions for the current selection depth.

`F1` opens a compact shortcut overlay grouped by:

- focus/navigation;
- marking and two-object actions;
- table/column editing;
- FK edge and relation navigation;
- layout/export.

The command-mode overlay remains the main discoverability mechanism for fast chords.

## Pros

- Defines a keyboard-safe replacement for vague two-table selection without adding arbitrary multi-select complexity.
- Makes FK edges reachable through explicit commands and fuzzy search, reducing dependence on mouse precision.
- Keeps `Enter` context-sensitive but less surprising through a strict status-bar/default-action rule.
- Adds immediate undo feedback after compound schema operations, increasing trust.
- Preserves MVP scope: no SQL import/export, no crow's foot notation, no required manual positioning, no full modeling-suite semantics.

## Cons / risks

- The mark concept is another learnable mode-like state; it must be visibly badged and easy to clear.
- `Space M` / `Shift+M` inside command mode is slightly less mnemonic than mouse multi-select, but safer for keyboard-first use.
- Edge navigation commands add two more shortcuts; however, they solve a real reachability problem for FK editing.
- Toasts must not steal focus or become a notification system; they should stay short and undo-oriented.
- Composite PK/unique display for join tables remains slightly beyond bare column fields, but it is still necessary for many-to-many sketches.

## Why this is better than v03

v03 made column-level editing viable, but left two-object workflows and FK edge selection underdefined. v04 adds a small, explicit mark mechanism for joins/FK prefill, makes relation edges keyboard-selectable, and tightens `Enter` behavior with visible default actions. These are functional UX improvements, not cosmetic changes, and they preserve the MVP boundary.
