# Keyboard-first UX

## Вывод

- Приложение должно быть keyboard-first, но не keyboard-only.
- Мышь разрешена как дополнительный способ управления.
- Все MVP-операции должны выполняться с клавиатуры.
- UX должен быть ближе к IDE, чем к офисному diagram editor.

## Почему это важно

- Разработчик не теряет flow при переключении на мышь.
- Команды быстрее drag-and-drop для структурных диаграмм.
- Fuzzy search лучше палитры фигур для большого набора элементов.
- Текстовое редактирование свойств быстрее точечного canvas editing.
- Keyboard workflow улучшает accessibility.

## MVP UX-принципы

- Визуальный canvas остается основным рабочим пространством.
- Command palette — главный вход в действия.
- IDE-style shortcuts используются по умолчанию.
- Keymap захардкожена.
- Vim-like режим не входит в MVP.
- Ручное позиционирование с клавиатуры не входит в MVP.
- Auto-layout простой и предсказуемый.
- Все изменения undo-safe.

## MVP keyboard operations

- Создать/открыть/сохранить проект.
- Создать sequence diagram.
- Создать database diagram.
- Переключить активную диаграмму.
- Добавить participant.
- Добавить message.
- Добавить table.
- Добавить column.
- Добавить foreign key.
- Найти элемент по имени.
- Выбрать source/target через fuzzy search.
- Редактировать properties выбранного элемента.
- Удалить selected через undo-safe command.
- Выполнить undo/redo.
- Экспортировать SVG.

## MVP navigation

- `0` / `1` / `2` / `3` — актуальное переключение крупных зон UI: top menu, left/project menu, canvas, inspector.
- `Tab` / `Shift+Tab` — scoped traversal внутри текущей панели, поля, dialog, picker или sheet; не активный способ переключения крупных зон.
- `Ctrl+F` — поиск элемента.
- `Enter` — edit/confirm.
- `Esc` — cancel/back.
- `Delete` — delete selected.
- Command palette для всех редких действий.

## Риски

- Keyboard workflow может быть неочевиден без подсказок.
- Нужен help overlay со списком shortcuts.
- Auto-layout должен давать предсказуемый результат.
- Selection/focus должен быть визуально очевиден.
- Массовые destructive actions требуют подтверждения.

## Вне MVP

- Vim-like режим.
- Пользовательская настройка shortcuts.
- Макросы/scripting.
- Ручное позиционирование элементов с клавиатуры.
- Расширенная настройка layout.
