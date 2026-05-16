# Current progress

## 2026-05-11 13:28 MSK

Task received: analyze and structure `requirements` and `design`, update `machine`, split work into parts, keep local plan in `machine/current`, execute in parts, and schedule a correctness/contradiction check at 13:45.

## 2026-05-11 13:30 MSK

Source inventory completed.

Observed current source state:

- `requirements/` contains product/MVP requirements, resolved questions, keyboard-first analysis, open-source analysis.
- `design/` contains DB diagram UX iterations v01-v07 and `final-summary.md`.
- Latest design recommendation is `design/db-diagram-ui-ux-v07.md`.
- Existing `machine/requirements.json` reflects base requirements but does not yet encode DB diagram design v07.
- Existing `machine/tasks.json` contains high-level implementation order but lacks design-specific DB diagram slices.

Important consistency note:

- Base requirements still say `Tab` / `Shift+Tab` move focus areas.
- DB design v07 replaces that for the DB editor with `F6` / `Shift+F6` for major areas and keeps `Tab` for traversal inside panels/dialogs/pickers.
- Machine state should make this explicit as a design refinement, not silently hide it.


## 2026-05-11 13:34 MSK

Machine artifacts updated:

- `machine/requirements.json` now includes source status and DB design v07 refinements.
- `machine/design.json` added as structured DB diagram UX/design state.
- `machine/tasks.json` rewritten into phased implementation slices.
- `machine/README.md` and `machine/ai-brief.md` refreshed.
- `machine/current/consistency-check.md` contains initial consistency result and the 13:45 follow-up checklist.


## 2026-05-11 13:45 MSK

Scheduled consistency review completed.

Validated JSON files and re-scanned requirements/design/machine for the known risk areas. No new contradictions found. The Tab-vs-F6 shortcut difference is intentionally documented as a DB diagram v07 refinement.

Updated `machine/tasks.json` to mark `A006` done and appended the result to `machine/current/consistency-check.md`.

## 2026-05-15 17:32 MSK — scheduler-3 project analysis/backlog refinement

Analyzed current code/docs/issues after recent sprint/work updates. New backlog issue created:

- `machine/issue/backlog/030-sequence-delete-undo-safe.md` — high/M: undo-safe delete for sequence participant/message, including participant cascade preview for affected messages.

Rationale: SEE clarified delete/edit are required for all sequence elements in first release. Existing `022` covers sequence add/edit/select flows, but destructive-action semantics for sequence elements were not separately captured; DB delete controller work does not cover sequence model selection or participant-message cascade.

Verification:

- Ran local Maven test contract with project-local JDK/Maven: `mvn -q test` — passed.

## 2026-05-15 18:06 MSK — scheduler-3 project analysis/backlog refinement

Analyzed current code/docs/issues after latest work updates. New backlog issue created:

- `machine/issue/backlog/031-normalize-issue-status-and-sprint-indexes.md` — mid/S: normalize issue path/status/sprint index drift after moved tasks.

Rationale: issue tree has planning metadata drift (`work/030` still says `status - backlog`; `sprint_4/cost.md` still describes `027` as planned while it is in work). This can mislead scheduler prioritization and release reports.

Verification:

- `mvn -q test` with project-local Java/Maven and `SEE_REPO=http://89.223.121.28:8181` — passed.
- `mvn -q -DskipTests package` reached `git diff --check` gate; package command passed, but `git diff --check` initially failed due trailing blank line in `machine/work/cost_rule.md`.
- Fixed trailing blank line in `machine/work/cost_rule.md`; `git diff --check` now passes.

## 2026-05-15 18:32 MSK — scheduler-3 project analysis/backlog refinement

Analyzed current code/docs/issues for Space-command, sequence, release, storage and backlog drift after latest work updates. No brand-new backlog issue created: the newly found gap belongs to existing planned issue `019`.

Updated issue:

- `machine/issue/sprint_3/019-wire-or-hide-declared-space-navigation-layout-commands.md` — expanded to cover `Space E` semantic drift: runtime/ShortcutMap use `Space E` as Edit selected, while `design/final-summary.md` and `machine/design.json` still describe it as FK edge picker / `select_fk_edge`.

Rationale: `019` already owns declared-vs-runtime Space command consistency (`G T`, `L S`); adding `Space E` avoids duplicate backlog tasks and keeps the fix S-sized unless a real FK-edge picker is explicitly chosen.

Verification:

- Ran local Maven test contract with project-local JDK/Maven and `SEE_REPO=http://89.223.121.28:8181`: `mvn -q test` — passed.
- `git diff --check` for the updated issue/progress files — passed.

## 2026-05-15 19:02 MSK — scheduler-3 project analysis/backlog refinement

Analyzed current code/docs/issues for sequence MVP, keyboard shortcuts, release/privacy polish, storage/export escaping, DB relation semantics, issue hygiene and release gates. No new backlog issue created: newly observed gaps are already covered by existing planned/work issues.

Existing coverage confirmed:

- `sprint_3/019-wire-or-hide-declared-space-navigation-layout-commands.md` covers active Space command drift: `Space G T` / `Space L S` are advertised but not executed, and `Space E` runtime/docs semantics conflict.
- `sprint_4/029-make-key-log-dev-only-for-release.md` covers release/privacy risk from F12 key log still appearing in footer/runtime `ShortcutMap`.
- `sprint_2/015-non-modal-keyboard-flows-for-frequent-actions.md` covers remaining blocking `showAndWait()` editor flows: palette/find/rename/delete/join preview.
- `sprint_3/021` and `sprint_3/022` cover sequence model/type/storage/validation and sequence add/edit/select keyboard gaps.
- `sprint_2/016`, `sprint_4/025`, `sprint_3/023`, `sprint_5/031` cover escaping, composite constraints, scale smoke, and issue status/index drift respectively.

Verification:

- `mvn -q test` with project-local Java/Maven and `SEE_REPO=http://89.223.121.28:8181` — passed.
- `git diff --check` — passed.

## 2026-05-15 19:35 MSK — scheduler-3 project analysis/backlog refinement

Analyzed current code/docs/issues for MVP keyboard shortcuts, sequence gaps, DB relation semantics, non-modal UX, release/privacy polish, release gates, storage/export escaping, performance smoke and issue hygiene.

No new backlog issue created: the only newly observed gap is a small extension of existing active issue `work/027-wire-missing-mvp-global-shortcuts.md`.

Updated issue:

- `machine/issue/work/027-wire-missing-mvp-global-shortcuts.md` — expanded to cover `Ctrl+Y` redo help/ShortcutMap drift: runtime handles `Ctrl+Y`, and requirements allow `Ctrl+Y` or `Ctrl+Shift+Z`, but active `ShortcutMap`/redo metadata/help only advertise `Ctrl+Shift+Z`.

Existing coverage confirmed:

- `sprint_3/019` covers `Space G T`, `Space L S`, and `Space E` declared-vs-runtime drift.
- `sprint_4/029` covers F12 key log release/privacy risk.
- `sprint_2/015` covers remaining blocking `showAndWait()` editor flows.
- `sprint_3/021`, `sprint_3/022`, `work/030`, and parent `work/007` cover sequence model/storage/validation, add/edit/select, delete, and full Sequence UI MVP.
- `sprint_2/016`, `sprint_4/025`, `sprint_3/023`, `sprint_2/017`, and `work/010` cover escaping, composite constraints, scale smoke, reproducible Maven contract, and release gates.

Verification:

- `mvn -q test` with project-local Java/Maven and `SEE_REPO=http://89.223.121.28:8181` — passed.
- `mvn -q -DskipTests package` with the same environment — passed.
- `git diff --check` — passed.

## 2026-05-15 20:01 MSK — scheduler-3 project analysis/backlog refinement

Analyzed current code/docs/issues for MVP gaps after latest shortcut, sequence, release/privacy, storage/export, performance and issue-hygiene updates.

No new backlog issue created: observed gaps are already covered by existing planned/work issues.

Existing coverage confirmed:

- `sprint_3/019-wire-or-hide-declared-space-navigation-layout-commands.md` covers active Space command drift: `Space G T` / `Space L S` are advertised but not executed, and `Space E` runtime/docs semantics conflict.
- `work/027-wire-missing-mvp-global-shortcuts.md` covers `Ctrl+Shift+P`, `Ctrl+N`, and `Ctrl+Y` redo hint/metadata drift.
- `sprint_4/029-make-key-log-dev-only-for-release.md` covers F12 key-log release/privacy risk.
- `sprint_2/015-non-modal-keyboard-flows-for-frequent-actions.md` covers blocking `showAndWait()` editor flows: palette, find, rename, delete, join preview.
- `sprint_2/016-harden-json-and-svg-string-escaping.md` covers incomplete JSON/SVG escaping in writer/export.
- `sprint_3/021`, `sprint_3/022`, `work/030`, and parent `work/007` cover sequence model/storage/validation, add/edit/select, delete, and full Sequence UI MVP.
- `sprint_4/025`, `sprint_3/023`, `sprint_2/017`, and `work/010` cover composite constraints, scale smoke, reproducible Maven contract, and release gates.

Verification:

- `mvn -q test` with project-local Java/Maven and `SEE_REPO=http://89.223.121.28:8181` — passed.
- `git diff --check` — passed.

## 2026-05-15 20:35 MSK — scheduler-3 project analysis/backlog refinement

Analyzed current code/docs/issues for command availability, sequence/DB context separation, Space-command hints, release gates and existing backlog coverage. New backlog issue created:

- `machine/issue/backlog/032-context-aware-command-availability-and-diagram-type-guards.md` — high/M: add diagram-type guards and context-aware command/palette/Space availability.

Rationale: after sequence commands landed, DB and sequence actions are mixed in the global palette/Space sheet. `addTableFromKeyboard()` has no `DiagramType.DATABASE` guard, so `Space A T` can mutate a sequence diagram by adding DB tables. Existing issues cover sequence add/edit (`022`), Space command drift (`019`), non-modal UX (`015`) and broad presenter extraction (`026`), but not the concrete model-corruption/context-availability guard.

