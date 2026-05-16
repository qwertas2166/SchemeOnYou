# UI theme decisions v01

Дата: 2026-05-15

## Направление

SchemeOnYou визуально должен быть самостоятельным modern canvas tool, но по плотности и поведению оставаться IDE-инструментом для разработчиков.

## Зафиксированные решения

1. **Стиль**: самостоятельный modern canvas tool, не прямой клон IntelliJ IDEA.
2. **Theme**: только dark theme для MVP. Light theme не требуется.
3. **Canvas**: темный, но не глубокий; спокойный темно-серый, без сильного контраста как у pure black UI.
4. **Приоритет восприятия**: IDE-инструмент важнее презентационной красоты схем.
5. **Семантические цвета**: фиксированные, без пользовательской настройки в MVP.
6. **Color-blind mode**: не поддерживается в MVP.
7. **Коммуникация состояния**: максимум текстовых labels; icons/badges допускаются как усиление, но не как единственный носитель смысла.
8. **Context line**: остается снизу, по модели IntelliJ status/context bar.
9. **Inspector**: плотный IDE-like, без больших карточных отступов.
10. **Drag-and-drop**: свободное перемещение без snap/grid guides в первой версии.

## Рекомендуемая базовая палитра

Цель: modern canvas feel + IDE-density + спокойный dark grey canvas.

- App background: `#1F232A`
- Panels / tool windows: `#252A32`
- Panel elevated: `#2B313B`
- Canvas background: `#303640`
- Canvas grid subtle: `#3A414D`
- Table/card background: `#252B34`
- Table/card header: `#303847`
- Border default: `#48515F`
- Text primary: `#E6EAF0`
- Text secondary: `#A8B0BD`
- Text muted: `#7F8896`
- Selection / focus: `#4C8DFF`
- FK / relation: `#62AEEF`
- PK: `#D7A84A`
- Unique: `#9BCA64`
- Pin: `#E6B450`
- Warning: `#D19A3E`
- Error: `#E06C75`
- Success: `#70C17B`

## UI rules

- Не использовать цвет как единственный сигнал: рядом с цветом должны быть текстовые labels (`PK`, `FK`, `PIN`, `WARN`, `ERROR`).
- Context line снизу показывает только highest-priority state, как в v07.
- Inspector должен быть compact: небольшие vertical gaps, табличные/формовые строки, плотная типографика.
- Canvas должен оставаться спокойным: grid едва заметный, элементы читаемые, focus/selection явно видны.
- Drag-and-drop не должен становиться обязательным: keyboard-first сценарии остаются полными.
