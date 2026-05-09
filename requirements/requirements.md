# Требования к SchemeOnYou

## 1. Описание продукта

SchemeOnYou — desktop sketch editor для создания и редактирования sequence-диаграмм и диаграмм таблиц БД на визуальном canvas. Все операции должны быть доступны с клавиатуры; мышь разрешена, но не обязательна.

## 2. Цели

- Позволить разработчику быстро создавать sequence-диаграммы и диаграммы таблиц БД.
- Обеспечить удобный UX/UI с визуальным canvas как основным рабочим пространством.
- Сделать все операции доступными с клавиатуры, не запрещая использование мыши.
- Обеспечить сохранение, открытие и экспорт диаграмм.

## 3. MVP

### 3.1 Характер MVP

MVP — быстрый sketch editor, не строгий UML/modeling suite. Приоритет — скорость создания диаграмм, удобный визуальный canvas и keyboard-first workflow.

Проект должен поддерживать несколько диаграмм в одном проекте.

### 3.2 Поддерживаемые диаграммы

Приоритет MVP:

1. Sequence diagram.
2. Диаграмма таблиц БД / ER-like database schema diagram.

Не входит в MVP без отдельного решения:

- PlantUML export/import.
- SQL DDL import/export.
- Sequence combined fragments: alt, opt, loop, par.
- IDE integration.
- CLI render/export.
- Packaging installers: deb/rpm/msi/dmg.
- Manual keyboard positioning as a required feature.
- Class diagram.
- Use case diagram.
- Activity diagram.
- State diagram.
- Component diagram.
- Deployment diagram.

## 4. Функциональные требования

### FR-001. Desktop-приложение

Приложение должно запускаться как desktop-приложение.

**Приоритет:** Must.

### FR-002. Полное управление с клавиатуры

Все основные операции должны быть доступны с клавиатуры. Мышь разрешена, но не должна быть обязательной:

- создание диаграммы;
- создание элемента;
- выбор элемента;
- редактирование элемента;
- создание связи;
- удаление элемента/связи;
- перемещение фокуса;
- сохранение;
- экспорт;
- undo/redo.

**Приоритет:** Must.

### FR-003. Command palette

Приложение должно иметь command palette с fuzzy search команд.

Примеры команд:

- New diagram.
- Add participant.
- Add message.
- Add database table.
- Add table column.
- Add foreign key / relation.
- Rename selected.
- Edit selected.
- Auto-layout.
- Export.
- Show shortcuts.

**Приоритет:** Must.

### FR-004. Визуальный canvas

Приложение должно отображать диаграмму на canvas.

Canvas должен поддерживать:

- отображение sequence-элементов: participants, lifelines, messages, activations;
- отображение DB-элементов: tables, columns, primary keys, foreign keys;
- отображение связей;
- выделение текущего элемента;
- zoom in/out;
- pan/scroll с клавиатуры;
- auto-fit диаграммы.

**Приоритет:** Must.

### FR-005. Структурная навигация

Пользователь должен уметь перемещаться по диаграмме с клавиатуры:

- следующий/предыдущий элемент;
- переход к связанному элементу;
- поиск элемента по имени;
- переход по списку элементов;
- переход между canvas, деревом модели и панелью свойств.

**Приоритет:** Must.

### FR-006. Текстовое редактирование свойств элемента

Выбранный UML-элемент должен редактироваться через текстовую панель или inline editor.

Для sequence diagram минимум:

- имя participant;
- тип participant: actor/service/database/external system;
- сообщение: from, to, label, order, sync/async/return;
- activation markers.

Для диаграммы таблиц БД минимум:

- имя таблицы;
- список колонок;
- типы колонок;
- primary key;
- nullable/not null;
- foreign key связи;
- индексы — опционально.

**Приоритет:** Must.

### FR-007. Создание связей с клавиатуры

Пользователь должен создавать связь без мыши:

1. выбрать команду Add relation;
2. выбрать source через fuzzy search или текущий selection;
3. выбрать target через fuzzy search;
4. выбрать тип связи;
5. подтвердить создание.

Типы связей для MVP:

Sequence diagram:

- sync message;
- async message;
- return message;
- self-call.

Диаграмма таблиц БД:

- foreign key;
- one-to-one;
- one-to-many;
- many-to-many через join table.

**Приоритет:** Must.

### FR-008. Auto-layout

Приложение должно иметь простой захардкоженный layout для MVP. Расширенная настройка auto-layout и ручное позиционирование выносятся за рамки MVP.

Минимум:

- предсказуемая раскладка всей диаграммы;
- сохранение состояния canvas в файле проекта.

**Приоритет:** Must.

### FR-009. Undo/redo

Все изменяющие операции должны поддерживать undo/redo.

**Приоритет:** Must.