Existing coverage confirmed:

- `sprint_3/019` covers `Space G T`, `Space L S`, and `Space E` declared-vs-runtime drift.
- `sprint_2/015` covers remaining blocking `showAndWait()` editor flows.
- `sprint_4/029` covers F12 key-log release/privacy risk.
- `sprint_3/021`, `sprint_3/022`, `work/030`, and parent `work/007` cover sequence model/storage/types, add/edit/select, delete, and Sequence UI MVP.
- `sprint_4/025`, `sprint_2/016`, `sprint_3/023`, `sprint_2/017`, and `work/010` cover composite DB semantics, escaping, scale smoke, reproducible Maven contract, and release gates.

Verification:

- `mvn -q test` with project-local Java/Maven and `SEE_REPO=http://89.223.121.28:8181` — passed.
- `mvn -q -DskipTests package` with the same environment — passed.
- `git diff --check` — passed.

## 2026-05-15 21:03 MSK — scheduler-3 project analysis/backlog refinement

Analyzed current code/docs/issues for runtime keymap, context-aware command guards, Space-command drift, non-modal UX, release/privacy polish, sequence gaps, storage/export hardening, performance smoke, and issue hygiene.

New backlog issue created:

- `machine/issue/backlog/033-sync-human-requirements-shortcut-model.md` — mid/S: sync human-readable requirements markdown with current runtime `0/1/2/3` major-area focus and scoped Tab behavior.

Rationale: machine/design docs already use the current runtime shortcut model, but `requirements/requirements.md` and `requirements/keyboard-only-analysis.md` still present `Tab` / `Shift+Tab` as major-area navigation. Existing `014`/`018` fixed machine/design artifacts, not the human requirements markdown, so this is a small non-duplicate consistency task.

Updated issue:

- `machine/issue/work/032-context-aware-command-availability-and-diagram-type-guards.md` — added successful verification after the previous local-tooling blocker.

Existing coverage confirmed:

- `sprint_3/019` covers `Space G T`, `Space L S`, and `Space E` declared-vs-runtime drift.
- `sprint_2/015` covers remaining blocking `showAndWait()` editor flows.
- `sprint_4/029` covers F12 key-log release/privacy risk.
- `sprint_3/021`, `sprint_3/022`, `work/030`, and parent `work/007` cover sequence model/storage/types, add/edit/select, delete, and Sequence UI MVP.
- `sprint_4/025`, `sprint_2/016`, `sprint_3/023`, `sprint_2/017`, and `work/010` cover composite DB semantics, escaping, scale smoke, reproducible Maven contract, and release gates.

Verification:

- `mvn -q test` with project-local Java/Maven and `SEE_REPO=http://89.223.121.28:8181` — passed.
- `mvn -q -DskipTests package` with the same environment — passed.
- `git diff --check` — passed.

## 2026-05-15 21:33 MSK — scheduler-3 project analysis/backlog refinement

Analyzed current code/docs/issues for project state after latest context-aware guards, sequence delete, shortcut sync, non-modal UX, storage/export hardening, release gates and issue hygiene updates.

No new backlog issue created: observed gaps are already covered by existing planned/work issues.

Existing coverage confirmed:

- `work/032-context-aware-command-availability-and-diagram-type-guards.md` covers DB/sequence command availability and diagram-type mutation guards.
- `sprint_6/033-sync-human-requirements-shortcut-model.md` covers stale human requirements markdown still describing `Tab` / `Shift+Tab` as major-area navigation.
- `work/027-wire-missing-mvp-global-shortcuts.md` covers `Ctrl+Shift+P`, `Ctrl+N`, and `Ctrl+Y` redo hint/metadata drift.
- `sprint_3/019-wire-or-hide-declared-space-navigation-layout-commands.md` covers `Space G T`, `Space L S`, and `Space E` runtime/docs semantics drift.
- `sprint_2/015-non-modal-keyboard-flows-for-frequent-actions.md` covers remaining blocking `showAndWait()` flows in frequent keyboard paths.
- `sprint_3/021`, `sprint_3/022`, `work/030`, and parent `work/007` cover sequence model/storage/types, add/edit/select, delete, and full Sequence UI MVP.
- `sprint_4/025`, `sprint_2/016`, `sprint_3/023`, `sprint_2/017`, and `work/010` cover composite DB semantics, JSON/SVG escaping, scale smoke, reproducible build contract, and release gates.
- `work/024-dirty-state-current-file-and-unsaved-change-guard.md` covers current-file/dirty-state/updatedAt safety semantics.

Verification:

- `mvn -q test` with project-local Java/Maven and `SEE_REPO=http://89.223.121.28:8181` — passed.
- `git diff --check` — passed.

## 2026-05-15 22:02 MSK — scheduler-3 project analysis/backlog refinement

Analyzed current code/docs/issues for keyboard-first MVP gaps, context-aware command guards, sequence model/storage/export, dirty/open safety, non-modal UX, release gates, storage/export escaping, packaging scripts and issue hygiene.

No new backlog issue created: observed gaps are already covered by existing planned/work issues.

Existing coverage confirmed:

- `work/032-context-aware-command-availability-and-diagram-type-guards.md` covers DB/sequence command availability and diagram-type mutation guards; code now has `CommandRouter` context filtering and direct DB/sequence guards, but F1/shortcut/help scoping remains part of the active issue.
- `sprint_6/033-sync-human-requirements-shortcut-model.md` covers stale human requirements markdown still describing `Tab` / `Shift+Tab` as major-area navigation.
- `sprint_2/015-non-modal-keyboard-flows-for-frequent-actions.md` covers remaining blocking `showAndWait()` flows in palette/find/rename/delete/join preview.
- `sprint_3/019-wire-or-hide-declared-space-navigation-layout-commands.md` covers declared-but-unexecuted `Space G T` / `Space L S` and `Space E` semantics drift.
- `sprint_3/021`, `sprint_3/022`, `work/030`, and parent `work/007` cover sequence properties/types/storage/validation, add/edit/select, delete, and full Sequence UI MVP.
- `work/001-open-project-loading.md` and `work/024-dirty-state-current-file-and-unsaved-change-guard.md` cover project load plus current-file/dirty/unsaved-change safety.
- `sprint_2/016`, `sprint_4/025`, `sprint_3/023`, `sprint_2/017`, `work/010`, and `sprint_1/011` cover escaping, composite DB semantics, scale smoke, reproducible local Maven gate, tests/release gates, and cross-platform launch smoke.
- `sprint_5/031-normalize-issue-status-and-sprint-indexes.md` is the existing coverage for issue status/path drift; no duplicate created.

Verification:

- `mvn -q test` with project-local Java/Maven and `SEE_REPO=http://89.223.121.28:8181` — passed.
- `git diff --check` — passed.

## 2026-05-16 13:16 MSK — scheduler-3 project analysis/backlog refinement

Analyzed current code/docs/issues for SchemeOnYou after latest sequence, command-context, dirty/open, shortcut-sync, release-gate and issue-hygiene updates.

No new backlog issue created: observed gaps remain covered by existing planned/work issues.

Existing coverage confirmed:

- `work/032-context-aware-command-availability-and-diagram-type-guards.md` covers context-aware palette/Space/F1 availability and diagram-type mutation guards; runtime already has `CommandRouter`/`SpaceCommandSheet` scoping and direct DB/sequence guards, remaining help/shortcut polish stays in that active issue.
- `sprint_3/021-sequence-domain-types-storage-validation.md` covers missing sequence participant/message type/order fields and sequence validation; current model/storage still has only participant name plus message from/to/label/activation and validator skips sequence diagrams.
- `sprint_3/022-sequence-keyboard-creation-and-inspector-editing.md`, `work/030-sequence-delete-undo-safe.md`, and parent `work/007-sequence-diagram-mvp-ui.md` cover sequence add/edit/select/delete/render/export MVP slices.
- `sprint_2/015-non-modal-keyboard-flows-for-frequent-actions.md` covers remaining blocking `showAndWait()` flows in palette/find/rename/delete/join/open-error paths.
- `sprint_2/016-harden-json-and-svg-string-escaping.md` covers JSON/SVG escaping gaps for control/XML-significant characters.
- `sprint_4/025-db-composite-constraints-and-relation-semantics.md`, `sprint_3/023-mvp-scale-performance-smoke.md`, `sprint_2/017-release-readiness-environment-and-ci-parity.md`, `work/010-tests-and-release-gates.md`, and `sprint_1/011-packaging-and-cross-platform-launch.md` cover DB relation semantics, scale smoke, reproducible test/package contract, release gates, and launch smoke.
- `backlog/026-extract-application-presenters-for-headless-ui-tests.md` remains the L-sized architecture debt for the still-large `SchemeOnYouApplication` (~2148 LOC), with already extracted command/document/canvas seams.

Verification:

- `git diff --check` — passed.
- `mvn -q test` with project-local Java/Maven and `SEE_REPO=http://89.223.121.28:8181` — blocked by generated `client/target/classes/.../theme.css` owned by user `see`: Maven resources copy fails with `Operation not permitted`. This is an environment/build-output ownership blocker, not a new product gap; covered by release-readiness/env parity work (`017`/`010`).

## 2026-05-16 13:33 MSK — scheduler-3 project analysis/backlog refinement

Analyzed current code/docs/issues for shortcut consistency, sequence/DB MVP gaps, non-modal UX, release gates, storage/export hardening, active backlog and root design tooling drift.

New backlog issue created:

- `machine/issue/backlog/034-sync-root-figma-plugin-runtime-shortcuts.md` — mid/XS: sync root `figma-plugin/code.js` active shortcut hints from stale `F6` / `Shift+F6` to current runtime `0/1/2/3` major-area focus model.

Rationale: existing `014`/`018` synchronized machine/design/docs/mockup copies, and `033` covers human requirements markdown. The root Figma plugin source remains stale (`F6 next area`, `F6 / Shift+F6` active shortcut) and can regenerate misleading design mockups; this is not covered by `033`.

