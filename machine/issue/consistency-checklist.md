# Issue tree consistency checklist

Use during cleanup/backlog scheduler passes:

1. `machine/issue/backlog/` contains only `status - backlog` issue files.
2. `machine/issue/work/` is optional staging for active issues; if used, it contains only `status - work` or `status - in_progress` files. It may be empty.
3. `machine/issue/sprint_N/` is the canonical planning/history location and may contain `planned`, `work`, `in_progress`, or `done` issue files. Active sprint files are valid when `cost.md` / progress notes make their current state explicit.
4. Done issue files live in `sprint_N/` or another history location, not in `work/`.
5. Dangling references to old `backlog/` or `work/` paths must either be replaced with the current `sprint_N/` canonical path or have a dated correction/history stub that prevents scheduler confusion.
6. `cost.md` status tables are newer than the latest move/close event or have an explicit dated correction section.
7. File content starts with expected metadata (`summary/status/priority/cost`) and has no stray leading characters.
8. Product/code scope is not changed by hygiene cleanup unless the issue explicitly asks for it.
