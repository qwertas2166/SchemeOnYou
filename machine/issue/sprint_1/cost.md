# Sprint 1 cost / backlog актуализация

Актуализировано: 2026-05-15 13:06 Europe/Moscow.

Метод оценки:

- S - лёгкая. Готов сделать
- M - средняя. Готов сделать, придется потрудиться
- L - большая. Требуется уточнение
- XL - очень большая. Требуется декомпозиция

## Найдено в `machine/issue/backlog`

Найдены 4 задачи-кандидата:

| Issue | Priority | Cost | Решение |
|---|---:|---:|---|
| `014-sync-runtime-keymap-with-machine-design-docs.md` | high | S | перенесена в `sprint_2/` |
| `015-non-modal-keyboard-flows-for-frequent-actions.md` | high | M | перенесена в `sprint_2/` |
| `016-harden-json-and-svg-string-escaping.md` | mid | S | перенесена в `sprint_2/` |
| `017-release-readiness-environment-and-ci-parity.md` | mid | M | перенесена в `sprint_2/` |

После переноса `machine/issue/backlog` пуст.

## Активные задачи текущего спринта

| Issue | Location | Status | Priority | Cost | Обоснование |
|---|---|---|---|---:|---|
| `001-open-project-loading.md` | `work/` | in_progress | high | M | Критично для MVP save/load; основной core/UI-срез уже начат, объем ограничен round-trip, UI-open flow и hardening формата. |
| `007-sequence-diagram-mvp-ui.md` | `work/` | in_progress | high | L | Второй обязательный тип диаграмм: rendering, команды, свойства, layout и SVG parity; нужен scope-cut. |
| `013-canvas-drag-and-drop-positioning.md` | `work/` | work | mid | M | Полезный UX-срез с понятным scope; риски локализованы в layout policy, mouse transform и undo-on-release. |
| `011-packaging-and-cross-platform-launch.md` | `sprint_1/` | in_progress | mid | M | Portable launchers уже начаты; остается packaging/release README/smoke без глубокой доменной логики. |
| `010-tests-and-release-gates.md` | `work/` | in_progress | mid | L | Нужны regression suites и release gates; объем зависит от CI/ручного smoke и тестируемости UI без DISPLAY. |

## Завершенные задачи Sprint 1

Все `*-done-*` в `sprint_1/` считаются `done`, priority/cost без переоценки: M. Отдельных изменений по завершенным задачам за этот проход нет.

## Рекомендуемая приоритезация текущего спринта

1. `001-open-project-loading.md` — high/M: закрыть реальный open/load перед дальнейшим релизным hardening.
2. `007-sequence-diagram-mvp-ui.md` — high/L: зафиксировать минимальный MVP-scope и добить как обязательный тип диаграмм.
3. `013-canvas-drag-and-drop-positioning.md` — mid/M: брать после save/load или параллельно малым срезом, если не конфликтует с layout changes.
4. `011-packaging-and-cross-platform-launch.md` — mid/M: завершить portable запуск после стабилизации save/load.
5. `010-tests-and-release-gates.md` — mid/L: расширять gates после фикса ключевых MVP-flow, но держать минимальный test gate включенным постоянно.

## Новый sprint_2: приоритезация

1. `014-sync-runtime-keymap-with-machine-design-docs.md` — high/S: быстрый consistency-fix после `012`, снижает риск возврата F6-модели.
2. `015-non-modal-keyboard-flows-for-frequent-actions.md` — high/M: важный UX debt; держать scope в рамках частых editor-flow dialogs.
3. `016-harden-json-and-svg-string-escaping.md` — mid/S: маленький hardening writer/export, хорошо стыкуется с `001`.
4. `017-release-readiness-environment-and-ci-parity.md` — mid/M: нужен для устранения ложных verification blockers и поддержки release gate.

## Перенос в новый спринт

Выполнен: создан/использован `machine/issue/sprint_2/`, все 4 найденные backlog-задачи перенесены туда и переведены из `status - backlog` в `status - planned`.

## Вопросы по L-задачам

### `007-sequence-diagram-mvp-ui.md`

1. Минимальный MVP для sequence: достаточно participant + message + activation, или нужны create/delete/edit для всех элементов?
2. Нужны ли nested/overlapping activations в первом релизе?
3. SVG export должен быть pixel-parity с canvas или достаточно семантически корректной схемы?
4. Какие keyboard shortcuts считаем обязательными для participant/message?

### `010-tests-and-release-gates.md`

1. Какой минимальный gate обязателен перед релизом: `compile + unit tests + package` или еще UI smoke?
2. CI должен запускаться локально только через Maven или добавляем GitHub Actions как источник истины?
3. UI smoke в headless среде нужен автоматизированный или достаточно ручного checklist?
4. Какие regression areas самые критичные: save/load, undo/redo, FK/join-table, SVG export?

## XL-декомпозиция

XL-задач не найдено. Декомпозиция не потребовалась.

## Блокеры / неясности

- Структура backlog/sprint/work понятна для планирования.
- Неясно, является ли `work/013-canvas-drag-and-drop-positioning.md` частью текущего спринта или уже взятой work-задачей вне спринта; включил в активную приоритизацию, файл не перемещал.
