summary - Добавить zoom через mouse wheel

status - done
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

progress - 2026-05-17 02:04 MSK: moved from backlog to sprint_12 during hourly backlog analysis; priority set to mid (small UX convenience after viewport exists).

progress - 2026-05-18 20:33 MSK scheduler-3: observed partial runtime support in `SchemeOnYouApplication`: `Ctrl+wheel` zooms canvas and plain wheel pans canvas. Kept `planned` because current acceptance expects direct wheel up/down zoom or an explicit UX decision to require `Ctrl+wheel`; no duplicate issue created.

progress - 2026-05-19 17:30 MSK scheduler-2: backlog/ empty; took highest-priority suitable planned task `047` (mid/S) from sprint pool. Changed canvas wheel behavior to direct wheel zoom on canvas only (wheel up zoom in, wheel down zoom out) using existing zoom bounds/redraw path; updated footer/help text to mention wheel zoom. Documented smoke-check: hover/focus canvas, wheel up/down changes zoom %, left menu/inspector wheel remains handled by their controls because handler is attached only to canvas. Verification: targeted client Maven tests passed.
