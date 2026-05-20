summary - Разместить Left menu и Inspector выше Canvas по z-level

status - done
priority - mid
cost - S

goal - Исправить визуальную и интерактивную иерархию слоев: Left menu и Inspector должны находиться выше Canvas по уровню отображения.

description - В layout/layering модели приложения боковые функциональные области Left menu и Inspector должны иметь более высокий z-level, чем центральная Canvas область. Это нужно, чтобы боковые панели визуально и интерактивно не перекрывались canvas-слоем, особенно при адаптивном resize, outline/fieldset, пустом background layer и возможных canvas interactions.

acceptance criteria -
- Left menu отображается выше Canvas по z-order/z-level.
- Inspector отображается выше Canvas по z-order/z-level.
- Canvas не перекрывает border/outline/fieldset/padding боковых областей.
- События мыши/keyboard focus для Left menu и Inspector не перехватываются Canvas при пересечениях или resize.
- Центральная Canvas область продолжает занимать доступное пространство между боковыми областями и не ломает adaptive layout.
- Поведение совместимо с закреплением Left menu/Inspector к краям окна и выравниванием центральной области по Canvas.
- Добавлена visual smoke-check запись или layout-проверка для z-level при обычном размере и после resize.

notes -
- Требование SEE от 2026-05-17: "Left menu и Inspector должны быть выше по уровню, чем Canvas".
- Связано с layout/layer задачами `041`, `044`, `046` и extraction `052`.
- Оценка дана в формате S/M/L/XL: `S`.

progress - 2026-05-17T12:30:00+03:00 scheduler-2: взята в работу как самая маленькая подходящая задача (S) среди backlog <= M; high-priority 049 имеет L и не подходит под лимит.
progress - 2026-05-17T12:30:00+03:00 scheduler-2: implemented z-layer contract via JavaFX viewOrder: side panels use lower viewOrder than canvas, app wiring applies it to Left/Canvas/Inspector, added headless layout smoke test. Verified `mvn -q test` and `mvn -q -DskipTests package` with local Java/Maven toolchain.

progress - 2026-05-17 13:05 MSK scheduler-1: moved from `work/` to `sprint_12/` as done artifact during hourly backlog normalization; `work/` kept for active tasks only.
