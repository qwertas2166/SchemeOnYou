summary - Dirty state, current file, and unsaved-change guard
status - done
priority - high
cost - M

goal - Preserve canonical issue history after issue-tree cleanup; do not reopen product/code scope.

description - Restored minimal history stub. Historical progress says scheduler-2 closed this task on 2026-05-16/17 with client tests/package/diff-check passing.

acceptance criteria -
- Canonical filename exists in `machine/issue/sprint_*` history so old progress and sprint summaries do not point to an absent issue record.
- Status remains `done`; this file is not backlog or active work.

progress - 2026-05-19 13:00 MSK scheduler-2: restored as minimal history stub during `064-reconcile-active-issue-paths-and-dangling-references`.