### FR-010. Сохранение проекта

Приложение должно сохранять диаграммы в любом внутреннем формате, выбранном разработчиком. Приоритет — удобный UX/UI и надежное сохранение состояния canvas.

Предпочтительный вариант по умолчанию: JSON или YAML как простой текстовый формат.

Файл должен содержать:

- модель диаграммы;
- визуальный layout;
- metadata версии формата.

**Приоритет:** Must.

### FR-011. Экспорт PlantUML

Экспорт PlantUML не входит в MVP.

**Приоритет:** Later.

### FR-012. Экспорт изображений

Приложение должно экспортировать диаграмму в SVG.

PNG и PDF не входят в MVP.

**Приоритет:** Must.

### FR-013. Импорт PlantUML

Импорт PlantUML не входит в MVP.

**Приоритет:** Later.

### FR-014. Горячие клавиши

Приложение должно иметь захардкоженную IDE-style keymap. Пользовательская настройка keymap не входит в MVP.

Базовые shortcuts:

- `Ctrl+K` или `Ctrl+Shift+P` — command palette;
- `Ctrl+S` — save;
- `Ctrl+Z` — undo;
- `Ctrl+Y` / `Ctrl+Shift+Z` — redo;
- `Ctrl+F` — find element;
- `Tab` — следующий focus area/element;
- `Shift+Tab` — предыдущий focus area/element;
- `Enter` — edit/confirm;
- `Esc` — cancel/back;
- `Delete` — delete selected через undo-safe action.

**Приоритет:** Must.

### FR-015. Help overlay

Приложение должно показывать список доступных команд и shortcuts.

**Приоритет:** Should.

## 5. Нефункциональные требования

### NFR-001. Стек

Стек: Java + JavaFX.

Swing и Compose Multiplatform не используются в MVP без отдельного решения.

**Приоритет:** Must.

### NFR-002. Производительность

MVP должен комфортно работать с диаграммами до:

- 200 элементов;
- 500 связей.

**Приоритет:** Should.

### NFR-003. Кроссплатформенность

Целевые платформы:

- Linux — Must;
- Windows — Must;
- macOS — Must.

### NFR-004. Доступность

UI должен быть полностью доступен с клавиатуры:

- видимый focus indicator;
- предсказуемый tab order;
- отсутствие mouse-only действий;
- readable contrast.

**Приоритет:** Must.

### NFR-005. Безопасность изменений

Деструктивные операции должны быть безопасными:

- undo для удаления;
- подтверждение для массового удаления;
- отсутствие silent destructive actions.

**Приоритет:** Must.

## 6. Архитектурные требования

### AR-001. Разделение модели и представления

Модель UML должна быть отделена от canvas/layout.

**Приоритет:** Must.

### AR-002. Command bus

Все действия пользователя должны проходить через единый command layer, чтобы обеспечить:

- keyboard invocation;
- menu invocation;
- undo/redo;
- макросы в будущем;
- тестируемость.

**Приоритет:** Must.

### AR-003. Layout engine abstraction

Auto-layout должен быть отдельным компонентом с возможностью заменить реализацию. В MVP допускается простая захардкоженная реализация.

**Приоритет:** Should.

### AR-004. Git-friendly workflow

Формат проекта должен быть совместим с Git-friendly workflow:

- plain text storage;
- stable ordering of entities;
- deterministic serialization/export;
- readable diffs where practical.

**Приоритет:** Must.

### AR-005. Dependency policy

Тяжелые зависимости для layout/rendering являются обсуждаемыми и не должны добавляться без отдельного решения.

**Приоритет:** Must.

## 7. Вопросы, влияющие на требования

См. `questions.md`.


## 8. Зафиксированные решения

- MVP: sequence diagram и диаграмма таблиц БД.
- Характер MVP: быстрый sketch editor, не строгий UML/modeling suite.
- Проект поддерживает несколько диаграмм.
- Основной приоритет: удобный UX/UI с визуальным canvas.
- Формат хранения: любой подходящий; предпочтительно JSON/YAML из-за простоты и отладки.
- Git-friendly workflow обязателен: plain text, stable ordering, deterministic serialization/export.
- Стек: Java + JavaFX.
- Мышь не запрещена, но все операции должны иметь keyboard workflow.
- Shortcuts: IDE-style, захардкоженные; настройка keymap не входит в MVP.
- Платформы: Linux, Windows, macOS.
- SVG export входит в MVP; PNG/PDF, PlantUML import/export, SQL DDL import/export, IDE integration, CLI render/export и installers — за рамками MVP.
- Для диаграмм БД достаточно таблиц с FK-связями; crow's foot не требуется в MVP.
- Sequence combined fragments alt/opt/loop/par — за рамками MVP.
- Для MVP достаточно скрипта запуска java jar.
