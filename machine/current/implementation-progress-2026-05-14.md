# Implementation progress — 2026-05-14

## Completed in source tree

Implemented MVP skeleton slices under the Maven modules, using Java 25, JavaFX, Maven, and SeeDi-style DI annotations/launchers:

- Maven project foundation:
  - cleaned parent/module Maven configuration;
  - JavaFX dependencies and plugin configuration;
  - runtime dependency copy for script launch;
  - `bin/schemeonyou` launcher script using `~/workspace/java` Java by default;
  - fixed public Java entrypoint through `see.Main` → `see.schemeonyou.SchemeOnYouMain`.
- Core domain model:
  - project/diagram model;
  - DB tables, columns, foreign keys;
  - sequence participants/messages;
  - canvas state/layout bounds;
  - relation pin and FK preview state.
- Command layer:
  - `Command`, `CommandMetadata`, undo/redo stack;
  - add diagram/table/column/FK commands;
  - delete table command with relation-safe undo;
  - compound command and join-table factory;
  - sequence participant/message commands.
- Persistence/export/layout:
  - deterministic JSON writer with stable version field;
  - storage save service;
  - deterministic layout engine for DB and sequence diagrams;
  - SVG exporter for DB and sequence diagrams.
- Client/UI:
  - JavaFX application shell with top bar, project tree, canvas, inspector and footer;
  - seeded DB diagram sample from requirements/design;
  - canvas rendering for DB tables/columns/FKs;
  - command palette/search model;
  - hardcoded shortcut map;
  - Space command sheet;
  - F6/Shift+F6 focus traversal model;
  - context-line/status surface;
  - save JSON and export SVG dialogs.
- Validation:
  - DB diagram validator for missing FK refs, FK type mismatch, target uniqueness warning.

## Checks

Using local tools:

```bash
export JAVA_HOME=$HOME/workspace/java/openjdk-25.0.2
export PATH=$HOME/workspace/java/apache-maven-3.9.11/bin:$JAVA_HOME/bin:$PATH
```

Passed:

- `mvn -q -DskipTests compile`
- `SEE_REPO=http://89.223.121.28:8181 mvn -q -DskipTests clean package`
- `./bin/schemeonyou --core-only`
- `git diff --check`

## Issues recorded

- `machine/issue/2026-05-14-seedi-dependency-resolution.md`
  - resolved after SEE provided `SEE_REPO=http://89.223.121.28:8181`;
  - build now uses real SeeDi from `see-repo`, with project-local Maven settings for the explicit HTTP repository.
- `machine/issue/2026-05-14-javafx-display.md`
  - visual UI launch cannot be verified here because no `DISPLAY` is available.

## Key logging window

Added a non-modal JavaFX key press log window:

- `F12` opens/focuses the key log window without blocking the main application window;
- key presses are appended with counter, timestamp, key code, text and modifiers;
- the log window has a Clear button and hides instead of destroying state on close;
- shortcut map and footer hint now include `F12`.

Check passed:

- `SEE_REPO=http://89.223.121.28:8181 mvn -q -DskipTests compile`

## Keyboard Space command execution fix

Fixed Space-command handling in the JavaFX client:

- `Space` on the canvas now enters a command sequence mode instead of opening a blocking dialog;
- `Space A T` creates and selects a new DB table with default `id: uuid` primary key;
- `Space A C` adds a default text column to the selected table;
- `Space U`, `Space P`, and `Space L D` have basic execution paths;
- `Esc` cancels the pending Space command.

Check passed:

- `SEE_REPO=http://89.223.121.28:8181 mvn -q -DskipTests compile`

## Direct focus shortcuts

Changed major UI focus switching to direct numeric shortcuts:

- `0` / numpad `0` focuses the top menu;
- `1` / numpad `1` focuses the left menu;
- `2` / numpad `2` focuses the central canvas area;
- `3` / numpad `3` focuses the inspector;
- footer and shortcut map now document the numeric focus shortcuts.

The numeric shortcuts are handled globally before Space command mode so they remain available from any focused area.

Check passed:

- `SEE_REPO=http://89.223.121.28:8181 mvn -q -DskipTests compile`

## Canvas footer hints

Moved user help/Space command hints out of modal system dialogs and into a non-blocking footer overlay inside the central canvas area:

- `F1` now shows shortcut hints in the canvas footer overlay;
- `Space` shows Space-command hints in the same overlay;
- prefix commands such as `Space A` narrow the hint text;
- successful command execution or `Esc` hides the overlay;
- no modal `Alert.showAndWait()` is used for shortcut/Space hints anymore.

Check passed:

- `SEE_REPO=http://89.223.121.28:8181 mvn -q -DskipTests compile`

## Canvas footer overlay size and toggle

Updated the canvas hint footer behavior:

- overlay height is constrained to 1/10 of the canvas height;
- overlay content is clipped to that 10% region;
- repeated `F1` toggles shortcut hints off;
- repeated `Space` toggles Space-command hints off while in canvas command mode.

Check passed:

- `SEE_REPO=http://89.223.121.28:8181 mvn -q -DskipTests compile`
- `git diff --check -- client/src/main/java/see/schemeonyou/SchemeOnYouApplication.java`
