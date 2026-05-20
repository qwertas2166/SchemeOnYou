summary - Перевести частые keyboard-first действия с blocking dialogs на встроенные preview/overlay flows
status - done
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

result - Frequent editor actions now use non-blocking canvas overlays/panels for command palette, find element, rename selected, delete confirmation, and join-table preview/confirmation. Native file/open/save/export lifecycle dialogs remain out of scope.
check - JAVA_HOME=/home/openclaw/workspace/java/openjdk-25.0.2 PATH=/home/openclaw/workspace/java/apache-maven-3.9.11/bin:/home/openclaw/workspace/java/openjdk-25.0.2/bin:$PATH mvn -q -pl client -am -Dtest=CommandRouterTest -Dsurefire.failIfNoSpecifiedTests=false test; git diff --check -- client/src/main/java/see/schemeonyou/SchemeOnYouApplication.java machine/issue/sprint_2/015-non-modal-keyboard-flows-for-frequent-actions.md

progress -
- 2026-05-16 scheduler-2: взят первый безопасный slice — command palette overlay; заменён blocking `Dialog.showAndWait()` на встроенную non-blocking панель в canvas stack с Enter/Esc и фильтрацией через existing `CommandRouter`.
- 2026-05-19 21:00 MSK scheduler-2: backlog/ пуст; продолжена highest-priority подходящая активная high/M задача. Перевел `Find element` с blocking `TextInputDialog.showAndWait()` на встроенный non-blocking overlay в canvas stack: поиск обновляется по вводу, Enter выбирает первый/выбранный результат, Esc закрывает overlay и возвращает focus на canvas. Проверка: `mvn -q -pl client -am -Dtest=CommandRouterTest -Dsurefire.failIfNoSpecifiedTests=false test` passed.
- 2026-05-19 22:00 MSK scheduler-2: backlog/ пуст; продолжена highest-priority активная high/M задача. Перевел `Rename selected` с blocking `TextInputDialog.showAndWait()` на встроенный non-blocking canvas overlay: поле предзаполняется именем выбранной таблицы, Enter применяет undoable `RenameTableCommand`, Esc отменяет и возвращает focus на canvas. Verification: `mvn -q -pl client -am -Dtest=CommandRouterTest -Dsurefire.failIfNoSpecifiedTests=false test`, `git diff --check -- client/src/main/java/see/schemeonyou/SchemeOnYouApplication.java` passed. Статус оставлен `work`: delete/join-table confirm остаются modal slices.
- 2026-05-19 22:30 MSK scheduler-2: backlog/ пуст; продолжена highest-priority активная high/M задача. Перевел `Delete selected` с blocking `Alert.showAndWait()` на non-blocking canvas confirmation overlay: preview показывает context line, Enter подтверждает через existing undoable delete controller, Esc/Cancel отменяет и возвращает focus на canvas. Verification: `mvn -q -pl client -am -Dtest=CommandRouterTest -Dsurefire.failIfNoSpecifiedTests=false test`, `git diff --check -- client/src/main/java/see/schemeonyou/SchemeOnYouApplication.java` passed. Статус остается `work`: join-table confirmation еще modal slice.

- 2026-05-19 23:30 MSK scheduler-2: backlog/ пуст; завершена highest-priority активная high/M задача. Перевел join-table preview/confirmation с blocking `Alert.showAndWait()` на non-blocking canvas overlay: preview показывает participants/columns/action, Enter создает join table через existing undoable command, Esc/Cancel отменяет и возвращает focus на canvas. После этого command palette/find/rename/delete/join frequent editor flows закрыты, статус `done`. Verification: `mvn -q -pl client -am -Dtest=CommandRouterTest -Dsurefire.failIfNoSpecifiedTests=false test`, `git diff --check -- client/src/main/java/see/schemeonyou/SchemeOnYouApplication.java machine/issue/sprint_2/015-non-modal-keyboard-flows-for-frequent-actions.md` passed.
