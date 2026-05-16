summary - Добавить zoom через mouse wheel

status - backlog
priority - mid
cost - S

goal - Сделать масштабирование canvas доступным не только через клавиши `+`/`-`, но и через колесо мыши.

description - Пользователь должен иметь возможность менять zoom canvas с помощью mouse wheel. Поведение должно быть согласовано с существующим keyboard zoom (`+`, `-`, `Ctrl+0`, `Ctrl+1`) и не конфликтовать с прокруткой контейнеров/панелей.

acceptance criteria -
- Mouse wheel изменяет zoom canvas в допустимых пределах min/max scale.
- Направление wheel-инпута интуитивно: wheel up — zoom in, wheel down — zoom out, если не переопределено платформой/UX.
- Zoom через wheel использует те же шаги или согласованную плавность, что и keyboard zoom.
- Zoom не ломает текущие `+`/`-`, `Ctrl+0` fit и `Ctrl+1` 100%.
- Wheel zoom применяется только в canvas/central area и не мешает scroll внутри left menu/inspector/fieldset-контейнеров.
- После zoom сохраняется корректное позиционирование viewport/canvas и visible focus/selection state.
- Добавлены тесты/проверки или documented smoke-check для wheel zoom in/out и min/max limits.

notes -
- Требование SEE от 2026-05-17: "zoom должен работать не только с помощью +-, но и с помощью mouse wheel".
- Связано с canvas viewport/zoom behavior.
- Оценка дана в формате S/M/L/XL: `S`.
