# SchemeOnYou DB UI Mockup — native Figma plugin

This is the primary Figma source for the SchemeOnYou DB diagram editor mockup.

It creates **editable native Figma layers**, not a flat SVG import:

- application shell and top bar;
- project tree, canvas, inspector and footer context line;
- table cards, columns, selection states, pinned target badge and FK preview;
- directional FK lines and command sheet;
- separate design-spec frames for shortcuts, UX states and tokens.

Based on:

- `../design/final-summary.md`
- `../design/db-diagram-ui-ux-v07.md`

## How to run

1. Open Figma desktop or browser.
2. Open any design file.
3. Go to **Plugins → Development → Import plugin from manifest…**.
4. Select `design/mockup/figma-plugin/manifest.json` from this repository.
5. Run **Plugins → Development → SchemeOnYou DB UI Mockup**.

The plugin will create a page named `SchemeOnYou DB UI Mockup` and a root frame named `SchemeOnYou DB diagram editor — native editable mockup`.
