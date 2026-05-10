# SchemeOnYou DB UI Mockup — Figma plugin source

This is the Figma integration source for the SchemeOnYou DB diagram mockup.

It creates the mockup as **native Figma layers** using the Figma Plugin API, based on:

- `design/final-summary.md`
- `design/db-diagram-ui-ux-v07.md`

## How to run in Figma

1. Open Figma desktop or browser.
2. Open any design file.
3. Go to **Plugins → Development → Import plugin from manifest…**
4. Select `figma-plugin/manifest.json` from this repository.
5. Run **Plugins → Development → SchemeOnYou DB UI Mockup**.

The plugin will create a page-level frame named:

`SchemeOnYou DB diagram editor — v07 mockup`

## Why plugin source, not only SVG

The previous SVG is useful for quick import, but this plugin creates editable Figma nodes: frames, text layers, rectangles, lines, chips, table cards, and annotation blocks.