Existing coverage confirmed:

- `sprint_6/033-sync-human-requirements-shortcut-model.md` covers stale `requirements/*.md` Tab/F6 shortcut text.
- `sprint_3/019-wire-or-hide-declared-space-navigation-layout-commands.md` covers `Space E`, `Space G T`, and `Space L S` runtime/docs command drift.
- `sprint_2/015-non-modal-keyboard-flows-for-frequent-actions.md` covers remaining blocking `showAndWait()` editor flows.
- `sprint_3/021`, `sprint_3/022`, `work/030`, and parent `work/007` cover sequence model/storage/types, add/edit/select, delete, and full Sequence UI MVP.
- `sprint_4/025`, `sprint_2/016`, `sprint_3/023`, `sprint_2/017`, and `work/010` cover composite DB semantics, JSON/SVG escaping, scale smoke, reproducible local Maven contract, and release gates.
- `work/001`, `work/024`, `work/028`, and `work/032` cover open/load safety, dirty/current-file guard, expanded find/navigation, and diagram-type command guards.

Verification:

- `git diff --check` — pass.

## 2026-05-16 14:01 MSK — scheduler-3 project analysis/backlog refinement

Analyzed current code/docs/issues after latest Figma shortcut sync, sequence/delete/context guards, storage/export hardening and release-gate updates.

No new backlog issue created: observed gaps are already covered by existing planned/work issues.

Existing coverage confirmed:

- `backlog/026-extract-application-presenters-for-headless-ui-tests.md` remains the only backlog item and is L umbrella debt; already decomposed into completed `026-01`, `026-02`, `026-03` slices. Next seam needs explicit choice before adding another child.
- `sprint_2/015-non-modal-keyboard-flows-for-frequent-actions.md` covers remaining frequent blocking `showAndWait()` flows: palette, find, rename/edit, delete/join confirmations and error alerts.
- `sprint_2/016-harden-json-and-svg-string-escaping.md` covers writer/export escaping hardening currently in progress.
- `sprint_2/017-release-readiness-environment-and-ci-parity.md` and `work/010-tests-and-release-gates.md` cover reproducible build/test/package contract and the current target-output ownership blocker.
- `sprint_3/019-wire-or-hide-declared-space-navigation-layout-commands.md` covers active `Space G T`, `Space L S`, and `Space E` semantic drift still visible in runtime/design sources.
- `sprint_3/021`, `sprint_3/022`, `work/030`, and parent `work/007` cover sequence type/order/storage/validation, keyboard add/edit/select, delete, and full Sequence UI MVP closure.
- `sprint_4/025` covers DB composite constraint/relation semantics; `sprint_3/023` covers MVP scale/performance smoke; `sprint_6/033` covers remaining human requirements shortcut-model drift.
- `work/028` covers expanded find/go-linked navigation, and `work/032` covers context-aware command availability/guards.

Verification:

- `git diff --check` — passed.
- `mvn -q test` with project-local Java/Maven and `SEE_REPO=http://89.223.121.28:8181` — blocked by generated `client/target/classes/see/schemeonyou/ui/theme.css` ownership/permission (`Operation not permitted`). This is the same environment/build-output blocker noted earlier, not a new product gap.

## 2026-05-16 14:33 MSK — scheduler-3 project analysis/backlog refinement

Analyzed current code/docs/issues after latest root Figma shortcut sync and sprint/backlog refresh.

No new backlog issue created: observed gaps are already covered by existing planned/work issues.

Existing coverage confirmed:

- `machine/issue/backlog/026-extract-application-presenters-for-headless-ui-tests.md` remains the only backlog item; it is L-sized umbrella architecture debt for the still-large `SchemeOnYouApplication` (~2200 LOC) and should not be moved whole without choosing another seam.
- `sprint_6/033-sync-human-requirements-shortcut-model.md` covers the remaining active requirements drift: `requirements/requirements.md` and `requirements/keyboard-only-analysis.md` still present Tab/Shift+Tab as major-area navigation.
- `sprint_3/019-wire-or-hide-declared-space-navigation-layout-commands.md` covers active `Space G T`, `Space L S`, and `Space E` runtime/docs command drift still visible in `ShortcutMap`, help text and design summaries.
- `sprint_2/015-non-modal-keyboard-flows-for-frequent-actions.md` covers remaining frequent `showAndWait()` dialogs: open/save/export errors, rename/find, delete/join confirmations.
- `sprint_3/021`, `sprint_3/022`, `work/030`, and parent `work/007` cover sequence type/order/storage/validation, keyboard add/edit/select, delete and full Sequence UI MVP closure.
- `work/032` covers DB/sequence command guards and context-aware help; current code has `CommandRouter`/`SpaceCommandSheet` scoping but still needs issue closure/polish.
- `sprint_2/017` and `work/010` cover the current Maven build-output ownership blocker under `client/target/classes/.../theme.css`.
- `sprint_4/025`, `sprint_3/023`, `sprint_2/016`, `sprint_1/011`, `work/001`, `work/024`, and `work/028` cover DB composite semantics, scale smoke, escaping, packaging, open/load, dirty/current-file guard, and expanded find/go-linked navigation.

Verification:

- `git diff --check` — passed.
- `mvn -q test` with project-local Java/Maven and `SEE_REPO=http://89.223.121.28:8181` — blocked by generated `client/target/classes/see/schemeonyou/ui/theme.css` owned by user `see` (`Operation not permitted`). Same environment/build-output blocker as previous pass; not a new product gap.

## 2026-05-16 15:01 MSK — scheduler-3 project analysis/backlog refinement

Analyzed current code/docs/issues after latest root Figma shortcut sync, context-aware command guards, sequence/delete work, release-gate and issue-hygiene updates.

No new backlog issue created: observed gaps are already covered by existing planned/work issues.

Existing coverage confirmed:

- `machine/issue/backlog/026-extract-application-presenters-for-headless-ui-tests.md` remains the only backlog item; it is L-sized umbrella architecture debt for `SchemeOnYouApplication` (~2200 LOC), with child seams `026-01`/`026-02`/`026-03` already split. Next child seam needs an explicit choice before creating another task.
- `sprint_6/033-sync-human-requirements-shortcut-model.md` covers remaining requirements drift where human markdown still presents `Tab` / `Shift+Tab` as major-area navigation.
- `sprint_3/019-wire-or-hide-declared-space-navigation-layout-commands.md` covers active Space-command drift: `Space G T`, `Space L S`, and `Space E` semantics across runtime/design/docs.
- `sprint_2/015-non-modal-keyboard-flows-for-frequent-actions.md` covers remaining blocking `showAndWait()` paths: open/save/export errors, rename/find, delete/join confirmations.
- `sprint_3/021`, `sprint_3/022`, `work/030`, and parent `work/007` cover sequence participant/message type/order/storage/validation, keyboard add/edit/select, delete and full Sequence UI MVP closure.
- `work/032` covers DB/sequence command availability and mutation guards; current code has `CommandRouter` and `SpaceCommandSheet` scoping plus direct guards, but issue closure/polish remains active work.
- `sprint_2/017` and `work/010` cover reproducible Maven test/package gates and the current generated target-output ownership blocker under `client/target/classes/.../theme.css`.
- `sprint_4/025`, `sprint_3/023`, `sprint_2/016`, `sprint_1/011`, `work/001`, `work/024`, and `work/028` cover DB composite semantics, scale smoke, escaping, packaging, open/load, dirty/current-file guard, and expanded find/go-linked navigation.

Verification:

- `git diff --check` — passed.
- `mvn -q test` with project-local Java/Maven and `SEE_REPO=http://89.223.121.28:8181` — blocked by generated `client/target/classes/see/schemeonyou/ui/theme.css` ownership/permission (`Operation not permitted`). Same environment/build-output blocker as previous passes; not a new product gap.

## 2026-05-16 15:03 MSK — SEE decision for 026 next seam

SEE answered the open `026` decomposition question: next presenter seam is **inspector, editing**.

Updated planning artifacts:

- Added `machine/issue/sprint_6/026-04-inspector-editing-presenter-slice.md` — mid/M planned child task under parent `026`.
- Updated `machine/issue/backlog/026-extract-application-presenters-for-headless-ui-tests.md` with the new decomposition/clarification.
- Updated `machine/issue/sprint_6/cost.md` with the sprint_6 addition.

Scope note: keep the slice focused on inspector model + editing decisions; do not include file dialogs/errors or canvas rendering.

## 2026-05-16 15:34 MSK — scheduler-3 project analysis/backlog refinement

Analyzed current code/docs/issues after latest inspector/editing presenter seam and new root UI/focus backlog entries.

New backlog issue created:

- `machine/issue/backlog/039-normalize-root-issue-backlog-into-machine-tree.md` — high/S: normalize root `issue/backlog/035-038` into canonical `machine/issue` planning tree or explicitly mark/mirror ownership.

Rationale: project instructions and scheduler backlog use `machine/issue/`, but four new UI/focus requirements currently exist only under root `issue/backlog/`. Without normalization, high-priority functional-area requirements can be missed by sprint planning; directly copying them would risk duplicate active tasks, so a planning hygiene task captures the safe migration/ownership decision.

Existing coverage confirmed:

- `machine/issue/backlog/026-extract-application-presenters-for-headless-ui-tests.md` remains the L umbrella presenter debt; new child `work/026-04-inspector-editing-presenter-slice.md` is active after SEE's 15:03 decision.
- `sprint_2/015` covers remaining non-modal `showAndWait()` editor flows; code still has blocking alerts/dialogs for open errors, join preview, rename, delete, find and save/export errors.
- `sprint_3/019` covers active Space-command drift: `Space G T`, `Space L S`, and `Space E` semantics still appear in ShortcutMap/runtime/docs.
- `sprint_3/021`, `sprint_3/022`, `work/030`, and parent `work/007` cover sequence type/order/storage/validation, keyboard add/edit/select, delete and full Sequence UI MVP closure.
- `sprint_4/025`, `sprint_3/023`, `sprint_2/016`, `sprint_2/017`, `work/010`, `work/001`, `work/024`, `work/028`, and `work/032` cover DB composite semantics, scale smoke, escaping, release gates/env parity, open/load, dirty/current-file guard, expanded find/go-linked navigation and context-aware command guards.

