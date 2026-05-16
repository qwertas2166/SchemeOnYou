summary - Перевести частые keyboard-first действия с blocking dialogs на встроенные preview/overlay flows
status - work
priority - high
cost - M

goal - Сделать command palette/find/rename/delete/join-table flows ближе к keyboard-first IDE UX и снизить риск блокирующих modal `showAndWait()` в основных сценариях.

description - В runtime уже есть canvas footer overlay и context line, но частые действия всё ещё используют JavaFX blocking dialogs: command palette, rename selected, find element, delete confirmation, join-table preview и часть error alerts. Это расходится с design-идеей transient command sheet/pickers/context confirmations и ухудшает flow с клавиатуры. Нужно заменить частые editor-flow диалоги на неблокирующие встроенные UI-состояния: palette/picker overlay, inline rename или inspector focus, context confirmation для delete/join preview. Нативные FileChooser для open/save/export можно оставить вне scope.

acceptance criteria -
- `Ctrl+K` / `Ctrl+Shift+P` command palette открывается как non-blocking overlay/panel; Enter запускает выбранную команду, Esc закрывает.
- `Ctrl+F` / `Space G S` ищет по таблицам и sequence elements через встроенный picker, без `TextInputDialog.showAndWait()`.
- `Space E` / Rename selected использует inspector/inline editor или non-blocking input, доступный с клавиатуры.
- Delete selected и join-table creation имеют явный preview/confirm/cancel без blocking `Alert.showAndWait()`.
- FileChooser flows явно остаются допустимым исключением; остальные частые editor actions не блокируют FX thread диалогом.
- Регрессия: keyboard shortcuts, Esc cancel, focus restore, context line priority покрыты unit/presenter tests или manual checklist.

dependencies/risks -
- Depends on current UI shell and context-line priority model.
- Related to done issues `002`, `006`, `009`, `012`, but не дублирует их: цель — снять оставшийся modal UX debt.
- Risk: может разрастись; если scope становится L, резать на palette/find и destructive-confirm подзадачи.

progress -
- 2026-05-16 scheduler-2: взят первый безопасный slice — command palette overlay; заменён blocking `Dialog.showAndWait()` на встроенную non-blocking панель в canvas stack с Enter/Esc и фильтрацией через existing `CommandRouter`.
