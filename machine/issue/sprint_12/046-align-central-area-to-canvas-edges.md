summary - Выравнивать центральную область по краям canvas

status - done
priority - high
cost - S

goal - Сделать layout центральной области визуально точным: центральная функциональная область должна выравниваться по краям canvas.

description - Центральная область интерфейса должна иметь границы/контейнер, согласованные с фактическими краями canvas. Не должно быть визуального смещения, лишних внутренних зазоров или несовпадения между рамкой/fieldset центральной области и областью рисования canvas, кроме явно заданного padding по design system.

acceptance criteria -
- Левая/правая/верхняя/нижняя границы центральной функциональной области выровнены с соответствующими краями canvas или согласованным canvas container.
- При изменении размера окна центральная область сохраняет выравнивание относительно canvas.
- Выравнивание не конфликтует с left menu и inspector, закрепленными к краям окна.
- Padding центральной области применяется предсказуемо и не создает случайного offset между областью и canvas.
- Fieldset/outline центральной области, если используется, визуально соответствует canvas bounds.
- Keyboard focus/select outline центральной области не смещает canvas и не ломает layout.
- Добавлена visual smoke-check запись или layout-проверка для нескольких размеров окна.

notes -
- Требование SEE от 2026-05-17: "Центральная область должна выравниваться по краям canvas".
- Связано с layout задачами `036`, `041`, `044`.
- Оценка дана в формате S/M/L/XL: `S`.

progress - 2026-05-17 02:04 MSK: moved from backlog to sprint_12 during hourly backlog analysis; priority set to high (layout foundation for current central/canvas UI polish).

progress - 2026-05-18 16:30 MSK scheduler-2:
- backlog directory was empty, so selected the highest-priority suitable planned sprint issue per consistency checklist.
- bound JavaFX canvas width/height to the central canvas container, removed the accidental 16px inner canvas-stack padding, and redraw on resize.
- added a layout smoke test for 960x540 central-area/canvas bound alignment.

validation -
- 2026-05-18 16:30 MSK: `mvn -q -pl client -am -Dtest=ApplicationLayoutConstraintsTest -Dsurefire.failIfNoSpecifiedTests=false test` — passed.
- 2026-05-18 16:30 MSK: `mvn -q test` — passed.
