# DB diagram UI/UX/Shortcut layout — v02

## Revision focus

v02 keeps the v01 direction but fixes the main UX/shortcut risks:

- `Tab` was overloaded between focus traversal and canvas object navigation.
- Bare letter chords like `A T` were useful, but needed an explicit canvas command mode so they do not collide with text entry or future type-to-search behavior.
- FK creation had too many steps for the common case.
- Pan/zoom/layout shortcuts needed clearer IDE-style behavior.
- Relationship hints needed clearer separation from visual notation, because crow's foot is not in MVP.

## Goal

Fast keyboard-first construction of database table diagrams on a visual canvas, without turning MVP into a full ERD/modeling suite.

The editor is keyboard-first, not keyboard-only: every MVP mutation must be reachable from keyboard, but mouse selection and dragging may exist as convenience input.

## Main screen layout

```text
┌────────────────────────────────────────────────────────────────────────────┐
│ Top bar: Project · Active diagram · Dirty state · Zoom · Command hint      │
├───────────────┬──────────────────────────────────────────┬─────────────────┤
│ Project tree  │ Canvas                                   │ Inspector       │
│ - diagrams    │                                          │ selected table  │
│ - tables      │   ┌ users ─────────┐     ┌ orders ─────┐ │ / column / FK   │
│               │   │ 🔑 id uuid     │────▶│ user_id uuid│ │                 │
│               │   │    email text! │     └─────────────┘ │                 │
├───────────────┴──────────────────────────────────────────┴─────────────────┤
│ Status: Canvas · users.email · Press Space for canvas commands · Ctrl+K... │
└────────────────────────────────────────────────────────────────────────────┘
```

Primary areas:

1. Project tree.
2. Canvas.
3. Inspector/properties panel.
4. Transient command palette, pickers, and dialogs.

## Focus and selection model

v02 separates **application focus** from **diagram selection**.

- `F6` / `Shift+F6`: move focus between major areas: tree → canvas → inspector → back.
- `Tab` / `Shift+Tab`: normal control traversal inside the currently focused area or dialog.
- Canvas object navigation does **not** use plain `Tab`.
- The canvas always shows:
  - one keyboard focus ring around the active canvas area;
  - one selection highlight on the selected table/column/FK edge.

Why: this follows desktop/IDE expectations and removes the v01 ambiguity where `Tab` sometimes meant focus traversal and sometimes meant next diagram element.

## Canvas navigation

When the canvas has focus:

| Shortcut | Action |
|---|---|
| Arrow keys | Move selection spatially to nearest visible object in that direction |
| `Ctrl+Arrow` | Pan viewport |
| `Shift+Arrow` | Extend selection to nearby object, if multi-select is enabled for the command |
| `Home` | Select first table in deterministic layout order |
| `End` | Select last table in deterministic layout order |
| `Ctrl+Plus` / `Ctrl+Minus` | Zoom in/out |
| `Ctrl+0` | Fit whole diagram to view |
| `Ctrl+1` | Actual size / 100% zoom |
| `Alt+Left` / `Alt+Right` | Selection history back/forward |

Mouse wheel and trackpad pan/zoom may exist, but are not required for keyboard operation.

## Command entry model

There are two command paths:

1. **Command palette** for all commands.
2. **Canvas command mode** for fast diagram editing chords.

### Command palette

- `Ctrl+Shift+P`: open command palette.
- `Ctrl+K Ctrl+P`: alternate IDE-style palette chord if supported by the platform.
- Palette supports fuzzy command names and structured command input.

`Ctrl+K` alone is not bound directly to the palette in v02 because it is often an IDE chord prefix. Binding it directly would make future shortcuts harder and could surprise users coming from editors.

### Canvas command mode

When the canvas is focused:

- `Space`: open a small command-mode overlay near the selection.
- The overlay lists the most relevant chords.
- Chords are accepted only while the overlay is open.
- `Esc` closes the overlay.
- Typing into dialogs, inline rename fields, palette search, or inspector fields never triggers canvas chords.

Example overlay:

