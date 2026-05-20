summary - Добавить MVP scale/performance smoke для 200 элементов и 500 связей
status - done
priority - mid
cost - M

goal - Проверить нефункциональное требование MVP: комфортная работа до 200 элементов и 500 связей на диаграмме.

description - Requirements define MVP performance target of 200 elements and 500 relations per diagram, but current release/test tasks focus on compile, unit tests, serialization, undo/redo and package gates. There is no explicit synthetic project generator or smoke check for layout, validation, SVG export and JSON round-trip at MVP scale. Нужно добавить лёгкий reproducible smoke/perf gate без тяжелых зависимостей и без DISPLAY: генерировать DB/sequence diagrams near target size, прогонять layout/validation/storage/export, фиксировать reasonable time/memory thresholds или хотя бы baseline log.

acceptance criteria -
- Есть core-only smoke test/tool that builds at least one DB diagram with ~200 tables/elements and ~500 FKs or documented equivalent relation load.
- Smoke covers deterministic layout, validation, JSON save/load round-trip and SVG export without JavaFX DISPLAY.
- Для sequence diagram есть отдельный scale fixture или явно задокументировано, почему DB-scale covers MVP risk first.
- Thresholds are conservative and CI-safe, or metrics are reported as baseline until stable thresholds are agreed.
- Release/check documentation references this smoke as optional or required gate.
- Test data generation is deterministic and does not add heavy dependencies.

dependencies/risks -
- Related to `work/010-tests-and-release-gates.md`, но scope только nonfunctional scale smoke, не общий test strategy.
- Related to `sprint_2/017-release-readiness-environment-and-ci-parity.md` for where the command is documented.
- Risk: strict timing thresholds can be flaky across hosts; start with baseline/report mode if needed.

progress -
- 2026-05-19 11:30 MSK scheduler-2: backlog dir was empty; per `machine/issue/consistency-checklist.md` selected planned sprint issue. Added core-only deterministic MVP DB scale smoke (`MvpScaleSmokeTest`) for 200 tables / 500 FKs covering layout, validation, JSON round-trip determinism and SVG export without JavaFX DISPLAY. Updated release verification doc to reference the smoke as optional extended verification.
