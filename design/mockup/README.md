# SchemeOnYou mockup

Static and Figma-source bundle for the DB diagram UI/UX mockup.

- `index.html` — GitHub Pages preview with HTML/CSS layout, not an embedded SVG screenshot.
- `figma-plugin/` — primary Figma source. Import `figma-plugin/manifest.json` in Figma to generate editable native layers.
- `design/` — copied final design notes used by the mockup.
- `assets/schemeonyou-db-ui-mockup.svg` — legacy fallback asset kept for reference only; it is no longer the primary Figma workflow.

## Figma workflow

1. Open Figma.
2. Go to **Plugins → Development → Import plugin from manifest…**.
3. Select `design/mockup/figma-plugin/manifest.json`.
4. Run **SchemeOnYou DB UI Mockup**.

The plugin creates a complete editable mockup: app shell, project tree, canvas, table cards, FK lines, inspector, command sheet, shortcut/spec frames and design tokens.
