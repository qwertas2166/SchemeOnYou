summary - Заменить самописные getters/setters на Lombok-аннотации

status - backlog
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
