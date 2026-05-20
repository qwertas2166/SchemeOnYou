summary - Заменить самописные getters/setters на Lombok-аннотации

status - done
priority - low
cost - M

goal - Уменьшить boilerplate в Java-коде: заменить ручные `get*`/`set*` методы на Lombok `@Getter`/`@Setter` там, где это безопасно.

description - Нужно провести рефакторинг моделей/DTO/state-классов и убрать самописные getters/setters, заменив их Lombok-аннотациями. Замена должна быть осторожной: не трогать методы с бизнес-логикой, валидацией, сайд-эффектами, нестандартными именами или публичным API, где ручная реализация важна.

acceptance criteria -
- Простые ручные getters заменены на Lombok `@Getter`.
- Простые ручные setters заменены на Lombok `@Setter`.
- Методы с логикой, валидацией, dirty-state, undo/redo side effects или нестандартным contract не заменяются автоматически.
- Публичный API и serialization/storage behavior не меняются.
- Lombok уже подключен/используется через существующую project setup; новые тяжелые dependencies не добавляются.
- Код после рефакторинга компилируется.
- Тесты/release gate проходят или blocker явно зафиксирован.

notes -
- Требование SEE от 2026-05-17: "Рефакторинг. Используй lombok @Getter @Getter вместо самописных getter и setter".
- Интерпретация: для getter использовать `@Getter`, для setter использовать `@Setter`; если действительно нужен только `@Getter`, уточнить перед реализацией.
- Оценка дана в формате S/M/L/XL: `M`.

progress - 2026-05-17 13:00 MSK scheduler-2: взята в работу как безопасная M-задача; начат core model slice: простые getters заменены на Lombok `@Getter`, простые primitive setters — на field-level `@Setter`; setters с null-check/touch/clamp/business behavior оставлены ручными.
progress - 2026-05-17 13:00 MSK scheduler-2: verification passed with local toolchain: `mvn -q test` and `mvn -q -DskipTests package` both exit 0. Remaining scope: review non-model DTO/state classes and decide whether null-check setters should remain manual.
progress - 2026-05-17 13:30 MSK scheduler-2: продолжен безопасный core model slice: null-check setters для `DbColumn.name/type`, `DbTable.name`, `Diagram.name`, `SequenceParticipant.name`, `SequenceMessage.label` заменены на Lombok field-level `@Setter` + `@NonNull`; ручными оставлены только setters с поведением (`FkPreview#setTarget`, `CanvasState#setZoom`, `SchemeProject#setName`).
progress - 2026-05-17 13:30 MSK scheduler-2: verification passed with local toolchain: `mvn -q test` and `mvn -q -DskipTests package` both exit 0. Remaining scope: review non-model DTO/state classes if new manual accessors appear; current `core/src/main/java` and `client/src/main/java` have no manual getters and only behavior-bearing manual setters.
progress - 2026-05-17 14:00 MSK scheduler-2: final review completed. `grep` over `core/src/main/java` and `client/src/main/java` found no manual getters; only behavior-bearing manual setters remain: `FkPreview#setTarget`, `CanvasState#setZoom`, `SchemeProject#setName`. Verification passed with local toolchain: `mvn -q test` and `mvn -q -DskipTests package` both exit 0. Task marked done.
