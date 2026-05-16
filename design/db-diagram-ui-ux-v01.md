# DB diagram UI/UX/Shortcut layout — v01

## Goal

Fast keyboard-first construction of database table diagrams on a visual canvas, without turning MVP into a full ERD/modeling suite.

## Main screen layout

```text
┌────────────────────────────────────────────────────────────────────┐
│ Top bar: Project · Active diagram · Save state · Zoom · Hints      │
├───────────────┬──────────────────────────────────────┬─────────────┤
│ Project tree  │ Canvas                               │ Properties  │
│ - diagrams    │                                      │             │
│ - tables      │   [users] ─────▶ [orders]            │ selected    │
│               │   [roles] ─────▶ [user_roles]        │ table/col/FK│
├───────────────┴──────────────────────────────────────┴─────────────┤
│ Status bar: current mode · selection · last command · shortcut hint │
└────────────────────────────────────────────────────────────────────┘
```

Focus areas:
1. Project/diagram tree.
2. Canvas.
3. Properties panel.
4. Command palette / transient dialogs.

`Tab` / `Shift+Tab` cycles focus areas. Inside canvas, `Tab` / `Shift+Tab` moves between diagram objects by visual/layout order.

## Core interaction model

### Principle

The canvas is visual feedback, not the primary data-entry mechanism. Most creation/editing happens through small keyboard dialogs and fuzzy pickers.

### Object hierarchy

- Table card
  - Header: table name
  - Columns list
    - PK marker
    - name
    - type
    - nullability marker
    - FK marker / target summary
- FK edge between table cards

Example:

```text
┌ users ───────────────┐
│ 🔑 id          uuid  │
│    email       text! │
│    role_id     uuid →│
└──────────────────────┘
```

Markers:
- `🔑` primary key.
- `!` not null.
- `?` nullable if explicit marker is needed.
- `→` column has FK reference.

## Primary DB workflow

### 1. Create a DB diagram

- `Ctrl+Shift+P` → `New database diagram`.
- Dialog asks for diagram name.
- New blank canvas opens.

### 2. Add table

Shortcut: `A T` when canvas focused.

Dialog:

```text
Add table
Name: users
Columns optional multiline:
id uuid pk
email text not null
created_at timestamp not null
```

Behavior:
- Creates table with parsed columns.
- If multiline is empty, creates table with default `id uuid pk`.
- Auto-layout places it near current selection or next free grid slot.
- Selection moves to the new table.

Why: fast initial sketching without forcing many column dialogs.

### 3. Add column

Shortcut: `A C` when table selected.

Dialog:

```text
Add column to users
Name: email
Type: text
Nullable: No
Primary key: No
```

Fast input alternative in command palette:

```text
Add column: users.email text not null
```

Selection moves to the created column.

### 4. Edit selected

Shortcut: `Enter`.

- Table selected → inline rename table, with `F2` as explicit rename.
- Column selected → properties editor focuses first editable field.
- FK edge selected → properties editor focuses target/reference info.

All changes are undoable commands.

### 5. Add foreign key

Shortcut: `A F` when a table or column is selected.

Picker flow:

1. Source table: prefilled from selected table/column.
2. Source column: fuzzy list of columns; option to create `<target>_id`.
3. Target table: fuzzy table picker.
4. Target column: default primary key, usually `id`.
5. Relationship hint: one-to-many / one-to-one / many-to-many via join table.

For many-to-many:
- Command `Add join table` creates a join table with two FK columns.
- Default name: `<left>_<right>` or `<left>_<right>_map` if conflict.

MVP visual rendering remains simple FK line; no crow's foot requirement.

### 6. Find/select

- `Ctrl+F`: fuzzy find tables, columns, FK edges.
- Result format:
  - `table users`
  - `column users.email`
  - `fk orders.user_id -> users.id`
- `Enter`: select and center on canvas.

### 7. Navigate relations

- `G T`: go to referenced target table/column from FK column/edge.
- `G S`: go to FK source from edge.
- `Alt+Left` / `Alt+Right`: selection history back/forward.

### 8. Layout

