# Анализ open source решений

## Краткий вывод

- Ниша SchemeOnYou: keyboard-first desktop sketch editor с визуальным canvas.
- Существующие GUI-инструменты сильны в canvas editing, но часто mouse-first.
- Text-first инструменты удобны для клавиатуры и Git, но не дают полноценного визуального UX.
- Для MVP нужно брать гибрид:
  - визуальный canvas;
  - command palette;
  - properties panel;
  - Git-friendly storage;
  - SVG export.

## Что учитывать в MVP

- Не копировать тяжелые modeling suites.
- Не строить строгий UML modeler.
- Не делать PlantUML/Mermaid основным UX.
- Делать canvas главным рабочим пространством.
- Делать keyboard workflow обязательным для всех MVP-функций.

## PlantUML

### Полезно для SchemeOnYou

- Text-first подход.
- Простые декларативные описания.
- Хорошая совместимость с Git.
- Сильная поддержка sequence diagrams.
- Java ecosystem.

### Не брать в MVP

- PlantUML export/import.
- PlantUML как основной формат хранения.
- Полностью text-only UX.

### Возможное применение после MVP

- Export sequence diagram to PlantUML.
- Import simple PlantUML sequence diagram.
- Compare визуальной модели с generated PlantUML.

## Mermaid

### Полезно для SchemeOnYou

- Простая диаграммная нотация.
- Популярность в Markdown/GitHub/GitLab.
- Идея lightweight diagram-as-code.

### Не брать в MVP

- Mermaid export/import.
- Mermaid renderer как обязательную зависимость.
- Web-first подход.

### Возможное применение после MVP

- Export to Mermaid.
- Documentation-friendly export.

## UMLet

### Полезно для SchemeOnYou

- Lightweight desktop editor.
- Sketch-first подход.
- Java-based desktop модель.
- Быстрое редактирование свойств через текст.

### Что взять в MVP

- Простоту UI.
- Text properties для выбранного элемента.
- Быстрое создание sketch diagrams.

### Что не брать

- Mouse-first создание элементов.
- Нестрогую модель без Git-friendly serialization.

## diagrams.net / draw.io

### Полезно для SchemeOnYou

- Зрелый canvas UX.
- Zoom/pan/selection patterns.
- Экспорт изображений.
- Большой опыт визуального diagram editing.

### Что взять в MVP

- Canvas как основной workspace.
- SVG export.
- Видимый selection/focus.
- Базовые операции canvas navigation.

### Что не брать в MVP

- Большую библиотеку фигур.
- Универсальный diagram editor scope.
- Mouse-first drag-and-drop как основной сценарий.

## Eclipse Papyrus

### Полезно для SchemeOnYou

- Разделение модели и представления.
- Model explorer concept.
- Строгие свойства элементов.

### Не брать в MVP

- Industrial-grade UML scope.
- SysML.
- Profiles.
- Heavy Eclipse-like UX.
- Полную UML 2.x модель.

## Modelio

### Полезно для SchemeOnYou

- Идея project/model consistency.
- Несколько диаграмм в одном проекте.
- Явные типы элементов и связей.

### Не брать в MVP

- BPMN/ArchiMate/SysML scope.
- Heavy modeling suite UX.
- Enterprise architecture features.

## MVP product decisions from analysis

- Product type: sketch editor.
- Primary UI: visual canvas.
- Primary control style: keyboard-first.
- Command entry: command palette with fuzzy search.
- Editing style: properties panel + inline editing.
- Storage: Git-friendly text format.
- Export: SVG only.
- Scope control: no PlantUML/Mermaid/DDL/IDE/CLI in MVP.
