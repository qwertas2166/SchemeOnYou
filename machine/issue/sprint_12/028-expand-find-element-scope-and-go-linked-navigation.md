summary - Расширить Find element и linked navigation до всех MVP-элементов

status - done
priority - high
cost - M

goal - Закрыть keyboard-first gap: поиск и переходы должны работать не только по DB tables, а по всем именованным/связанным MVP-элементам диаграмм.

description - Требования фиксируют `Find element`, навигацию по элементам, `go to linked element` и выбор source/target через fuzzy search. Сейчас `SchemeOnYouApplication.findElement()` ищет только DB table name через blocking `TextInputDialog`, а `Space G S` вызывает тот же table-only flow. Колонки, FK edges, sequence participants/messages и linked navigation source/target остаются недоступны через универсальный keyboard path. Задача не про modal-vs-non-modal UI (это покрывает `sprint_2/015`), а про полноту search index, selection result и go-linked semantics.

acceptance criteria -
- Find element индексирует DB tables, DB columns, FK edges/relations, sequence participants и sequence messages, где применимо по name/label/id/context label.
- Результат поиска выбирает найденный элемент с корректной selection depth/state и обновляет canvas, inspector и context line.
- Для DB column результат выбирает table + column depth; для FK edge выбирает relation edge; для sequence message выбирает message state после реализации sequence selection slice.
- `Space G S` / `Space G T` semantics согласованы с `019`: переход к source/target работает для выбранного FK edge или message, либо команда не рекламируется как active.
- Search scope и labels отражены в command palette/F1/help hints; пользователь не видит table-only текст, если поиск шире.
- Добавлены headless/presenter tests для search index и result-to-selection mapping, либо минимальный manual checklist до выделения presenter.
- Не добавляются тяжелые dependencies; fuzzy matching может оставаться простым contains/ranked contains для MVP.

dependencies/risks -
- Depends on/aligns with `sprint_3/022-sequence-keyboard-creation-and-inspector-editing.md` for sequence message selection state.
- Related to `sprint_2/015-non-modal-keyboard-flows-for-frequent-actions.md`, but not duplicate: `015` changes UI surface, this task changes indexed scope and navigation semantics.
- Related to `sprint_3/019-wire-or-hide-declared-space-navigation-layout-commands.md` for declared-vs-runtime Space command consistency.
- Risk: if source/target pickers grow too large, keep first slice to search index + select result, and split richer fuzzy source/target picking into a separate S task.

progress -
- 2026-05-15 scheduler-2: moved from backlog to work. Chosen because priority is high and cost is M, while the only other backlog item is L and already decomposed.
- 2026-05-15 scheduler-2: implemented first safe slice: headless FindElementPresenter indexes DB tables, DB columns, FK relations, sequence participants and messages; Ctrl+F now uses broader labels and maps first result back to current selection state where UI supports it. Added presenter tests for index coverage and selection mapping. Verified with `mvn -q -pl client -am test` using local JDK/Maven.

progress - 2026-05-17 02:04 MSK: moved from backlog to sprint_12 during hourly backlog analysis; priority set to high (keyboard-first MVP gap; already partially progressed, finishable M).
progress - 2026-05-18 17:30 MSK scheduler-2: backlog/ пуст; взята как highest-priority подходящая planned high/M задача, уже имевшая реализованный search-index slice. Закрыл оставшийся help/shortcut drift: `Space G T` больше не рекламируется как active/wired команда, `Space G S`/shortcut help теперь явно называется Find element и указывает на общий `element.find`, чтобы не оставалось table-only/unwired go-table поведения. Verification: `mvn -q -pl client -am test`, `git diff --check` passed. Status set to done.
