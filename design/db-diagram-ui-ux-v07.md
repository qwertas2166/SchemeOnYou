# DB diagram UI/UX/Shortcut layout — v07

## Revision focus

v07 keeps the v06 MVP scope: a keyboard-first JavaFX desktop sketch editor for database diagrams with table, column, PK, FK, join-table support, deterministic auto-layout, undo/redo, Git-friendly text storage, and SVG export.

The v06 design is close, but a final critique finds three remaining UX/shortcut risks:

1. **The context action bar has too many jobs.** It can show selection hints, relation pin state, preview prompts, undo feedback, and safe-repeat hints. Without strict priority rules it may flicker or hide the one thing the user needs.
2. **`R Repeat` is still a hidden transient mode.** It is safer than blind repeat, but it depends on noticing a short-lived footer shortcut. That can surprise users if it keeps or restores a target after the user mentally moved on.
3. **Canvas command chords need stronger feedback.** `Space A F`-style shortcuts are good, but users need to see the current chord prefix and valid next keys so shortcut errors do not feel like lost keystrokes.

v07 resolves those by adding a strict **single context line priority model**, replacing `R Repeat` with an explicit **Keep target pinned** option in relation previews, and making the Space command overlay a small **progressive command sheet**. This is a UX simplification, not a new feature expansion.

No SQL DDL text import/export, crow's foot notation, required manual keyboard positioning, or full modeling-suite constraint editor is added. PostgreSQL metadata import from a live connection is an explicit MVP capability and must not be treated as that excluded DDL-text feature.

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
│ Context: Pinned target users.id · select source column · Space A F          │
│ Status: Canvas · Enter edit · Space commands · F1 help                     │
└────────────────────────────────────────────────────────────────────────────┘
```

Primary areas:

1. Project tree.
2. Canvas.
3. Inspector/properties panel.
4. Single context/status footer.
5. Transient command palette, command sheet, pickers, and dialogs.

## Single context line with priority rules

v07 keeps the v06 idea of avoiding a separate relation tray, but makes the footer deterministic. There is one **context line** above the ordinary status hint, and it shows only the highest-priority relevant message.

Priority from highest to lowest:

1. **Blocking validation or confirmation** — e.g. `Delete users will remove 3 FKs · Enter confirm · Esc cancel`.
2. **Active preview or picker instruction** — e.g. `Source orders.user_id → Target users.id · Enter Create FK · X Swap`.
3. **Relation pin state** — e.g. `Pinned target users.id · select source column · Space A F · Space U unpin`.
4. **Recent undoable result** — e.g. `Created FK orders.user_id → users.id · Undo Ctrl+Z`.
5. **Selection-specific tip** — e.g. `Selected users.email · Enter edit · Space A F add FK`.

Rules:

- The context line never steals focus.
- Messages do not stack; lower-priority messages wait or are omitted.
- Compound previews and destructive confirmations cannot be hidden by undo/result toasts.
- Result messages expire quickly and must not contain state-changing shortcut traps.
- If a relation pin exists, pin state reappears after result feedback expires.

This fixes the main v06 risk: the same footer can support relation state, preview prompts, and feedback without becoming noisy or unpredictable.

## Focus, selection, and selection depth

### Application focus

- Current runtime refinement replacing the original v07 cycle: `0` top menu, `1` project/left menu, `2` canvas, `3` inspector. Historical note: original v07 used `F6` / `Shift+F6` to cycle project tree → canvas → inspector → back.
- `Tab` / `Shift+Tab`: normal traversal inside the focused panel, dialog, picker, or command sheet.
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

## Progressive Space command sheet

v07 keeps Space command mode but makes it visibly stateful while it is active.

When canvas has focus and no editor/dialog is active:

- tap `Space`: open the command sheet near the selected object;
- type a visible chord prefix;
- the sheet updates to show valid next keys;
- `Backspace` removes the last chord key;
- `Esc` closes the sheet;
- invalid keys show a small inline error and do not pass through to the canvas;
- the sheet times out only if no chord key has been typed.

Initial sheet:

```text
Canvas commands
A  Add…        G  Go…        L  Layout…
P  Pin relation U Unpin      E  Select FK edge
?  Shortcuts   Esc Close
```

After pressing `A`:

```text
A · Add…
T  Table       C  Column
F  Foreign key J  Join table
Backspace      Esc Close
```

After pressing `L`:

```text
L · Layout…
D  Whole diagram
S  Selected cluster
Backspace      Esc Close
```

Why this matters: `Space A F` remains fast for experts, but beginners and occasional users get immediate feedback for every prefix. It also prevents the common shortcut error where a missed key silently starts an unrelated action.

`Space` in text fields, inspector controls, palette search, dialogs, and pickers remains normal text input.

## Relation pin and explicit keep-pinned flow

The relation pin remains **Pin for next relation**, not arbitrary multi-select.

| Shortcut | Context | Action |
|---|---|---|
| `Space`, `P` | Table/column selected | Pin selected object for next relation command |
| `Space`, `U` | Canvas | Clear relation pin |
| `Space`, `A F` | Table/column selected, optional relation pin | Add foreign key |
| `Space`, `A J` | Table selected, optional pinned table | Add join table |
| `X` | Relation preview/dialog | Swap source and target where valid |

Pin lifetime rules:

- A pin is cleared automatically after a successful FK or join-table creation by default.
- A preview can explicitly enable **Keep target pinned after create** for repeated FK work.
- A pin is cleared if its object is deleted.
- A pin survives ordinary navigation so the user can move across the canvas.
- If a command uses a stale or incompatible pin, the dialog explains the issue and offers `Use selected only`, `Choose other object`, or `Cancel`.
- `Esc` does not clear a pin, because users often press `Esc` to leave column depth; `Space U` is the explicit unpin.

### Why v07 removes `R Repeat`

v06's `R Repeat with target users.id` was safe in implementation but weak in discoverability. v07 replaces it with an explicit option inside the FK preview:

```text
Create foreign key
Source column: [orders.user_id]  →  Target column: [users.id]
Meaning: many orders rows can reference one users row

