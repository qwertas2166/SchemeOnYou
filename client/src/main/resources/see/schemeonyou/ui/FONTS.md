# UI font notes

SchemeOnYou uses `Monaspace Krypton` as the preferred UI font family.

- Preferred family: `Monaspace Krypton`
- Body/content weight: `Extra Light 200`
- Headings/functional-area legends: `Regular 400`
- Fallback stack: `Monaspace`, `SFMono-Regular`, `Consolas`, `monospace`

Bundling note: the runtime theme currently references the installed/system font with a safe fallback stack instead of bundling font binaries. The official upstream source is GitHub Next Monaspace (`githubnext/monaspace`), licensed under SIL Open Font License 1.1 with the reserved font names including `Monaspace Krypton`. If font binaries are bundled later, use the official upstream release and keep the OFL license notice with the resources.