Verification:

- `git diff --check` — passed.
- `mvn -q test` with project-local Java/Maven and `SEE_REPO=http://89.223.121.28:8181` — blocked by generated `client/target/classes/see/schemeonyou/ui/theme.css` owned by user `see` (`Operation not permitted`). Same environment/build-output blocker as previous passes; not a new product gap.

## 2026-05-16 15:44 MSK — SEE decisions for parent/L questions

SEE answered the open parent/L questions from the latest analysis:

- `026` — close parent; no additional presenter seam required after inspector/editing as a parent-closing condition.
- `007` — close parent; no separate sequence UI polish required as a parent-closing condition.
- `012` — enough as previously captured; no extra work added.

Updated issue metadata: `026` parent status set to done, `007` parent status set to done, `012` annotated with the no-extra-work clarification.

## 2026-05-16 16:01 MSK — scheduler-3 project analysis/backlog refinement

Analyzed current code/docs/issues after root backlog normalization and SEE's parent/L decisions for `026`, `007`, and `012`.

No new backlog issue created: newly visible UI/focus requirements are already normalized into canonical backlog as `035`-`038`, and other observed gaps remain covered by active planned/work issues.

New/updated coverage confirmed:

- `machine/issue/backlog/035-keyboard-tab-stays-inside-functional-area.md` — high/S: scoped Tab/Shift+Tab inside current functional area only.
- `machine/issue/backlog/036-empty-background-and-padded-functional-containers.md` — mid/S: empty background layer plus padded functional containers.
- `machine/issue/backlog/037-focus-first-object-on-functional-area-switch.md` — high/S: focus/select first available object after `0`/`1`/`2`/`3` area switch.
- `machine/issue/backlog/038-functional-area-fieldsets-with-names.md` — mid/S: fieldset-like visual grouping and names for functional areas.
- `machine/issue/work/039-normalize-root-issue-backlog-into-machine-tree.md` is done; root `issue/backlog/035`-`038` are non-active mirrors pointing to canonical machine backlog copies.
- `machine/issue/backlog/026-extract-application-presenters-for-headless-ui-tests.md` remains in backlog path but status is `done` per SEE 15:44 decision; active child `work/026-04` still needs validation/closure.
- Existing active/planned coverage remains: `015` for modal `showAndWait()` flows, `019` for `Space G T`/`Space L S`/`Space E` drift, `021`/`022`/`030` for sequence details, `017`/`010` for build/release gates, `025` for composite DB semantics, `023` scale smoke, `001` open/load, `024` dirty/current-file, `028` find/navigation, `032` command guards, `033` human requirements shortcut sync.

Verification:

- `git diff --check` — passed.
- `mvn -q test` with project-local Java/Maven and `SEE_REPO=http://89.223.121.28:8181` — blocked by generated `client/target/classes/see/schemeonyou/ui/theme.css` ownership/permission (`Operation not permitted`). Same environment/build-output blocker; covered by `017`/`010`, not a new product backlog item.

## 2026-05-16 16:30 MSK — scheduler-3 project analysis/backlog refinement

Analyzed current code/docs/issues for MVP coverage after sprint_8 focus tasks were planned.

No new backlog issue created: observed gaps are already covered by existing planned/work issues; creating more files would duplicate active work.

Coverage confirmed:

- `machine/issue/sprint_8/035-keyboard-tab-stays-inside-functional-area.md` — high/S: Tab/Shift+Tab scoped inside functional area.
- `machine/issue/sprint_8/036-empty-background-and-padded-functional-containers.md` — mid/S: empty background layer and padded functional containers.
- `machine/issue/sprint_8/037-focus-first-object-on-functional-area-switch.md` — high/S: first object focus/selection after `0`/`1`/`2`/`3` area switch.
- `machine/issue/sprint_8/038-functional-area-fieldsets-with-names.md` — mid/S: named fieldset-like functional-area grouping.
- `sprint_2/015` covers remaining blocking `showAndWait()` editor flows; current code still has blocking native/dialog paths for open errors, join/delete confirmations, rename/find and save/export errors.
- `sprint_3/019` covers active Space-command/documentation drift for `Space G T`, `Space L S`, and `Space E` semantics.
- `sprint_3/021`, `sprint_3/022`, `work/030`, and done parent `work/007` cover sequence type/order/storage/validation, add/edit/select/delete and MVP sequence closure.
- `sprint_2/017` and `work/010` cover reproducible build/test/package gates and the current Maven target ownership blocker.
- `sprint_4/025`, `sprint_3/023`, `sprint_2/016`, `sprint_1/011`, `work/001`, `work/024`, `work/028`, `work/032`, and `sprint_6/033` cover DB composite semantics, scale smoke, escaping, packaging, open/load, dirty/current-file guard, find/navigation, command guards and human requirements shortcut sync.

Verification:

- `mvn -q test` with project-local Java/Maven and `SEE_REPO=http://89.223.121.28:8181` — blocked by generated `client/target/classes/see/schemeonyou/ui/theme.css` owned by user `see` (`Operation not permitted`). Same environment/build-output blocker; covered by `017`/`010`, not a new product backlog item.

## 2026-05-16 16:53 MSK — backlog done cleanup

SEE requested moving all `done` tasks from backlog into the latest sprint. Moved closed parent `026-extract-application-presenters-for-headless-ui-tests.md` from `machine/issue/backlog/` to `machine/issue/sprint_8/`. Root `issue/backlog` had no `status - done` tasks.

## 2026-05-16 17:03 MSK — scheduler-3 project analysis/backlog refinement

Analyzed current code/docs/issues after sprint_8 focus work started and root backlog changed.

New backlog issue created:

- `machine/issue/backlog/040-apply-monaspace-krypton-font-weights.md` — mid/S: apply Monaspace Krypton as main UI font, with Extra Light 200 for body/content and Regular 400 for headings/functional-area titles.

Rationale: new SEE requirement existed only in root `issue/backlog/040`, while canonical scheduler backlog is `machine/issue/backlog`. Current runtime and design artifacts still use `System`, `Monospaced`, `Inter`, and JetBrains Mono font declarations; no existing machine issue covered this typography requirement. Root `issue/backlog/040` was marked as a non-active mirror pointing to the canonical machine issue.

Existing coverage confirmed:

- `work/035-keyboard-tab-stays-inside-functional-area.md` covers current Tab/Shift+Tab scoped focus work; scheduler-2 has moved it to work and implemented the scene-level trap.
- `sprint_8/037`, `sprint_8/036`, and `sprint_8/038` cover remaining focus-first, padded containers/background, and named fieldset-like functional-area UI requirements.
- `sprint_2/015` covers remaining blocking `showAndWait()` paths.
- `sprint_3/019` covers active Space-command drift for `Space G T`, `Space L S`, and `Space E`.
- `sprint_3/021`, `sprint_3/022`, done `work/030`, and done parent `work/007` cover sequence model/storage/validation, add/edit/select/delete and MVP sequence closure.
- `sprint_2/017` and `work/010` cover reproducible build/test/package gates and the known Maven target-output ownership blocker.

Verification:

- `git diff --check -- machine/issue/backlog/040-apply-monaspace-krypton-font-weights.md issue/backlog/040-apply-monaspace-krypton-font-weights.md machine/current/progress.md` — passed.
- Maven test was not rerun in this pass because the previous unchanged blocker is generated `client/target/classes/see/schemeonyou/ui/theme.css` ownership/permission; covered by `017`/`010`.

## 2026-05-16 17:33 MSK — scheduler-3 project analysis/backlog refinement

Analyzed current code/docs/issues after sprint_8 focus/font tasks were moved out of canonical backlog.

No new backlog issue created: `machine/issue/backlog/` is currently empty, and newly observed gaps are already covered by existing active/planned issues.

Coverage confirmed:

- `work/035` and `work/037` cover active functional-area focus work: Tab/Shift+Tab stays inside the current area; `0`/`1`/`2`/`3` moves to an area and focuses the first available object.
- `sprint_8/036`, `sprint_8/038`, and `sprint_8/040` cover padded/background containers, named fieldset-like functional areas, and Monaspace Krypton font/weight rollout.
- `sprint_6/033` covers stale human-readable requirements still saying Tab/Shift+Tab are focus-area shortcuts.
- `sprint_2/015` covers remaining modal/blocking `showAndWait()` flows.
- `sprint_3/019` covers remaining Space command semantic drift (`Space G T`, `Space G S`, `Space L S`, `Space E`).
- `work/001`, `work/024`, `work/028`, `work/032`, `sprint_3/021`, `sprint_3/022`, `work/030`, `sprint_4/025`, `sprint_3/023`, `sprint_2/016`, `sprint_2/017`, and `work/010` still cover open/load, dirty/current-file guard, find/navigation, command guards, sequence completion, DB relation semantics, scale smoke, escaping and release gates.

Estimates/priorities checked:

- No XL tasks found.
- L tasks already known: `work/010` remains in progress; `sprint_8/026` and `work/007` are done/closed parents per SEE decisions. No new L question added.
- Current highest priorities remain active high/M release gaps (`001`, `020`, `024`, `028`, `032`) plus active high/S focus tasks (`035`, `037`).

Verification:

- `git diff --check` — passed.
- `mvn -q -pl core test` with project-local Java/Maven — passed.
- Full `mvn -q test` is still blocked by generated `client/target/classes/see/schemeonyou/ui/theme.css` owned by user `see` (`Operation not permitted`). Same environment/build-output blocker; covered by `017`/`010`, not a new product issue.

## 2026-05-16 18:02 MSK — scheduler-3 project analysis/backlog refinement

Analyzed current code/docs/issues after latest sprint_8 focus/font planning and active work moves.

