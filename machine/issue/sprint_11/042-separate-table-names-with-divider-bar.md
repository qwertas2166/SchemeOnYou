яёsummary - Отделить названия таблиц визуальной полосой

status - planned
priority - mid
cost - S

goal - Улучшить читаемость таблиц на диаграмме: название таблицы должно быть визуально отделено от списка колонок полосой/разделителем.

description - В карточке таблицы заголовок с названием должен явно отделяться от тела таблицы. Нужно использовать более подходящий UI-компонент или настроить текущий компонент так, чтобы между названием таблицы и колонками была стабильная визуальная полоса/разделитель.

acceptance criteria -
- В каждой таблице название визуально отделено от колонок горизонтальной полосой/разделителем.
- Разделитель консистентен с текущей dark theme: цвет, толщина, отступы и состояние выделения не конфликтуют с остальным UI.
- Решение работает для обычного состояния, hover/focus/selected state и validation/warning state таблицы.
- Разделитель масштабируется вместе с таблицей и не ломает layout при длинных названиях/колонках.
- Если текущий компонент плохо подходит, он заменен на более подходящий или обернут без ухудшения keyboard/focus behavior.
- SVG/export/rendering parity обновлены, если таблицы экспортируются/рисуются отдельным кодом.
- Добавлена visual smoke-check запись или тест/проверка для таблицы с несколькими колонками.

notes -
- Требование SEE от 2026-05-17: "Названия таблиц должны отделяться полосой. Используй другой компонент или настрой текущий".
- Оценка дана в формате S/M/L/XL: `S`.

dependencies/risks -
- Depends on current canvas table renderer and SVG exporter using consistent table geometry.
- Risk: changing header/body geometry can desync hit-testing, selection bounds, drag bounds, and SVG export; update all affected rendering paths together.

progress - 2026-05-17 01:06 MSK: moved from backlog to sprint_11 during hourly backlog analysis; priority set to {prio} ({reason}).