```text
Canvas commands
A T  Add table        A C  Add column
A F  Add FK           A J  Add join table
G T  Go to target     G S  Go to source
L D  Layout diagram   L S  Layout selected cluster
Enter Edit            Del Delete
```

This preserves the memorable v01 letter chords while making the mode explicit and safe.

## DB editing shortcuts v02

| Shortcut | Context | Action |
|---|---|---|
| `Space`, `A T` | Canvas command mode | Add table |
| `Space`, `A C` | Table/column selected | Add column to selected table |
| `Space`, `A F` | Table/column selected | Add foreign key |
| `Space`, `A J` | Table selected or two tables selected | Add join table |
| `Enter` | Selection | Edit selected |
| `F2` | Selection | Rename selected |
| `Delete` | Selection | Delete selected, undo-safe |
| `Space`, `G T` | FK column/edge selected | Go to referenced target |
| `Space`, `G S` | FK edge/target selected | Go to source |
| `Space`, `L D` | Canvas | Layout whole diagram |
| `Space`, `L S` | Selection | Layout selected cluster |
| `Ctrl+F` | Global | Find table/column/FK |

`Ctrl+L` is intentionally removed as the primary layout shortcut because it conflicts with common browser/location-bar muscle memory and can be confused with formatting shortcuts in IDEs. Layout remains fast through `Space L D` and the command palette.

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
email text not null
created_at timestamp not null
```

Rules for the constrained multiline parser:

```text
<name> <type> [pk] [not null|null] [unique]
```

MVP accepts only this small grammar. Unsupported tokens show inline errors and do not silently create fields.

Behavior:

- Creates the table and parsed columns as one undoable command.
- Empty column list creates `id uuid pk` by default.
- Auto-layout places the table near current selection or in the next free grid slot.
- Selection moves to the new table.
- The status bar shows the next likely actions: `A C Add column · A F Add FK · Enter Rename`.

### 3. Add column

Shortcut: `Space`, `A C` when a table or one of its columns is selected.

Dialog:

```text
Add column to users
Name: email
Type: text
Nullable: No
Primary key: No
```

Fast structured input in command palette:

```text
Add column users.email text not null
```

Selection moves to the created column.

### 4. Edit selected

Shortcut: `Enter`.

- Table selected → inspector focuses table name, with `F2` as direct inline rename.
- Column selected → inspector focuses the column name field.
- FK edge selected → inspector focuses FK summary and relationship hint.

Commit/revert:

- `Ctrl+Enter`: apply current inspector/dialog edit.
- `Esc`: revert current field edit or close transient UI.
- Every accepted mutation is an undoable command.

### 5. Add foreign key

Shortcut: `Space`, `A F`.

v02 optimizes for the common case: selected source column points to target table primary key.

#### Fast path

If a column is selected and its name looks like `<table>_id` or `<singular_table>_id`:

1. Show target table suggestions first.
2. Default target column to that table's primary key.
3. Confirmation dialog summarizes the FK:

```text
Create foreign key
orders.user_id  →  users.id
Relationship hint: many-to-one
[Create] [Change target...] [Cancel]
```

`Enter` creates the FK.

#### General path

If no clear source or target exists, use four compact picker rows in one dialog rather than a long wizard:

```text
Create foreign key
Source table:  orders
Source column: user_id
Target table:  users
Target column: id
Relationship:  many-to-one  (metadata only)
```

Each row is a fuzzy picker. `Tab` moves rows; typing filters the active picker. This reduces the v01 step count while preserving keyboard use.

#### Creating missing FK columns

The source-column picker includes:

```text
+ Create column user_id uuid
```

Choosing it creates the source column and the FK in one undoable compound command.

### 6. Add join table

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
```

Behavior:

- Creates join table and two FK edges as one undoable command.
- Default name is deterministic: `<singular_left>_<plural_or_singular_right>` if available, otherwise `<left>_<right>`.
- If a name conflict exists, suggest `_map` or `_2` but do not silently rename.
- Visual rendering is still simple FK lines; no crow's foot notation.

### 7. Find/select

- `Ctrl+F`: fuzzy find tables, columns, and FK edges.
- Result examples:
  - `table users`
  - `column users.email`
  - `fk orders.user_id -> users.id`
