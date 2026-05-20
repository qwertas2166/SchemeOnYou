summary - Интегрировать whole-diagram replace для DB import результата

status - done
priority - high
cost - M
parent - 049-import-tables-from-database-connection.md

goal - Применить результат PostgreSQL import к выбранной DB-диаграмме через полную замену содержимого без merge и без imported-subset metadata.

description - Четвертый M-срез для `049`: integration layer между import result и model/storage. Repeated import заменяет полностью всё содержимое выбранной DB-диаграммы. Metadata вроде `importSource/profile/schema/importBatchId` для MVP не требуется.

scope -
- Convert metadata import result into DB diagram model elements.
- Replace all current content of selected DB diagram atomically from import result.
- Preserve project-level structure outside selected DB diagram.
- Ensure result persists in existing project storage format without migration unless implementation proves migration unavoidable.
- Provide command/service seam usable by modal UI.
- If no DB diagram exists, automatically create a new DB diagram before applying import result.
- Keep operation undo-safe if current command architecture supports it; otherwise document why import replace is a committed destructive action guarded by modal warning.

out of scope -
- Imported-subset tracking/metadata.
- Merge semantics.
- Preview/diff.
- Profiles CRUD and modal UI internals.
- Non-MVP constraints beyond nullable/PK/FK.

acceptance criteria -
- Applying import result removes all previous tables/relations/content from the selected DB diagram and replaces it with imported tables/FKs.
- Other diagrams/project metadata are not modified except expected dirty/save state.
- Re-running import does not create duplicates.
- Empty project/no DB diagram path auto-creates a DB diagram and applies import result there.
- Result can be saved and re-opened through existing storage.
- Headless tests cover full replace, no duplicates on repeated replace, and preservation of other diagrams.

notes -
- SEE decision 2026-05-17 20:48 MSK: replace completely everything in the selected DB diagram; imported-subset metadata is not needed.
- SEE decision 2026-05-17 22:21 MSK: if no DB diagram exists, automatically create a new DB diagram.

progress - 2026-05-18 10:00 MSK scheduler-2: backlog пуст; взят следующий highest-priority eligible planned DB-import service slice `049-04` (high/M), после завершенных `049-01` и `049-02`.
progress - 2026-05-18 10:00 MSK scheduler-2: реализован headless `DbImportDiagramReplacer`: whole-diagram replace для selected DB diagram, auto-create DB diagram when missing, очистка старых tables/FK/sequence/canvas bounds, mapping import result в DbTable/DbColumn/ForeignKey, service result для UI seam. Добавлены headless tests: full replace с preservation других диаграмм, repeated replace без дублей, auto-create DB diagram, save/load round-trip.
validation -
- 2026-05-18 10:00 MSK: `JAVA_HOME=/home/openclaw/workspace/java/openjdk-25.0.2 PATH=/home/openclaw/workspace/java/apache-maven-3.9.11/bin:/home/openclaw/workspace/java/openjdk-25.0.2/bin:$PATH mvn -q -pl core test` — passed.
- 2026-05-18 10:00 MSK: `JAVA_HOME=/home/openclaw/workspace/java/openjdk-25.0.2 PATH=/home/openclaw/workspace/java/apache-maven-3.9.11/bin:/home/openclaw/workspace/java/openjdk-25.0.2/bin:$PATH mvn -q -pl core -am -DskipTests package` — passed.
- 2026-05-18 10:00 MSK: `git diff --check` — passed.
- 2026-05-18 10:00 MSK: full `mvn -q test` still blocked by existing client target permission issue: `client/target/classes/see/schemeonyou/ui/theme.css: Operation not permitted`.
