# DB diagram UI/UX/Shortcut layout — v03

## Revision focus

v03 keeps the v02 keyboard-first canvas model, but fixes three weak spots that would hurt real schema sketching:

- Column-level keyboard selection was under-specified: arrows could select tables, but it was unclear how to move inside a table card without a mouse.
- Relationship hints could become inconsistent metadata. In MVP, relation meaning should be derived from FK/PK/unique structure instead of manually edited cardinality labels.
- `Space` command mode needed clearer activation rules so it does not conflict with text editing, buttons, or future canvas panning behavior.

The result is still an MVP sketch editor: table, column, PK, FK, join table, deterministic layout, undo/redo, SVG export. No SQL import/export, no crow's foot notation, no required manual keyboard positioning.

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
│               │   │ ! email     text   │  └───────────┘ │ actions         │
├───────────────┴──────────────────────────────────────────┴─────────────────┤
│ Status: Canvas · users.email · Space: commands · Enter: edit · F1: help    │
└────────────────────────────────────────────────────────────────────────────┘
```

Primary areas:

1. Project tree.
2. Canvas.
3. Inspector/properties panel.
4. Transient command palette, pickers, and dialogs.

## Focus, selection, and selection depth

v03 separates **application focus**, **diagram selection**, and **selection depth**.

### Application focus

- `F6` / `Shift+F6`: move focus between major areas: project tree → canvas → inspector → back.
- `Tab` / `Shift+Tab`: normal field/control traversal inside the focused area or dialog.
- Canvas object navigation never uses plain `Tab`.

### Diagram selection

The canvas always shows:

- one keyboard focus ring around the active canvas area;
- one selection highlight on the selected object;
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

Why this improves v02: users can now reliably add/edit/delete columns and FKs without relying on mouse hit-testing or ambiguous spatial arrows inside dense table cards.

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

Mouse wheel and trackpad pan/zoom may exist. Holding `Space` for temporary mouse-pan is allowed only when no text field/dialog/command overlay is active; tapping `Space` opens command mode.

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
- type a two-key chord while the overlay is open;
- `Esc` closes the overlay;
- `Space` in text fields, inspector controls, palette search, and dialogs remains normal text/control input.

Optional mouse behavior:

- hold `Space` + drag: pan canvas;
- tap `Space`: commands.

If implementing tap-vs-hold feels fragile in JavaFX, MVP should prefer **tap `Space` for commands** and skip hold-to-pan.

Overlay example:

```text
Canvas commands
A T  Add table        A C  Add column
A F  Add FK           A J  Add join table
G T  Go to target     G S  Go to source
L D  Layout diagram   L S  Layout selected cluster
?    Show shortcuts   Esc  Close
```

## Shortcut layout v03

| Shortcut | Context | Action |
|---|---|---|
| `Ctrl+Shift+P` | Global | Command palette |
| `F6` / `Shift+F6` | Global | Next/previous major area |
| `Ctrl+F` | Global/canvas | Find table/column/FK |
| `F1` | Global | Shortcut overlay |
| `Space`, `A T` | Canvas command mode | Add table |
| `Space`, `A C` | Table/column selected | Add column to selected table |
| `Space`, `A F` | Table/column selected | Add foreign key |
| `Space`, `A J` | Table selected or two tables selected | Add join table |
| `Enter` | Selection | Drill into table, edit field, or open selected object depending on depth |
| `F2` | Selection | Rename selected table/column |
| `Delete` | Selection | Delete selected, undo-safe |
| `Space`, `G T` | FK column/edge selected | Go to referenced target |
| `Space`, `G S` | FK edge/target selected | Go to source |
| `Space`, `L D` | Canvas | Layout whole diagram |
| `Space`, `L S` | Selection | Layout selected cluster |
| `Space`, `?` | Canvas command mode | Show shortcut overlay |

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

Why changed from v02: silent default PK creation is useful, but making it visible avoids surprising users who intentionally wanted an empty sketch table.

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

#### Fast path

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

`Enter` creates the FK.

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

#### Creating missing FK columns

The source-column picker includes a command row:

```text
+ Create column user_id uuid
```

Choosing it creates the source column and the FK in one undoable compound command.

### 6. Relation meaning in MVP

v03 removes free-form editable relationship hints.

Instead, relation labels are derived from structure:

- FK source column not unique → many source rows can reference one target row.
- FK source column unique → at most one source row can reference one target row.
- Join table with two FKs and a combined uniqueness/PK rule → many-to-many sketch.

This satisfies the MVP need to express one-to-one, one-to-many, and many-to-many without adding crow's foot notation or an inconsistent cardinality metadata editor.

Visual rendering remains simple directional FK lines. The inspector may display derived text, but it is not a separate editable truth.

### 7. Add join table

Shortcut: `Space`, `A J`.

Best case: select two tables, then run Add join table.

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
- Visual rendering is still simple FK lines; no crow's foot notation.

### 8. Find/select

- `Ctrl+F`: fuzzy find tables, columns, and FK edges.
- Result examples:
  - `table users`
  - `column users.email`
  - `fk orders.user_id -> users.id`
- `Enter`: select and center result.
- `Alt+Left` / `Alt+Right`: return through selection history.

For dense diagrams, `Ctrl+F` is the reliable escape hatch when spatial navigation becomes noisy.

### 9. Navigate relations

- `Space`, `G T`: from FK column or edge, select referenced target table/column.
- `Space`, `G S`: from FK edge or target column, show/select source columns that reference it.
- If multiple sources exist, open a fuzzy picker rather than cycling invisibly.

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
│ !  email       text  │
│ →  role_id     uuid  │
└──────────────────────┘
```