- `Ctrl+L`: re-layout whole diagram.
- `Shift+Ctrl+L`: re-layout around selected cluster.
- Layout rules:
  - Tables in dependency layers: referenced tables left/top, dependent tables right/bottom.
  - Join tables placed between related tables.
  - Preserve stable order by entity creation/id/name for deterministic layout.

Manual positioning by keyboard is intentionally not part of MVP.

## Shortcut layout v01

### Global / IDE-style

| Shortcut | Action |
|---|---|
| `Ctrl+K`, `Ctrl+Shift+P` | Command palette |
| `Ctrl+S` | Save project |
| `Ctrl+O` | Open project |
| `Ctrl+N` | New project/diagram via dialog |
| `Ctrl+F` | Find element |
| `Ctrl+Z` | Undo |
| `Ctrl+Y`, `Ctrl+Shift+Z` | Redo |
| `Esc` | Cancel/back/close transient UI |
| `Delete` | Delete selected, undo-safe |
| `F1` | Show shortcuts/help overlay |

### Canvas navigation

| Shortcut | Action |
|---|---|
| `Tab` | Next element or next focus area depending focus context |
| `Shift+Tab` | Previous element/focus area |
| Arrow keys | Move selection spatially between visible objects |
| `Ctrl+Plus` / `Ctrl+Minus` | Zoom in/out |
| `Ctrl+0` | Fit diagram |
| `Space` then arrows | Pan canvas while holding transient pan mode |
| `Alt+Left` / `Alt+Right` | Selection history back/forward |

### DB editing chords

Chords are two-key sequences, shown in status bar after first key.

| Shortcut | Action |
|---|---|
| `A T` | Add table |
| `A C` | Add column to selected table |
| `A F` | Add foreign key |
| `A J` | Add join table for many-to-many |
| `E` or `Enter` | Edit selected |
| `F2` | Rename selected |
| `G T` | Go to referenced target |
| `G S` | Go to source |
| `Ctrl+L` | Layout diagram |
| `Ctrl+Shift+L` | Layout selected cluster |

Rationale: letter chords are easier to remember than many Ctrl/Alt combinations and avoid conflicts with standard IDE/global shortcuts.

## Properties panel

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

Properties panel supports direct keyboard editing. `Enter` edits current field, `Ctrl+Enter` applies, `Esc` reverts field editing.

## Command palette command set for DB diagrams

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
- Fit diagram
- Export SVG
- Show shortcuts

## Error handling / validation UX

- Duplicate table name: inline warning, suggest `users_2` but do not auto-rename silently.
- Duplicate column name in table: block confirmation until resolved.
- FK target missing primary key: allow choosing any column, warn clearly.
- Deleting table with FK edges: show confirmation listing affected relations; action remains undoable.
- Bulk delete: confirmation required.

## Visual design

- High-contrast focus ring: blue for keyboard focus, orange for validation issue.
- Selection uses thick border on table or highlighted row for column.
- FK edges are muted gray by default; selected edge is high-contrast.
- Mini-map is not MVP unless canvas navigation becomes painful.
- Status bar always shows next likely shortcuts, e.g. `A C Add column · A F Add FK · Enter Edit`.

## Pros

- Strong keyboard flow for schema sketching.
- Keeps visual canvas central while avoiding mouse dependency.
- Chord shortcuts scale without conflicting with global IDE shortcuts.
- Multiline add-table flow speeds up realistic schema drafting.
- Fuzzy selection for FK creation avoids fragile canvas targeting.
- Explicitly respects MVP exclusions: no crow's foot, no DDL import/export, no manual positioning requirement.

## Cons / risks

- Chords like `A T` need discoverability and timeout handling.
- `Tab` behavior may be ambiguous between focus-area navigation and element navigation.
- Multiline column parser can become a hidden mini-language if not constrained.
- Many shortcuts may feel heavy for first-time users.
- Relationship hints may confuse users if visual notation stays simple.

## Improvement candidates for next revisions

- Clarify focus vs selection model to avoid `Tab` ambiguity.
- Add a compact onboarding/help overlay for keyboard workflows.
- Decide whether DB editing chords should require command mode or work directly on canvas.
- Refine FK creation flow to reduce step count.
- Define stable auto-layout rules in more detail.