No new backlog issue created: `machine/issue/backlog/` is empty and newly observed gaps are already covered by existing active/planned issues.

Coverage confirmed:

- `work/035`, `work/036`, and `work/037` cover active functional-area focus/layout work: scoped Tab/Shift+Tab, padded/background containers, and first-object focus after `0`/`1`/`2`/`3` area switch.
- `sprint_8/038` and `sprint_8/040` cover named fieldset-like functional areas and Monaspace Krypton font/weight rollout.
- `sprint_6/033` covers remaining stale human-readable requirements where Tab/Shift+Tab are still described as major-area shortcuts.
- `sprint_2/015` covers remaining blocking `showAndWait()` paths: open/save/export errors, rename/find, delete/join confirmations.
- `sprint_3/019` covers active Space-command drift for `Space G T`, `Space L S`, and `Space E` semantics across runtime/design docs.
- `sprint_3/021`, `sprint_3/022`, done `work/030`, and done parent `work/007` cover sequence type/order/storage/validation, keyboard add/edit/select/delete and MVP sequence closure.
- `work/001`, `work/020`, `work/024`, `work/028`, `work/032`, `sprint_4/025`, `sprint_3/023`, `sprint_2/016`, `sprint_2/017`, `sprint_1/011`, and `work/010` still cover open/load, delete integration, dirty/current-file guard, find/navigation, command guards, DB composite semantics, scale smoke, escaping, release/env gates, packaging and tests/release gates.

Estimates/priorities checked:

- No XL tasks found.
- Only active L task remains `work/010-tests-and-release-gates.md` (already in progress); no new L-sized task/question discovered.
- Highest active release risks remain high/M work items: `001`, `020`, `024`, `028`, `032`, plus high/S focus tasks `035`/`037`.

Verification:

- `git diff --check` — passed.
- `mvn -q -pl core test` with project-local Java/Maven and `SEE_REPO=http://89.223.121.28:8181` — passed.
- `mvn -q test` with the same environment — passed.
## 2026-05-16 18:33 MSK — scheduler-3 project analysis/backlog refinement

Analyzed current code/docs/issues after sprint_8 functional-area work, sequence/context/delete/storage/release planning, and current backlog state.

No new backlog issue created: `machine/issue/backlog/` is empty and newly observed gaps are already covered by existing planned/work issues.

Updated existing issues:

- `machine/issue/work/035-keyboard-tab-stays-inside-functional-area.md` — status `done`; verified scoped Tab/Shift+Tab with local Maven gate.
- `machine/issue/work/037-focus-first-object-on-functional-area-switch.md` — status `done`; verified first-focus behavior with local Maven gate.
- `machine/issue/work/036-empty-background-and-padded-functional-containers.md` — status `done`; re-verified container/background/padding slice with local Maven/package gates.
- `machine/issue/sprint_8/cost.md` — updated sprint_8 status: UI/focus/layout pack `035`/`036`/`037`/`038` closed; next planned item is `040`.

Existing coverage confirmed:

- `sprint_8/040` covers Monaspace Krypton font/theme weights and includes license/source risk before bundling.
- `sprint_2/015` covers remaining blocking `showAndWait()` editor flows.
- `sprint_3/019` covers `Space G T`, `Space L S`, and `Space E` runtime/docs drift.
- `sprint_3/021` and `sprint_3/022` cover sequence type/order/storage/validation and add/edit/select flows; sequence delete parent is already done via `work/030`.
- `sprint_4/025`, `sprint_3/023`, `sprint_2/017`, `work/010`, `work/001`, `work/024`, `work/028`, and `work/032` cover DB composite semantics, scale smoke, reproducible release gates, open/load, dirty/current-file guard, expanded find/go-linked navigation, and context-aware command guards.
- `sprint_6/033` covers remaining human-readable requirements shortcut drift.

Verification:

- `mvn -q -pl client -am test` with project-local JDK/Maven and `SEE_REPO=http://89.223.121.28:8181` — passed.
- `mvn -q -DskipTests package` with the same environment — passed.
- `git diff --check` — passed.

## 2026-05-16 19:00 MSK — scheduler-3 project analysis/backlog refinement

Analyzed current code/docs/issues for MVP gaps after latest focus-area, sequence, shortcut, delete, non-modal, release and presenter-slice work.

New backlog issue created:

- `machine/issue/backlog/041-prune-or-wire-unused-ui-shell-stubs.md` — low/S: remove or explicitly wire unused UI shell/panel stubs (`ui/AppShell.java`, `ui/project/ProjectTreePanel.java`) left after presenter extraction.

Rationale: these classes compile but are not referenced by runtime/tests, while their names/state overlap with active `SchemeOnYouApplication` and presenter/controller architecture. This is small maintenance debt, not a release blocker, and is not covered by existing active MVP feature tasks.

Existing coverage confirmed:

- `work/032` covers diagram-type command guards.
- `sprint_2/015` covers remaining blocking `showAndWait()` editor flows.
- `sprint_3/019` covers advertised-but-unimplemented Space command drift (`Space G T`, `Space L S`, `Space E`).
- `sprint_3/021`, `sprint_3/022`, and `work/030` cover sequence types/storage/validation, add/edit/select and undo-safe delete.
- `work/028` covers expanded Find/linked navigation across MVP elements.
- `sprint_4/025`, `sprint_2/016`, `sprint_3/023`, `sprint_2/017`, `work/010`, and `sprint_6/033` cover composite DB semantics, escaping, scale smoke, reproducible verification, release gates, and human-readable shortcut docs.

Verification:

- `mvn -q test` with project-local Java/Maven and `SEE_REPO=http://89.223.121.28:8181` — passed.

## 2026-05-16 19:30 MSK — scheduler-3 project analysis/backlog refinement

Analyzed current code/docs/issues after sprint_8 functional-area closure and sprint_9 hygiene planning.

No new backlog issue created: `machine/issue/backlog/` is empty and newly observed gaps are already covered by existing planned/work issues.

What was analyzed:

- Canonical and root issue backlog: `machine/issue/backlog/`, root mirror `issue/backlog/`.
- Active/planned issues across `machine/issue/work/`, `sprint_1..9`.
- Requirements/design/machine drift for shortcut model, functional areas, non-modal flows and typography.
- Runtime code for known hotspots: blocking dialogs, font/theme literals, UI shell stubs, sequence/DB command coverage.

Coverage confirmed:

- `sprint_8/040-apply-monaspace-krypton-font-weights.md` covers remaining `System`/`Monospaced`/`Inter`/JetBrains Mono typography drift and license/source risk before bundling Monaspace Krypton.
- `sprint_9/041-prune-or-wire-unused-ui-shell-stubs.md` covers unused `ui/AppShell.java` / `ui/project/ProjectTreePanel.java` maintenance debt.
- `sprint_6/033-sync-human-requirements-shortcut-model.md` covers stale human requirements still describing `Tab` / `Shift+Tab` as major-area navigation.
- `sprint_2/015-non-modal-keyboard-flows-for-frequent-actions.md` covers remaining `showAndWait()` / `TextInputDialog` editor-flow debt.
- `sprint_3/019-wire-or-hide-declared-space-navigation-layout-commands.md` covers Space command drift (`Space G T`, `Space L S`, `Space E`).
- `work/001`, `work/020`, `work/024`, `work/028`, `work/032`, `sprint_3/021`, `sprint_3/022`, `sprint_4/025`, `sprint_3/023`, `sprint_2/016`, `sprint_2/017`, `sprint_1/011`, and `work/010` still cover open/load, delete integration, dirty/current-file guard, find/navigation, command guards, sequence, DB constraints, scale smoke, escaping, packaging and release gates.

Estimates/priorities checked:

- New backlog tasks: none.
- No XL tasks found.
- No new L tasks found; existing `work/010` remains the only active L/in-progress release-gate parent with previous SEE decisions recorded.
- Current planned backlog-like queue is `sprint_8/040` (mid/S) then `sprint_9/041` (low/S), while active high/M release gaps remain higher priority.

Verification:

- `mvn -q test` with project-local JDK/Maven and `SEE_REPO=http://89.223.121.28:8181` — passed.

## 2026-05-16 20:05 MSK — scheduler-3 project analysis/backlog refinement

Analyzed current code/docs/issues after sprint_8 typography work was completed.

No new backlog issue created: `machine/issue/backlog/` is empty and newly observed gaps are already covered by existing active/planned issues.

Updated existing mirror:

- `issue/backlog/040-apply-monaspace-krypton-font-weights.md` — root non-active mirror now points to canonical `machine/issue/work/040-apply-monaspace-krypton-font-weights.md` with `status - done` instead of stale `sprint_8` location.

Coverage confirmed:

- `work/040` covers Monaspace Krypton font family/weights, fallback and source/license note; remaining dev-only `Monospaced` key-log font is explicitly allowed by the issue.
- `sprint_2/015` covers remaining `showAndWait()` / `TextInputDialog` editor-flow debt.
- `sprint_3/019` covers Space-command drift (`Space G T`, `Space L S`, and `Space E` docs/runtime semantics).
- `work/028` covers broader find/go-linked navigation scope; `work/032` covers context-aware command availability/diagram-type guards.
- `sprint_3/021`, `sprint_3/022`, `sprint_4/025`, `sprint_3/023`, `sprint_2/016`, `sprint_2/017`, `work/010`, `work/001`, `work/020`, and `work/024` still cover sequence, DB constraints, scale, escaping, release gates, open/load, delete integration and dirty/current-file guard.

Estimates/priorities checked:

- New backlog tasks: none.
- No XL tasks found.
- No new L tasks found. Existing active L task remains `work/010-tests-and-release-gates.md`; no new question required this pass.
- Highest active release risks remain high/M work items: `001`, `020`, `024`, `028`, `032`, plus high/M `015` for non-modal keyboard flows.

Verification:

- `mvn -q test` with project-local JDK/Maven and `SEE_REPO=http://89.223.121.28:8181` — passed.
- `mvn -q -pl client -am test` with the same environment — passed.
- `git diff --check` — passed.