Markers:

- `🔑` primary key.
- `!` not null.
- `◇` unique, if shown.
- `→` foreign-key source column.

FK edges:

- Simple directional line from source table to target table.
- Label may show `orders.user_id → users.id` when selected or hovered.
- No crow's foot notation in MVP.

## Inspector/properties panel

Table selected:

```text
Table
Name: users
Columns: 4
Incoming FKs: 2
Outgoing FKs: 1
[Add column] [Add FK] [Rename]
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
```

FK selected:

```text
Foreign key
Source: orders.user_id
Target: users.id
Derived relation: many-to-one from orders to users
```

Inspector keyboard behavior:

- `Tab` / `Shift+Tab`: field traversal.
- `Enter`: edit focused field or open picker.
- `Ctrl+Enter`: apply.
- `Esc`: revert current field edit.

## Command palette command set

- New database diagram
- Add table
- Add column
- Add foreign key
- Add join table
- Rename selected
- Edit selected
- Delete selected
- Find table/column/FK
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
```

The structured input remains intentionally small; it must not grow into a hidden SQL/DDL language in MVP.

## Error handling and validation UX

- Duplicate table name: inline warning, suggest deterministic alternative, but do not auto-rename silently.
- Duplicate column name in table: block create/apply until resolved.
- Invalid multiline column token: highlight exact token and show accepted grammar.
- FK target missing primary key: allow choosing any column, warn clearly.
- FK source/target type mismatch: warn but allow, because this is a sketch editor.
- Deleting a column with FK edges: confirmation lists affected relations.
- Deleting a table with FK edges: confirmation lists affected columns/relations.
- Bulk delete: confirmation required.
- All delete/create/edit actions are undo-safe.

## Onboarding and discoverability

First empty canvas shows:

```text
Press Space A T to add a table
Press Ctrl+Shift+P for all commands
Press F1 for shortcuts
Press Enter to drill into a selected table
```

Status bar always shows context actions for the current selection depth.

`F1` opens a compact shortcut overlay grouped by:

- focus/navigation;
- table/column editing;
- FK and relation navigation;
- layout/export.

The command-mode overlay remains the main discoverability mechanism for fast chords.

## Pros

- Makes column-level keyboard work concrete and predictable.
- Removes inconsistent editable relationship metadata while still covering one-to-one, one-to-many, and many-to-many semantics structurally.
- Clarifies `Space` command mode enough to avoid conflicts with text input and optional pan behavior.
- Keeps v02's safer focus model: `F6` for areas, `Tab` for controls, arrows for canvas objects.
- Still fits JavaFX and MVP constraints; no SQL import/export, no crow's foot, no required manual keyboard placement.
- Compound operations remain undo-safe and deterministic.

## Cons / risks

- Selection depth adds a concept users must learn, though it matches common tree/table navigation patterns.
- Derived relationship labels depend on correct unique/PK metadata; the UI must make unique constraints easy to see.
- Tap-vs-hold `Space` for commands vs pan can be error-prone; MVP may need to skip hold-to-pan.
- Composite PK/unique display for join tables is slightly more than the bare table/column/FK model, but it is needed to represent many-to-many cleanly.
- Fuzzy find becomes important for large diagrams; if implemented poorly, navigation will feel slower.

## Why this is better than v02

v02 fixed major shortcut conflicts, but it still left practical keyboard editing gaps. v03 makes the selected table's internal rows navigable, replaces fragile cardinality hints with derived relation meaning, and tightens `Space` command-mode rules. These are functional UX improvements rather than cosmetic additions, and they preserve the MVP boundary.
