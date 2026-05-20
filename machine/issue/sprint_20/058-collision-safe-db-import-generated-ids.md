summary - Сделать generated IDs DB import collision-safe
status - done
priority - high
cost - S

goal - Не допускать невалидный project JSON и broken selection/storage после импорта PostgreSQL схем с именами, которые схлопываются в одинаковые normalized IDs.

description - `DbImportDiagramReplacer` генерирует IDs через `stableId(prefix, name)`, нормализуя имя до lowercase и заменяя не `[a-z0-9]` на `-`. Разные легальные DB identifiers вроде `user-role`, `user_role`, `user role`, а также похожие FK names могут получить одинаковый `table-*`, `column-*` или `fk-*` ID. Open/load hardening уже отвергает duplicate IDs, но import generator сам может создать такую диаграмму. Нужно сделать генерацию deterministic и collision-safe в пределах импортируемой диаграммы, не ломая повторный import/no-duplicates поведение.

acceptance criteria -
- Импорт двух таблиц/колонок/FK, чьи names нормализуются в один base ID, создает уникальные deterministic IDs с понятным suffix/hash.
- Повторный import той же схемы воспроизводит те же IDs и не создает дублей.
- Уникальность проверяется отдельно для diagram/table/column/FK scopes согласно текущей storage validation модели.
- Existing readable IDs для простых имен по возможности сохраняются без suffix.
- Save -> load round-trip проходит для collision fixture.
- Добавлены headless tests для table ID collision, same-table/different-table column ID collision и FK ID collision.

dependencies/risks -
- Depends on completed `sprint_16/049-04-whole-diagram-replace-integration.md`.
- Related to `sprint_11/001-open-project-loading.md`, but not duplicate: `001` rejects malformed duplicate IDs on open; this task prevents DB import from generating them.
- Risk: changing ID format may affect persisted imported diagrams; keep deterministic and backward-compatible for non-colliding names.

progress - 2026-05-18 18:30 MSK scheduler-2: взято как high/S после того, как `backlog/` уже пуст, но задача была свежей минимальной planned в `sprint_20`. Реализован collision-safe deterministic allocator для generated table/column/FK IDs в DB import: неколлизирующие readable IDs сохраняются, для normalized collisions добавляется короткий SHA-256 suffix. Добавлен headless test на table, same-table/different-table column и FK collisions + повторяемость и save/load round-trip. Проверки: `mvn -q -pl core -Dtest=DbImportDiagramReplacerTest test` OK; `mvn -q test` OK.