## 2026-05-16 20:32 MSK — scheduler-3 project analysis/backlog refinement

Analyzed current code/docs/issues after sprint_8 typography completion and sprint_9 stub cleanup work.

New backlog issue created:

- `machine/issue/backlog/042-normalize-machine-requirements-shortcut-labels.md` — mid/XS: clarify active `machine/requirements.json` shortcut labels so `Tab` / `Shift+Tab` are scoped in-area traversal, not generic next/previous focus that could be read as major-area navigation.

Rationale: `sprint_2/018` moved active machine policy to `0`/`1`/`2`/`3` and made F6 legacy, while `sprint_6/033` covers human-readable markdown. A residual structured ambiguity remains in `machine/requirements.json`: active keys `next_focus` / `previous_focus` still map to `Tab` / `Shift+Tab`. This is smaller than reopening done `018` and distinct from markdown-only `033`.

Existing coverage confirmed:

- `sprint_6/033-sync-human-requirements-shortcut-model.md` covers stale `requirements/*.md` text still saying Tab/Shift+Tab switch focus areas.
- `sprint_2/015-non-modal-keyboard-flows-for-frequent-actions.md` covers remaining `showAndWait()` / `TextInputDialog` editor-flow debt.
- `sprint_3/019-wire-or-hide-declared-space-navigation-layout-commands.md` covers Space-command drift (`Space G T`, `Space L S`, `Space E`).
- `work/041-prune-or-wire-unused-ui-shell-stubs.md` covers unused UI shell/panel stub cleanup.
- `work/028`, `work/032`, `sprint_3/021`, `sprint_3/022`, `sprint_4/025`, `sprint_3/023`, `sprint_2/016`, `sprint_2/017`, `work/010`, `work/001`, `work/020`, `work/024`, and `sprint_1/011` still cover find/navigation, command guards, sequence, DB constraints, scale, escaping, release/env gates, open/load, delete integration, dirty/current-file guard and packaging.

Estimates/priorities checked:

- New issue `042`: mid/XS.
- No XL tasks found.
- No new L task found. Existing active L parent remains `work/010-tests-and-release-gates.md`; no new L question required.

Verification:

- `python3 -m json.tool machine/requirements.json` — passed.
- `git diff --check -- machine/issue/backlog/042-normalize-machine-requirements-shortcut-labels.md machine/current/progress.md` — passed before progress append; changes are plain markdown only.

## 2026-05-16 21:01 MSK — scheduler-3 project analysis/backlog refinement

Analyzed current code/docs/issues after structured shortcut-label cleanup (`042`) and sprint_9 unused-stub cleanup (`041`).

No new backlog issue created: `machine/issue/backlog/` is empty and newly observed gaps are already covered by existing active/planned issues.

Updated existing issues:

- `machine/issue/work/041-prune-or-wire-unused-ui-shell-stubs.md` — status `done`; removed previous Maven-gate blocker after re-verifying with project-local JDK/Maven.
- `machine/issue/sprint_9/cost.md` — updated `041` location/status and recorded the 21:01 gate result.

Coverage confirmed:

- `sprint_6/033-sync-human-requirements-shortcut-model.md` covers stale human-readable requirements still saying `Tab` / `Shift+Tab` switch focus areas.
- `sprint_3/019-wire-or-hide-declared-space-navigation-layout-commands.md` covers active Space-command drift: `Space G T`, `Space L S`, and `Space E` runtime/docs semantics.
- `sprint_2/015-non-modal-keyboard-flows-for-frequent-actions.md` covers remaining blocking `showAndWait()` / `TextInputDialog` flows.
- `work/028` and `work/032` cover expanded find/go-linked navigation and context-aware command availability/guards.
- `sprint_3/021`, `sprint_3/022`, `sprint_4/025`, `sprint_3/023`, `sprint_2/016`, `sprint_2/017`, `work/010`, `work/001`, `work/020`, `work/024`, and `sprint_1/011` cover sequence completion, DB constraints, scale, escaping, release/env gates, open/load, delete integration, dirty/current-file guard, and packaging.

Estimates/priorities checked:

- New backlog tasks: none.
- No XL tasks found.
- No new L task found. Existing active L parent remains `work/010-tests-and-release-gates.md`; no new question required.
- Current highest release risks remain high/M work items: `001`, `020`, `024`, `028`, `032`, plus high/M `015` for non-modal keyboard flows.

Verification:

- Reference check for deleted stubs: no `AppShell`, `ProjectTreePanel`, or `diagramNames` references under `client/src` / `core/src`.
- `python3 -m json.tool machine/requirements.json machine/design.json machine/tasks.json` equivalent checks — passed.
- `SEE_REPO=http://89.223.121.28:8181 mvn -q -pl client -am test` — passed.
- `SEE_REPO=http://89.223.121.28:8181 mvn -q test` — passed.
- `git diff --check` — passed.

## 2026-05-16 21:31 MSK — scheduler-3 project analysis/backlog refinement

Analyzed current code/docs/issues after `020` delete integration re-verification and current empty canonical backlog.

No new backlog issue created: `machine/issue/backlog/` is empty and newly observed gaps are already covered by existing active/planned issues.

What was analyzed:

- Canonical backlog `machine/issue/backlog/` and root mirror backlog `issue/backlog/`.
- Active/planned/done issues across `machine/issue/work/` and `sprint_1..9`.
- Requirements/design/machine docs for shortcut drift, Space-command drift, sequence MVP scope, release gates and font/theme state.
- Runtime hotspots: blocking dialogs, sequence model/add/edit/delete paths, command palette/Space shortcuts, stale UI stubs, Monaspace/dev-only font usage.

Coverage confirmed:

- `sprint_2/015-non-modal-keyboard-flows-for-frequent-actions.md` covers remaining blocking `showAndWait()` / `TextInputDialog` flows.
- `sprint_3/019-wire-or-hide-declared-space-navigation-layout-commands.md` covers `Space G T`, `Space L S`, and `Space E` runtime/docs drift.
- `sprint_3/021-sequence-domain-types-storage-validation.md` covers missing sequence participant/message types, order and storage/validation semantics.
- `sprint_3/022-sequence-keyboard-creation-and-inspector-editing.md` covers remaining sequence add/edit/select inspector flow work.
- `sprint_6/033-sync-human-requirements-shortcut-model.md` covers stale human-readable requirements still saying `Tab` / `Shift+Tab` switch focus areas.
- `work/001`, `work/024`, `work/028`, `work/032`, `sprint_4/025`, `sprint_3/023`, `sprint_2/016`, `sprint_2/017`, `sprint_1/011`, and `work/010` cover open/load, dirty/current-file guard, find/navigation, command guards, DB semantics, scale, escaping, release/env gates, packaging and tests.

Estimates/priorities checked:

- New backlog tasks: none.
- No XL tasks found.
- No new L task found. Existing active L parent remains `work/010-tests-and-release-gates.md`; no new L question required this pass.
- Highest release risks remain high/M work items: `001`, `024`, `028`, `032`, plus high/M `015` and high/M sequence slices `021`/`022`.

Verification:

- `python3 -m json.tool machine/requirements.json machine/design.json machine/tasks.json` — passed.
- `SEE_REPO=http://89.223.121.28:8181 mvn -q -pl client -am test` — passed with project-local JDK/Maven.
- `git diff --check` — passed.

## 2026-05-16 22:01 MSK — scheduler-3 project analysis/backlog refinement

Analyzed current code/docs/issues after `041`/`042` closure, delete integration verification and current empty canonical backlog.

No new backlog issue created: `machine/issue/backlog/` is empty and newly observed gaps are already covered by existing active/planned issues.

What was analyzed:

- Canonical backlog `machine/issue/backlog/` and root mirror backlog `issue/backlog/`.
- Active/planned/done issues across `machine/issue/work/` and `sprint_1..9`.
- Requirements/design/machine docs for shortcut drift, Space-command drift, sequence MVP scope, release gates, font/theme and issue metadata hygiene.
- Runtime hotspots: blocking dialogs, sequence model/add/edit/delete paths, command palette/Space shortcuts, find/navigation, command guards and remaining large app-shell presenter seam.

Updated existing issues:

- `machine/issue/work/026-04-inspector-editing-presenter-slice.md` — normalized status spelling `in progress` → `in_progress` and corrected parent link from old backlog path to canonical `sprint_8/026-...`; scope/estimate unchanged.

Coverage confirmed:

- `sprint_2/015-non-modal-keyboard-flows-for-frequent-actions.md` covers remaining blocking `showAndWait()` / `TextInputDialog` flows.
- `sprint_3/019-wire-or-hide-declared-space-navigation-layout-commands.md` covers `Space G T`, `Space L S`, and `Space E` runtime/docs drift.
- `sprint_3/021-sequence-domain-types-storage-validation.md` and `sprint_3/022-sequence-keyboard-creation-and-inspector-editing.md` cover remaining sequence MVP details.
- `sprint_6/033-sync-human-requirements-shortcut-model.md` covers stale human-readable shortcut docs.
- `work/001`, `work/024`, `work/028`, `work/032`, `sprint_4/025`, `sprint_3/023`, `sprint_2/016`, `sprint_2/017`, `sprint_1/011`, `work/010`, and `work/026-04` cover open/load, dirty/current-file guard, find/navigation, command guards, DB semantics, scale, escaping, release/env gates, packaging, tests and inspector presenter extraction.

Estimates/priorities checked:

- New backlog tasks: none.
- No XL tasks found.
- No new L task found. Existing active L parent remains `work/010-tests-and-release-gates.md`; no new L question required.
- Highest release risks remain high/M work items: `001`, `028`, `032`, plus high/M `015` and sequence slices `021`/`022`.

Verification:

- `python3 -m json.tool machine/requirements.json machine/design.json machine/tasks.json` — passed.
- `SEE_REPO=http://89.223.121.28:8181 mvn -q -pl client -am test` with project-local JDK/Maven — passed.
- `git diff --check -- machine/issue/work/026-04-inspector-editing-presenter-slice.md machine/current/progress.md` — passed.

