# SchemeOnYou — requirements

Desktop sketch editor для sequence-диаграмм и диаграмм таблиц БД.

## MVP summary

- Java + JavaFX.
- Linux, Windows, macOS.
- Визуальный canvas.
- Keyboard-first UX.
- Мышь разрешена.
- Несколько диаграмм в одном проекте.
- Sequence diagram.
- Database tables diagram.
- Git-friendly storage.
- SVG export.
- Запуск через Java JAR / script.

## Файлы

- `requirements.md` — итоговые требования MVP и non-MVP.
- `questions.md` — решённые вопросы и зафиксированные ответы.
- `keyboard-only-analysis.md` — keyboard-first UX-анализ.
- `open-source-analysis.md` — выводы из анализа open source решений.

## Статус

- Версия: черновик 0.4.
- Открытых вопросов: нет.
## Runtime keymap refinement

Актуальная MVP-навигация по крупным зонам UI согласована с runtime и machine/design docs: `0` top menu, `1` left/project menu, `2` canvas, `3` inspector. `Tab` / `Shift+Tab` остаются scoped traversal внутри текущей панели, поля, dialog, picker или sheet. Ранние упоминания Tab как перехода между focus areas считаются legacy/refinement context, а не текущей shortcut-моделью.
