summary - Выделять контур выбранной функциональной области

status - planned
priority - high
cost - S

goal - Сделать активную функциональную область визуально очевидной: при select/focus функциональной области ее контур должен выделяться.

description - Когда пользователь выбирает функциональную область через `0`, `1`, `2`, `3` или другим допустимым способом, выбранная область должна получать заметный outline/border. Это помогает понять, где сейчас активен keyboard-focus scope и внутри какой группы будет работать `Tab`.

acceptance criteria -
- При выборе функциональной области ее контур визуально выделяется.
- Контур снимается с предыдущей функциональной области при переключении на новую.
- Выделение работает для top menu, left menu, canvas area, inspector и других существующих functional areas.
- Контур совместим с fieldset-like оформлением, padding/container structure и dark theme.
- Контур не ломает layout: не вызывает скачков размеров, обрезания содержимого или смещения элементов.
- Состояние outline синхронизировано с keyboard navigation: `0/1/2/3` выбирают область, `Tab` остается внутри выбранной области.
- Контур не конфликтует с внутренним selected/focused элементом области; область и элемент должны визуально различаться.
- Добавлена visual smoke-check запись или тест/проверка для переключения контура между областями.

notes -
- Требование SEE от 2026-05-17: "Когда происходит select функциональной области, требуется выделять контур".
- Связано с `035-keyboard-tab-stays-inside-functional-area.md`, `037-focus-first-object-on-functional-area-switch.md`, `038-functional-area-fieldsets-with-names.md`.
- Оценка дана в формате S/M/L/XL: `S`.

dependencies/risks -
- Depends on existing functional-area containers (`038`) and current direct area focus shortcuts `0/1/2/3`.
- Risk: outline styling may conflict with existing inner focus rings; keep area outline visually distinct and avoid layout-affecting border width changes.

progress - 2026-05-17 01:06 MSK: moved from backlog to sprint_11 during hourly backlog analysis; priority set to {prio} ({reason}).