## 2026-05-16 22:30 MSK — scheduler-3 project analysis/backlog refinement

Analyzed current code/docs/issues after the 22:01 pass and current empty canonical backlog.

No new backlog issue created: `machine/issue/backlog/` remains empty; newly rechecked gaps are already covered by existing active/planned issues.

What was analyzed:

- Canonical backlog `machine/issue/backlog/` and root mirror backlog `issue/backlog/`.
- Active/planned/done issues across `machine/issue/work/` and `sprint_1..9`.
- Requirements/design/machine docs for shortcut drift, Space-command drift, sequence MVP scope, release gates and issue metadata hygiene.
- Runtime hotspots: blocking dialogs, sequence model/add/edit/delete paths, command palette/Space shortcuts, find/navigation, command guards, root mirror status and Monaspace/dev-only font usage.

Coverage confirmed:

- `sprint_2/015-non-modal-keyboard-flows-for-frequent-actions.md` covers remaining blocking `showAndWait()` / `TextInputDialog` flows.
- `sprint_3/019-wire-or-hide-declared-space-navigation-layout-commands.md` covers `Space G T`, `Space L S`, and `Space E` runtime/docs drift.
- `sprint_3/021-sequence-domain-types-storage-validation.md` and `sprint_3/022-sequence-keyboard-creation-and-inspector-editing.md` cover remaining sequence MVP model/storage/editing gaps.
- `sprint_6/033-sync-human-requirements-shortcut-model.md` covers stale human-readable requirements still saying `Tab` / `Shift+Tab` switch focus areas.
- `work/001`, `work/028`, `work/032`, `sprint_4/025`, `sprint_3/023`, `sprint_2/016`, `sprint_2/017`, `sprint_1/011`, `work/010`, and `work/026-04` cover open/load, find/navigation, command guards, DB constraints, scale, escaping, release/env gates, packaging, tests and inspector presenter extraction.
- Root `issue/backlog/*` files are non-active mirrors with `status - mirrored`; no duplicate active backlog item found there.

Estimates/priorities checked:

- New backlog tasks: none.
- No XL tasks found.
- No new L task found. Existing active L parent remains `work/010-tests-and-release-gates.md`; no new L question required.
- Highest release risks remain high/M work items: `001`, `028`, `032`, plus high/M `015` and sequence slices `021`/`022`.

Verification:

- `python3 -m json.tool machine/requirements.json machine/design.json machine/tasks.json` — passed.
- `SEE_REPO=http://89.223.121.28:8181 mvn -q -pl client -am test` with project-local JDK/Maven — passed.
- `git diff --check` — passed before this progress append.

## 2026-05-16 23:01 MSK — scheduler-3 project analysis/backlog refinement

Analyzed current code/docs/issues after `024` and `032` were re-verified/closed and canonical backlog remained empty.

No new backlog issue created: newly observed release warning is covered by existing planned `017`, so creating a duplicate would fragment release-readiness work.

What was analyzed:

- Canonical backlog `machine/issue/backlog/` and root mirror backlog `issue/backlog/`.
- Active/planned/done issues across `machine/issue/work/` and `sprint_1..9`.
- Requirements/design/machine docs for shortcut drift, Space-command drift, sequence MVP scope, release gates and issue metadata hygiene.
- Runtime hotspots: blocking dialogs, sequence storage/editing, command availability, dirty/open/load state, Space shortcuts and release build warnings.

Updated existing issue:

- `machine/issue/sprint_2/017-release-readiness-environment-and-ci-parity.md` — acceptance criteria extended to classify Java 25 / JavaFX native-access warnings or add agreed runtime/test/package `--enable-native-access=ALL-UNNAMED` equivalent.

Rationale:

- Local Maven gates pass with project-local JDK/Maven, but JavaFX tests print: `Restricted methods will be blocked in a future release unless native access is enabled`.
- This is a release verification/runtime flag risk, already aligned with `017` and `011`; no separate backlog issue needed.

Coverage confirmed:

- `sprint_2/015-non-modal-keyboard-flows-for-frequent-actions.md` covers remaining blocking `showAndWait()` / `TextInputDialog` flows.
- `sprint_3/019-wire-or-hide-declared-space-navigation-layout-commands.md` covers `Space G T`, `Space L S`, and `Space E` runtime/docs drift.
- `sprint_3/021` and `sprint_3/022` cover remaining sequence MVP model/storage/editing gaps.
- `sprint_6/033-sync-human-requirements-shortcut-model.md` covers stale human-readable requirements still saying `Tab` / `Shift+Tab` switch focus areas.
- `work/001`, `work/028`, `sprint_4/025`, `sprint_3/023`, `sprint_2/016`, `sprint_2/017`, `sprint_1/011`, `work/010`, and `work/026-04` cover open/load, find/navigation, DB constraints, scale, escaping, release/env gates, packaging, tests and inspector presenter extraction.

Estimates/priorities checked:

- New backlog tasks: none.
- Updated `017`: priority mid, cost M unchanged.
- No XL tasks found.
- No new L task found. Existing active L parent remains `work/010-tests-and-release-gates.md`; no new L question required.

Verification:

- `python3 -m json.tool machine/requirements.json machine/design.json machine/tasks.json` — passed.
- `JAVA_HOME=/home/openclaw/workspace/java/openjdk-25.0.2 PATH=/home/openclaw/workspace/java/apache-maven-3.9.11/bin:$JAVA_HOME/bin:$PATH SEE_REPO=http://89.223.121.28:8181 mvn -q -pl client -am test` — passed, with Java 25 native-access warnings noted above.
- `JAVA_HOME=/home/openclaw/workspace/java/openjdk-25.0.2 PATH=/home/openclaw/workspace/java/apache-maven-3.9.11/bin:$JAVA_HOME/bin:$PATH SEE_REPO=http://89.223.121.28:8181 mvn -q test` — passed, with same warnings.
- `JAVA_HOME=/home/openclaw/workspace/java/openjdk-25.0.2 PATH=/home/openclaw/workspace/java/apache-maven-3.9.11/bin:$JAVA_HOME/bin:$PATH SEE_REPO=http://89.223.121.28:8181 mvn -q -DskipTests package` — passed.
- `git diff --check -- machine/issue/sprint_2/017-release-readiness-environment-and-ci-parity.md machine/current/progress.md` — passed before this final progress append.

## 2026-05-16 23:31 MSK — scheduler-3 project analysis/backlog refinement

Analyzed current code/docs/issues after scheduler-2 completed `033` requirements shortcut sync and current canonical backlog remained empty.

No new backlog issue created: newly rechecked gaps are already covered by existing active/planned issues.

What was analyzed:

- Canonical backlog `machine/issue/backlog/` and root mirror backlog `issue/backlog/`.
- Active/planned/done issues across `machine/issue/work/` and `sprint_1..9`.
- Requirements/design/machine docs after `033` sync for `0/1/2/3`, scoped `Tab`/`Shift+Tab`, Space-command drift, sequence MVP scope, release gates and issue metadata hygiene.
- Runtime hotspots: remaining blocking dialogs, sequence add/edit/delete paths, command palette/Space shortcuts, find/navigation, command guards, release build warnings, and the remaining large app-shell/presenter seam.

Coverage confirmed:

- `sprint_2/015-non-modal-keyboard-flows-for-frequent-actions.md` covers remaining blocking `showAndWait()` / `TextInputDialog` flows; command palette slice is noted there, while find/rename/delete/join confirmations remain in scope.
- `sprint_3/019-wire-or-hide-declared-space-navigation-layout-commands.md` covers active `Space G T`, `Space L S`, and `Space E` runtime/docs drift.
- `sprint_3/021-sequence-domain-types-storage-validation.md` and `sprint_3/022-sequence-keyboard-creation-and-inspector-editing.md` cover remaining sequence participant/message type/order/storage/editing semantics.
- `sprint_4/025-db-composite-constraints-and-relation-semantics.md` covers composite DB constraints and derived relation semantics.
- `work/001`, `work/028`, `work/013`, `work/014`, `work/026-04`, `sprint_2/016`, `sprint_2/017`, `sprint_3/023`, `sprint_1/011`, and `work/010` cover open/load, expanded find/navigation, drag/drop convenience, theme, inspector presenter extraction, escaping, release/env gates, scale smoke, packaging and tests.
- Root `issue/backlog/*` files remain non-active mirrors; canonical machine issues are already done or tracked in `machine/issue/work/`.

Estimates/priorities checked:

- New backlog tasks: none.
- No XL tasks found.
- No new L task found. Existing active L parent remains `work/010-tests-and-release-gates.md`; no new L question required.
- Current highest release risks remain high/M work items: `001`, `028`, `014`, plus high/M planned/work slices `015`, `021`, `022`, `025`.

Verification:

- `python3 -m json.tool machine/requirements.json machine/design.json machine/tasks.json` — passed.
- `SEE_REPO=http://89.223.121.28:8181 mvn -q -pl client -am test` with project-local JDK/Maven — passed; Java 25/JavaFX native-access warnings remain and are already covered by `017`.
- `SEE_REPO=http://89.223.121.28:8181 mvn -q test` with project-local JDK/Maven — passed; same warnings.
- `git diff --check` — passed before this progress append.

## 2026-05-17 00:01 MSK — scheduler-3 project analysis/backlog refinement

Analyzed current code/docs/issues after scheduler-2 completed requirements shortcut sync (`033`) and current canonical backlog had no active items.

New backlog issue created:

- `machine/issue/backlog/043-prune-unused-focus-traversal-cycle.md` — mid/S: remove or update unused `FocusTraversal` cyclic major-area helper that still models next/previous area traversal despite current direct `0`/`1`/`2`/`3` policy.

Rationale:

