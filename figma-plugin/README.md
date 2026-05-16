# SchemeOnYou DB UI Mockup — native Figma plugin

Primary mockup source now lives in `design/mockup/figma-plugin/` together with the GitHub Pages preview.
This root copy is kept for convenience and mirrors the same native Figma layout code.

It creates **editable native Figma layers**, not a flat SVG import:

- application shell and top bar;
- project tree, canvas, inspector and footer context line;
- table cards, columns, selection states, pinned target badge and FK preview;
- directional FK lines and command sheet;
- separate design-spec frames for shortcuts, UX states and tokens.

## How to run

Preferred path:

1. Open Figma desktop or browser.
2. Open any design file.
3. Go to **Plugins → Development → Import plugin from manifest…**.
4. Select `design/mockup/figma-plugin/manifest.json`.
5. Run **SchemeOnYou DB UI Mockup**.

Convenience path: import `figma-plugin/manifest.json` from the repository root.