- `Enter`: select and center result.
- `Alt+Left` / `Alt+Right`: return through selection history.

### 8. Navigate relations

- `Space`, `G T`: from FK column or edge, select referenced target table/column.
- `Space`, `G S`: from FK edge or target column, show/select source columns that reference it.
- If multiple sources exist, open a fuzzy picker rather than cycling invisibly.

### 9. Layout

Manual keyboard positioning is not required in MVP.

Layout commands:

- `Space`, `L D`: layout whole diagram.
- `Space`, `L S`: layout selected cluster.
- Command palette: `Layout diagram`, `Layout selected cluster`.

Simple deterministic rules:

1. Tables with no outgoing FKs are placed in the first layer.
2. Tables depending on them are placed in later layers.
3. Join tables are placed between the two most connected related tables when possible.
4. Cycles are broken deterministically by stable table id, then name.
5. Within a layer, order by stable table id, then name.
6. Existing manual mouse positions may be preserved as soft hints, but `Layout diagram` can always normalize the canvas.

This avoids required manual keyboard positioning and keeps SVG/export diffs stable.

## Visual object design

Table card:

```text
┌ users ───────────────┐
│ 🔑 id          uuid  │
│    email       text! │
│    role_id     uuid → users.id │
└──────────────────────┘
```

Markers:

- `🔑` primary key.
- `!` not null.
- `?` nullable only when explicitly useful.
- `→ users.id` FK target summary.

FK edges:

- Simple directional line from source table to target table.
- Label may show `orders.user_id → users.id` when selected or hovered.
- No crow's foot notation in MVP.
- Relationship hint is metadata in inspector/dialogs, not a visual cardinality promise.

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
Foreign key: none
```

FK selected:

```text
Foreign key
Source: orders.user_id
Target: users.id
Relationship hint: many-to-one
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

Commands with arguments support structured text where practical, for example:

```text
Add table users
Add column users.email text not null
Add foreign key orders.user_id users.id
```

## Error handling and validation UX

- Duplicate table name: inline warning, suggest a deterministic alternative, but do not auto-rename silently.
- Duplicate column name in table: block create/apply until resolved.
- Invalid multiline column token: highlight the exact token and show the accepted grammar.
- FK target missing primary key: allow choosing any column, warn clearly.
- Deleting a column with FK edges: confirmation lists affected relations.
- Deleting a table with FK edges: confirmation lists affected columns/relations.
- Bulk delete: confirmation required.
- All delete/create/edit actions are undo-safe.

## Onboarding and discoverability

- First empty canvas shows three actions:

```text
Press Space A T to add a table
Press Ctrl+Shift+P for all commands
Press F1 for shortcuts
```

- Status bar always shows context actions for the current selection.
- `F1` opens a compact shortcut overlay grouped by: navigation, editing, layout, find/go-to.
- The command-mode overlay is the primary way users discover chords.

## Pros

- Removes the biggest v01 ambiguity by separating `F6` focus traversal from arrow-key canvas navigation.
- Keeps visual canvas primary while preserving keyboard-first editing.
- Makes letter chords safer by requiring explicit canvas command mode.
- Reduces FK creation friction with a fast path and one compact general dialog.
- Avoids crow's foot and SQL import/export while still representing PK/FK clearly.
- Keeps mutations undo-safe and compound operations atomic.
- More deterministic layout rules improve Git-friendly storage and SVG export repeatability.

## Cons / risks

- `Space` command mode adds one keystroke before frequent chord actions.
- Users unfamiliar with `F6` focus traversal may need the onboarding hint.
- Fast FK inference from `_id` names can be wrong; confirmation must be obvious and cheap to change.
- Structured palette input and multiline table parsing still need tight grammar to avoid becoming a hidden scripting language.
- Supporting both mouse soft positions and deterministic layout may require careful persistence rules.

## Why this is better than v01

v01 had the right product shape, but several shortcuts were under-specified or overloaded. v02 improves interaction correctness without expanding MVP scope. The main changes are structural rather than cosmetic: safer command mode, unambiguous focus/navigation, faster FK creation, clearer layout commands, and explicit limits on parser complexity.