[ ] Keep target pinned after create
[Create FK] [Swap] [Change source...] [Change target...] [Cancel]
```

If checked, the target remains visibly pinned after creation:

```text
Created FK orders.user_id → users.id · Undo Ctrl+Z
Pinned target users.id · choose another source · Space A F · Space U unpin
```

This preserves the fast repeated-FK workflow while avoiding a transient hidden repeat shortcut.

## Source/target role chips and Swap

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
- `X` is a local shortcut only while the visible preview shows `Swap`; it is not a global command.

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

Mouse wheel and trackpad pan/zoom may exist. Holding `Space` for temporary mouse-pan is optional and must be disabled while text fields, dialogs, pickers, overlays, or the command sheet are active.

## Command entry model

There are two command paths:

1. Command palette for all commands.
2. Progressive Space command sheet for fast diagram-editing chords.

### Command palette

- `Ctrl+Shift+P`: open command palette.
- `Ctrl+K Ctrl+P`: optional IDE-style alternate chord.
- Fuzzy search supports commands and small structured arguments.

### Shortcut layout v07

| Shortcut | Context | Action |
|---|---|---|
| `Ctrl+Shift+P` | Global | Command palette |
| `0` / `1` / `2` / `3` | Global | Direct major-area focus: top menu / left menu / canvas / inspector |
| `F6` / `Shift+F6` | Legacy v07 note | Replaced previous next/previous major-area cycle; not an active runtime shortcut |
| `Ctrl+F` | Global/canvas | Find table/column/FK |
| `F1` | Global | Shortcut overlay |
| `Space`, `A T` | Canvas command sheet | Add table |
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
| `Space`, `?` | Canvas command sheet | Show shortcut overlay |
| `Backspace` | Canvas command sheet | Remove previous chord key |
| `X` | Relation preview | Swap FK source/target when valid |

There is intentionally no `R` repeat shortcut in v07. Repeated FK creation uses explicit `Keep target pinned after create`.

## Default action and confirmation rules

`Enter` remains context-sensitive, but only when the visible default action is safe or explicitly selected.

Safe default actions may happen directly if visible in the status/context area:

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

[ ] Keep target pinned after create
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

After successful creation, the pin clears automatically unless **Keep target pinned after create** was checked.

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

[ ] Keep target pinned after create
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

The structured input remains intentionally small and must not grow into hidden SQL/DDL text import. PostgreSQL metadata import is handled only through the explicit live-connection import flow.

## Validation, undo, and feedback UX

- Duplicate table name: inline warning, suggest deterministic alternative, but do not auto-rename silently.
- Duplicate column name in table: block create/apply until resolved.
- Invalid multiline column token: highlight exact token and show accepted grammar.
- FK target missing primary key: allow choosing any column, warn clearly.
- FK source/target type mismatch: warn but allow, because this is a sketch editor.
- Incompatible relation pin: warn in context line and relation dialog; never silently use it.
- Possible reversed FK direction: show source/target chips and `Swap` before create.
- Deleting a column with FK edges: confirmation lists affected relations.
- Deleting a table with FK edges: confirmation lists affected columns/relations.
- Bulk delete: confirmation required.
- All delete/create/edit actions are undo-safe.

After every compound mutation, show a short non-modal result message:

```text
Created join table user_roles with 2 FKs · Undo Ctrl+Z
Created FK orders.user_id → users.id · Undo Ctrl+Z
Deleted column users.email and 1 FK · Undo Ctrl+Z
```

The footer does not steal focus. `Ctrl+Z` remains the real undo mechanism. Result messages never introduce new temporary state-changing shortcuts.

## Onboarding and discoverability

First empty canvas:

```text
Press Space A T to add a table
Press Ctrl+Shift+P for all commands
Press F1 for shortcuts
Press Enter to drill into a selected table
```

When Space is pressed, the progressive command sheet teaches the shortcut tree instead of requiring users to memorize every chord.

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
- command sheet basics;
- relation pin and two-object actions;
- table/column editing;
- FK edge and relation navigation;
- layout/export.

## Pros

- Removes v06's hidden `R Repeat` state while preserving repeated-FK speed through explicit keep-pinned behavior.
- Makes footer behavior deterministic with clear priority rules, reducing message churn and lost relation context.
- Improves shortcut learnability through a progressive Space command sheet without adding new product features.
- Keeps FK direction safety from v06 via source/target chips and local `Swap`.
- Preserves MVP constraints and keyboard-first operation.

## Cons / risks

- The progressive command sheet is more UI than a bare command overlay, though it remains transient and simple.
- `Keep target pinned after create` adds one checkbox to FK previews; it must default off to avoid surprising sticky state.
- Strict context-line priority requires disciplined implementation; otherwise messages may still feel inconsistent.
- The design still depends on clear visual indicators for selected object, pinned object, and FK direction.

## Why this is better than v06

v06 made relation work safer, but it still relied on a short-lived `R` repeat shortcut and gave the context action bar multiple competing responsibilities. v07 simplifies the mental model: repeated FK creation is just an explicit decision to keep the target pinned, and the footer follows a strict priority order. The progressive command sheet also makes keyboard chords self-revealing. The result is less surprising, more teachable, and still well within the MVP sketch-editor constraints.
