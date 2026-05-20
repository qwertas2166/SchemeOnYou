summary - Вынести инициализацию функциональных областей из SchemeOnYouApplication

status - planned
priority - mid
cost - M

goal - Уменьшить размер и связность `SchemeOnYouApplication`: найти инициализацию всех функциональных областей и вынести ее в отдельные классы.

description - `SchemeOnYouApplication` продолжает содержать создание и настройку top menu, left menu, центральной/canvas области, inspector, status/footer и связанных контейнеров/focus handlers. Нужно выделить отдельные классы/компоненты для инициализации функциональных областей, оставив `SchemeOnYouApplication` thin wiring layer для сборки приложения и координации общих dependencies.

acceptance criteria -
- В `SchemeOnYouApplication` найдены все места инициализации функциональных областей: top menu, left menu, canvas/central area, inspector, status/footer и related functional containers.
- Для каждой крупной функциональной области создан отдельный класс/component или небольшая группа классов с понятной ответственностью.
- `SchemeOnYouApplication` больше не содержит детальную сборку UI этих областей; он только создает dependencies, вызывает area builders/controllers и связывает их между собой.
- Поведение keyboard navigation/focus model не регрессирует: `0/1/2/3`, scoped `Tab`, outline выбранной области и status Focus работают как раньше.
- Стили/theme classes, padding/fieldset/container structure и adaptive layout сохраняются.
- Inspector view/edit modes и canvas interactions не ломаются из-за переноса инициализации.
- Новые классы не тянут бизнес-логику туда, где должен оставаться presenter/controller слой; это именно layout/area initialization extraction.
- Добавлены или обновлены tests/smoke checks для сборки основных областей, если это возможно без JavaFX DISPLAY; минимум проходит Maven gate.
- Storage format и тяжелые dependencies не меняются.

notes -
- Требование SEE от 2026-05-17: "Рефакторинг. В классе SchemeOnYouApplication найти инициализацию всех функциональных областей и вынести в отдельные классы".
- Связано с ongoing refactor вокруг `026` и UI/focus/layout задачами.
- Оценка дана в формате S/M/L/XL: `M`.

progress - 2026-05-17 13:05 MSK scheduler-1: moved from backlog to sprint_13; priority set to mid (рефакторинг UI initialization после presenter/extraction работ).
