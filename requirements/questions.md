# Решённые вопросы

## MVP

- Тип продукта: быстрый sketch editor.
- Не строгий UML/modeling suite.
- В одном проекте нужно несколько диаграмм.
- MVP-диаграммы:
  - sequence diagram;
  - диаграмма таблиц БД.

## Keyboard-first UX

- Мышь разрешена.
- Все операции MVP доступны с клавиатуры.
- Shortcuts: IDE-style.
- Keymap: hardcoded.
- Пользовательская настройка keymap не нужна в MVP.
- Vim-like режим:
  - optional в будущем;
  - не входит в MVP.

## Canvas/layout

- В MVP достаточно простой захардкоженной раскладки.
- Ручное позиционирование с клавиатуры не входит в MVP.
- Настройка auto-layout не входит в MVP.
- Состояние canvas/layout должно сохраняться в проекте.

## Форматы и экспорт

- Формат хранения выбирает разработчик.
- Предпочтительно JSON/YAML.
- Git-friendly workflow обязателен:
  - plain text;
  - stable ordering;
  - deterministic serialization/export.
- SVG export входит в MVP.
- Вне MVP:
  - PlantUML export/import;
  - PNG export;
  - PDF export;
  - SQL DDL import/export.

## Стек и платформы

- Стек: Java + JavaFX.
- Платформы:
  - Linux;
  - Windows;
  - macOS.
- Тяжелые зависимости для layout/rendering — обсуждаемо, не добавлять без отдельного решения.

## Интеграции и packaging

- IDE integration не входит в MVP.
- CLI render/export не входит в MVP.
- Installers/packages не входят в MVP:
  - deb;
  - rpm;
  - msi/exe;
  - dmg.
- Для MVP достаточно запуска через Java JAR / shell script.

## Диаграмма таблиц БД

- В MVP достаточно таблиц с FK-связями.
- Crow's foot ERD notation не требуется в MVP.
- SQL DDL import/export не входит в MVP.

## Sequence diagram

- Combined fragments не входят в MVP:
  - alt;
  - opt;
  - loop;
  - par.

## Открытые вопросы

- Открытых вопросов на текущем этапе нет.
