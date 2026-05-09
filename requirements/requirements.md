# SchemeOnYou — требования

## 1. Продукт

- Desktop sketch editor для диаграмм.
- Основной рабочий режим: визуальный canvas.
- Все операции доступны с клавиатуры.
- Мышь разрешена, но не обязательна.
- MVP не является строгим UML/modeling suite.

## 2. MVP

### 2.1 Входит в MVP

- Java + JavaFX desktop application.
- Платформы:
  - Linux;
  - Windows;
  - macOS.
- Один проект содержит несколько диаграмм.
- Типы диаграмм:
  - sequence diagram;
  - диаграмма таблиц БД.
- Визуальный canvas.
- Keyboard-first управление.
- IDE-style shortcuts.
- Захардкоженная keymap.
- Command palette с fuzzy search.
- Простая захардкоженная раскладка элементов.
- Сохранение/открытие проекта.
- Git-friendly формат хранения.
- Undo/redo для изменяющих операций.
- Экспорт в SVG.
- Запуск через Java JAR / shell script.

### 2.2 Не входит в MVP

- Строгая UML-модель.
- Class/use case/activity/state/component/deployment diagrams.
- PlantUML export/import.
- Mermaid export/import.
- SQL DDL import/export.
- Sequence combined fragments:
  - alt;
  - opt;
  - loop;
  - par.
- Crow's foot ERD notation.
- Ручное позиционирование элементов с клавиатуры как обязательная возможность.
- Настройка auto-layout пользователем.
- Пользовательская настройка keymap.
- Vim-like режим.
- IDE integration.
- CLI render/export.
- PNG/PDF export.
- Installers/packages:
  - deb;
  - rpm;
  - msi/exe;
  - dmg.
- Тяжелые зависимости без отдельного согласования.

## 3. Диаграммы MVP

### 3.1 Sequence diagram

- Элементы:
  - participant;
  - lifeline;
  - message;
  - activation.
- Типы participant:
  - actor;
  - service;
  - database;
  - external system.
- Типы message:
  - sync;
  - async;
  - return;
  - self-call.
- Свойства message:
  - source;
  - target;
  - label;
  - order;
  - type.

### 3.2 Диаграмма таблиц БД

- Элементы:
  - table;
  - column;
  - primary key;
  - foreign key.
- Свойства table:
  - name;
  - columns.
- Свойства column:
  - name;
  - type;
  - nullable/not null;
  - primary key flag;
  - foreign key reference.
- Связи:
  - foreign key;
  - one-to-one;
  - one-to-many;
  - many-to-many через join table.
- ERD crow's foot notation не требуется в MVP.

## 4. UX/UI MVP

### 4.1 Canvas

- Отображает элементы и связи диаграммы.
- Показывает текущий selection/focus.
- Поддерживает:
  - zoom in/out;
  - pan/scroll с клавиатуры;
  - auto-fit;
  - простую автоматическую раскладку.

### 4.2 Keyboard-first управление

- Все основные операции доступны с клавиатуры:
  - создать проект;
  - открыть проект;
  - сохранить проект;
  - создать диаграмму;
  - выбрать диаграмму;
  - создать элемент;
  - выбрать элемент;
  - изменить элемент;
  - создать связь;
  - удалить элемент/связь;
  - выполнить undo/redo;
  - экспортировать SVG.
- Мышь может дублировать операции, но не заменяет keyboard workflow.

### 4.3 Command palette

- Открывается через shortcut.
- Поддерживает fuzzy search.
- Команды MVP:
  - New project;
  - Open project;
  - Save project;
  - New sequence diagram;
  - New database diagram;
  - Add participant;
  - Add message;
  - Add table;
  - Add column;
  - Add foreign key;
  - Rename selected;
  - Edit selected;
  - Delete selected;
  - Find element;
  - Export SVG;
  - Show shortcuts.

### 4.4 Навигация

- Переключение между зонами UI:
  - project/diagram list;
  - canvas;
  - properties panel;
  - command palette.
- Навигация по элементам:
  - next/previous element;
  - find by name;
  - go to linked element;
  - select source/target через fuzzy search.

### 4.5 Properties editing

- Выбранный элемент редактируется через properties panel или inline editor.
- Редактирование должно быть доступно с клавиатуры.
- Изменения проходят через undoable commands.

### 4.6 Shortcuts MVP

- `Ctrl+K` или `Ctrl+Shift+P` — command palette.
- `Ctrl+S` — save.
- `Ctrl+O` — open.
- `Ctrl+N` — new project/diagram через command palette или dialog.
- `Ctrl+F` — find.
- `Ctrl+Z` — undo.
- `Ctrl+Y` или `Ctrl+Shift+Z` — redo.
- `Tab` — next focus area/element.
- `Shift+Tab` — previous focus area/element.
- `Enter` — edit/confirm.
- `Esc` — cancel/back.
- `Delete` — delete selected через undo-safe action.

## 5. Хранение проекта

### 5.1 Формат

- Формат выбирается разработчиком.
- Предпочтительно: JSON или YAML.
- Требования:
  - plain text;
  - readable enough for debugging;
  - stable ordering;
  - deterministic serialization;
  - version field;
  - project metadata;
  - list of diagrams;
  - diagram model;
  - canvas state/layout.

### 5.2 Git-friendly workflow

- Файлы проекта должны нормально храниться в Git.
- Порядок сущностей должен быть стабильным.
- Сериализация должна быть детерминированной.
- Diff должен быть читаемым, насколько это практично.

## 6. Экспорт

### 6.1 MVP

- SVG export.

### 6.2 После MVP

- PNG export.
- PDF export.
- PlantUML export/import.
- Mermaid export/import.
- SQL DDL import/export.

## 7. Архитектура

### 7.1 Обязательные решения

- Java + JavaFX.
- Разделение:
  - project model;
  - diagram model;
  - canvas/layout state;
  - UI state.
- Все изменяющие действия проходят через command layer.
- Command layer обеспечивает:
  - keyboard invocation;
  - undo/redo;
  - тестируемость;
  - будущие macros/scripting.
- Layout engine выделен отдельным компонентом.
- В MVP layout может быть простой и захардкоженный.

### 7.2 Dependency policy

- Легкие зависимости допустимы.
- Тяжелые зависимости для layout/rendering — только после отдельного согласования.
- Большие dependency installs — не делать без подтверждения.

## 8. Нефункциональные требования

### 8.1 Производительность MVP

- Комфортная работа до:
  - 200 элементов на диаграмме;
  - 500 связей на диаграмме.

### 8.2 Доступность

- Видимый focus indicator.
- Предсказуемый tab order.
- Нет mouse-only действий для MVP-функций.
- Контрастность достаточна для чтения.

### 8.3 Безопасность изменений

- Undo для изменяющих операций.
- Удаление должно быть undo-safe.
- Массовое удаление требует подтверждения.
- Нет silent destructive actions.

## 9. Зафиксированные решения

- MVP: sketch editor.
- MVP-диаграммы:
  - sequence diagram;
  - диаграмма таблиц БД.
- Проект поддерживает несколько диаграмм.
- Основной приоритет: удобный визуальный canvas.
- Все операции доступны с клавиатуры.
- Мышь разрешена.
- Shortcuts: IDE-style.
- Keymap: hardcoded.
- Формат хранения: любой, предпочтительно JSON/YAML.
- Git-friendly workflow обязателен.
- SVG export входит в MVP.
- Платформы: Linux, Windows, macOS.
- Запуск MVP: java jar script.
