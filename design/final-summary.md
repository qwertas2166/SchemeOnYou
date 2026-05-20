# Итоговое резюме дизайна DB diagram UI/UX

Дата: 2026-05-10 14:30 MSK

## Выпущенные версии

- `db-diagram-ui-ux-v01.md` — базовый keyboard-first canvas: таблицы, колонки, FK, join table, командная палитра, простая автораскладка.
- `db-diagram-ui-ux-v02.md` — устранены конфликты `Tab` и bare-letter shortcuts; введён `Space` command mode; упрощён FK-flow.
- `db-diagram-ui-ux-v03.md` — добавлена глубина выбора table/column; relationship semantics стали выводиться из FK/PK/unique, а не храниться как отдельная метаинформация.
- `db-diagram-ui-ux-v04.md` — добавлен keyboard-friendly mark для двухобъектных сценариев, выбор FK edges и более строгие правила `Enter`.
- `db-diagram-ui-ux-v05.md` — generic mark заменён на более безопасный `Pin for relation`; добавлена видимость pinned state и превью для compound actions.
- `db-diagram-ui-ux-v06.md` — relation tray заменён на context action bar; добавлены FK Source/Target role chips, локальный `Swap` и безопасный повтор через feedback.
- `db-diagram-ui-ux-v07.md` — финальная рекомендация: строгий приоритет одной context line, progressive Space command sheet, удаление скрытого `R Repeat` в пользу явного `Keep target pinned after create`.

Latest version: `db-diagram-ui-ux-v07.md`.

`STOP_REVISIONS.md`: отсутствует на момент проверки.

## Последняя рекомендация

Рекомендовать к реализации `db-diagram-ui-ux-v07.md` как наиболее устойчивый MVP-дизайн. Он сохраняет keyboard-first подход и JavaFX/MVP-ограничения, но снижает риск скрытых состояний, конфликтов shortcuts и ошибок направления FK.

Ключевой итог v07: canvas остаётся главным рабочим пространством, все MVP-действия доступны с клавиатуры, а сложные действия проходят через явное превью с undo-safe поведением.

## Ключевые UI/UX и shortcut-решения

- Основной экран: Project tree, Canvas, Inspector/properties panel, одна context line + status hint, transient command palette/sheet/dialogs.
- Текущий runtime shortcut model: `0` top menu, `1` left menu, `2` canvas, `3` inspector переключают крупные области напрямую; `Tab` остаётся только для traversal внутри панели, диалога или picker. Историческое решение v07 с `F6` / `Shift+F6` заменено этой схемой.
- Canvas navigation использует стрелки, selection depth и `Enter` для входа из table depth в column depth.
- `Space` на canvas открывает progressive command sheet: видны prefix, допустимые следующие клавиши, `Backspace`, `Esc` и feedback на неверные keys.
- Основные active chords: `Space A T` add table, `Space A C` add column, `Space A F` add FK, `Space A J` add join table, `Space P` pin relation, `Space U` unpin, `Space E` edit selected, `Space G S` find element, `Space L D` layout diagram. Future/non-MVP: `Space G T` go target, `Space L S` layout selected cluster, FK edge picker on a non-conflicting chord.
- FK creation строится через source/target role chips, явное превью, локальный `X Swap` и понятную derived meaning строку.
- Повторное создание FK решается не временным shortcut, а checkbox `Keep target pinned after create`, который должен быть выключен по умолчанию.
- Relation semantics выводятся из структуры: FK + unique/PK/composite constraints; отдельная editable relationship metadata не вводится.
- Join table создаётся как один undoable compound action: таблица, две колонки, composite PK/unique marker и две FK edges.
- Визуализация MVP остаётся простой: table cards, markers для PK/not-null/unique/FK/pin, directional FK lines; без crow's foot notation.
- MVP допускает PostgreSQL metadata import from live connection; SQL DDL text import/export остаётся out of scope. Текущая граница DB import: single-column FK импортируются как FK edges, composite PK импортируются как table-level constraints, composite FK в MVP пропускаются с warning и не превращаются в misleading per-column edges.
- Auto-layout детерминированный и предпочтительнее обязательного ручного keyboard positioning.
- Все compound/destructive actions требуют превью или подтверждения и должны быть undo-safe.
- `Enter` выполняет только видимое безопасное default action; если смысл неоднозначен, открывается action menu.

## Оставшиеся риски

- Progressive command sheet добавляет немного UI-сложности по сравнению с bare overlay.
- `Keep target pinned after create` должен быть строго opt-in/default off, иначе появится неожиданное sticky state.
- Single context line требует дисциплинированной реализации приоритетов; иначе footer снова станет шумным или непредсказуемым.
- Нужно очень чётко отрисовать selected object, pinned object, FK direction и source/target roles, чтобы keyboard-first workflow не путал пользователя.
- Selection depth и relation pin — полезные, но обучаемые концепции; onboarding и `F1` shortcut overlay важны для принятия.
- FK inference и type mismatch warnings должны быть прозрачными: редактор sketch-oriented, поэтому часть предупреждений допускает продолжение, но не должна молча принимать сомнительные решения.