- Runtime focus flow now uses direct major-area shortcuts and `FocusAreaNavigator` for scoped in-area Tab traversal.
- `FocusTraversal.java` is not referenced outside itself and exposes `nextMajorArea()` / `previousMajorArea()`, which can mislead future focus/navigation work back toward the replaced cyclic major-area model.
- Existing done tasks `033`, `035`, `037`, and `042` fixed docs/runtime/structured shortcut policy; this is a small non-duplicate cleanup of stale runtime source.

Coverage confirmed:

- `sprint_2/015-non-modal-keyboard-flows-for-frequent-actions.md` covers remaining blocking `showAndWait()` / `TextInputDialog` flows.
- `sprint_3/019-wire-or-hide-declared-space-navigation-layout-commands.md` covers active `Space G T`, `Space L S`, and `Space E` runtime/docs drift.
- `sprint_3/021` and `sprint_3/022` cover remaining sequence MVP model/storage/editing gaps.
- `sprint_4/025`, `sprint_3/023`, `sprint_2/016`, `sprint_2/017`, `sprint_1/011`, `work/001`, `work/010`, `work/013`, `work/014`, `work/026-04`, and `work/028` cover DB semantics, scale, escaping, release/env gates, packaging, open/load, tests, drag/drop, theme, inspector presenter extraction and expanded find/navigation.

Estimates/priorities checked:

- New issue `043`: priority mid, cost S.
- No XL tasks found.
- No new L task found. Existing active L parent remains `work/010-tests-and-release-gates.md`; no new L question required.
- Highest release risks remain high/M planned/work items: `015`, `021`, `022`, `025`, plus active `001`, `014`, `028`.

Verification:

- Reference check: `FocusTraversal` appears only in its own source file; `FocusAreaNavigator` remains used by runtime/tests.
- `python3 -m json.tool machine/requirements.json machine/design.json machine/tasks.json` — passed.
- `git diff --check -- machine/issue/backlog/043-prune-unused-focus-traversal-cycle.md machine/current/progress.md` — passed before this progress append.

## 2026-05-17 00:31 MSK — scheduler-3 project analysis/backlog refinement

Analyzed current code/docs/issues after scheduler-2 completed `043` focus traversal cleanup and current canonical backlog remained empty.

No new backlog issue created: newly rechecked gaps are already covered by existing active/planned issues, and `machine/issue/backlog/` has no active files.

What was analyzed:

- Canonical backlog `machine/issue/backlog/` and root mirror backlog `issue/backlog/`.
- Active/planned/done issues across `machine/issue/work/` and `sprint_1..10`.
- Requirements/design/machine docs for direct `0`/`1`/`2`/`3` area navigation, scoped Tab traversal, legacy F6 notes, Space-command drift, sequence MVP scope, release gates and issue hygiene.
- Runtime hotspots: remaining blocking dialogs, sequence model/add/edit/delete paths, command palette/Space shortcuts, find/navigation, command guards, release warnings and focus traversal cleanup.

Coverage confirmed:

- `work/043-prune-unused-focus-traversal-cycle.md` is done; `FocusTraversal`/`nextMajorArea`/`previousMajorArea` no longer appear in `client/src` or `core/src` outside issue/progress notes.
- `sprint_2/015-non-modal-keyboard-flows-for-frequent-actions.md` covers remaining blocking `showAndWait()` / `TextInputDialog` flows.
- `sprint_3/019-wire-or-hide-declared-space-navigation-layout-commands.md` covers active `Space G T`, `Space L S`, and `Space E` runtime/docs drift.
- `sprint_3/021` and `sprint_3/022` cover remaining sequence participant/message type/order/storage/editing semantics.
- `sprint_4/025`, `sprint_3/023`, `sprint_2/016`, `sprint_2/017`, `sprint_1/011`, `work/001`, `work/010`, `work/013`, `work/014`, `work/026-04`, and `work/028` cover DB semantics, scale, escaping, release/env gates, packaging, open/load, tests, drag/drop, theme, inspector presenter extraction and expanded find/navigation.

Estimates/priorities checked:

- New backlog tasks: none.
- No XL tasks found.
- No new L task found. Existing active L parent remains `work/010-tests-and-release-gates.md`; previously answered scope is still sufficient, so no new L question required.
- Highest release risks remain high/M planned/work items: `015`, `021`, `022`, `025`, plus active `001`, `014`, `028`.

Verification:

- `python3 -m json.tool machine/requirements.json machine/design.json machine/tasks.json` — passed.
- `SEE_REPO=http://89.223.121.28:8181 mvn -q -pl client -am test` with project-local JDK/Maven — passed; Java 25/JavaFX native-access warnings remain and are already covered by `017`.
- `git diff --check` — passed before this progress append.

## 2026-05-17 00:50 MSK — Tab order clarification

SEE clarified the existing keyboard navigation requirement: `Tab` moves focus to the next element in order, but only within the current functional group. Updated canonical issue `machine/issue/work/035-keyboard-tab-stays-inside-functional-area.md` and its root mirror; no duplicate issue created because the requirement is already covered by `035`.

## 2026-05-17 01:32 scheduler-3 backlog analysis

Analyzed current code/docs/issues for SchemeOnYou MVP coverage after latest shortcut/focus/layout cleanup and sequence/search work.

Checked:
- `requirements/requirements.md`, `requirements/keyboard-only-analysis.md`, `machine/current/{progress,plan,consistency-check}.md`, `machine/ai-brief.md`.
- Active/planned issues under `machine/issue/work`, `machine/issue/sprint_*`, and empty `machine/issue/backlog`.
- Runtime hotspots in `SchemeOnYouApplication`, command routing/palette, storage reader/writer, sequence model, validator, shortcut/help and blocking-dialog flows.

No new backlog issue created in this pass. Observed gaps are already covered by existing issues:
- `sprint_3/021` covers sequence participant/message type/order/storage/validation gaps.
- `sprint_3/022`, `work/030`, and done parent `work/007` cover sequence add/edit/select/delete/render/export scope.
- `work/028` covers expanded Find element and linked navigation across DB/sequence MVP elements.
- `sprint_2/015` covers remaining blocking editor dialogs/overlays, excluding native file choosers.
- `sprint_2/017`, `work/010`, and `sprint_1/011` cover reproducible Maven gate, JavaFX native-access warning classification, release gates and cross-platform launch packaging.
- `sprint_4/025`, `sprint_3/023`, and `sprint_2/016` cover DB composite relation semantics, MVP scale smoke and JSON/SVG escaping.
- `sprint_11/042` and `sprint_11/044` cover current visual polish requests.

L-sized active questions:
- `work/010-tests-and-release-gates.md` remains the only active L umbrella; prior SEE decisions stand: local Maven `compile + tests + package` is enough, UI smoke/manual checklist/GitHub Actions are not required for MVP gate.

Blockers:
- None for backlog creation. Build/test verification was not rerun in this analysis pass because no code/backlog task files were changed.

## 2026-05-17 01:41 MSK — canonical backlog moved to machine/issue/backlog

SEE requested using `/home/openclaw/workspace/SchemeOnYou/machine/issue/backlog` as the backlog and moving everything out of root `issue/backlog`.

Result:
- Root `issue/backlog` emptied/removed.
- Files that already had canonical copies in `machine/issue/{work,sprint_*}` were removed from root as duplicate mirrors.
- Files without a machine canonical copy were moved into `machine/issue/backlog`: `045`, `046`, `047`, `048`, `049`, `050`.

## 2026-05-17 01:46 MSK — work folder cleanup

SEE requested analysis/cleanup of `machine/issue/work`: completed tasks were removed from `work`, unfinished tasks were moved back to `machine/issue/backlog` with `status - backlog`.

Removed done tasks from `work`:
- `007-sequence-diagram-mvp-ui.md`
- `020-integrate-delete-controller-for-column-and-fk-selection.md`
- `024-dirty-state-current-file-and-unsaved-change-guard.md`
- `026-01-command-router-presenter-slice.md`
- `026-02-document-lifecycle-guard-presenter-slice.md`
- `026-03-canvas-hit-test-presenter-slice.md`
- `027-wire-missing-mvp-global-shortcuts.md`
- `030-sequence-delete-undo-safe.md`
- `032-context-aware-command-availability-and-diagram-type-guards.md`
- `033-sync-human-requirements-shortcut-model.md`
- `034-sync-root-figma-plugin-runtime-shortcuts.md`
- `035-keyboard-tab-stays-inside-functional-area.md`
- `036-empty-background-and-padded-functional-containers.md`
- `037-focus-first-object-on-functional-area-switch.md`
- `038-functional-area-fieldsets-with-names.md`
- `039-normalize-root-issue-backlog-into-machine-tree.md`
- `040-apply-monaspace-krypton-font-weights.md`
- `041-pin-left-menu-and-inspector-to-window-edges.md`
- `041-prune-or-wire-unused-ui-shell-stubs.md`
- `042-normalize-machine-requirements-shortcut-labels.md`
- `043-add-foreign-key-source-target-selection-flow.md`
- `043-prune-unused-focus-traversal-cycle.md`

Moved unfinished tasks to backlog:
- `001-open-project-loading.md`
- `010-tests-and-release-gates.md`
- `013-canvas-drag-and-drop-positioning.md`
- `014-apply-modern-dark-ide-theme.md`
- `026-04-inspector-editing-presenter-slice.md`
- `028-expand-find-element-scope-and-go-linked-navigation.md`

## 2026-05-17 01:50 MSK — next task moved to work

SEE requested taking the next available task into work. Selected `001-open-project-loading.md` by priority/cost ordering from `machine/issue/backlog` (`priority - high`, `cost - M`), moved it to `machine/issue/work/`, and set `status - in_progress`.

## 2026-05-17 01:58 MSK — 001 validated and closed

SEE pointed to the local Java/Maven toolchain under `/home/openclaw/workspace/java`. Re-ran validation for `machine/issue/work/001-open-project-loading.md`:

- `mvn -q test` with local JDK/Maven — passed.
- `mvn -q -DskipTests package && git diff --check` with local JDK/Maven — passed.

Set `001` status to done.
