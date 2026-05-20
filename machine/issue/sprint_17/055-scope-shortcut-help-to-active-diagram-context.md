summary - Scope F1/ShortcutMap help to active diagram context

status - done
priority - mid
cost - S

goal - Убрать рассинхрон между context-aware command availability и F1/ShortcutMap help: пользователь не должен видеть недоступные DB/sequence Space-команды как актуальные для текущего типа диаграммы.

description - Runtime уже фильтрует command palette через `CommandRouter.search(..., activeDiagramType())`, а Space hint через `SpaceCommandSheet.entries(activeDiagramType())`. Но `showShortcuts()` строит F1 overlay напрямую из полного `ShortcutMap.asMap()`, поэтому на sequence-диаграмме показывает DB-only команды (`Space A T`, `Space A C`, `Space A F`, `Space A J`, `Space P`, `Space U`, `Space G T`, `Space L S`), а на DB-диаграмме показывает sequence-only команды (`Space A P`, `Space A M`). В `machine/current/progress.md` этот gap ранее был записан как `032-context-aware-command-availability-and-diagram-type-guards.md`, но canonical issue-файл сейчас отсутствует в `backlog/`, `work/` и `sprint_*`, поэтому нужна новая backlog-задача без дублирования существующего файла.

acceptance criteria -
- F1/shortcuts overlay фильтрует Space-команды по `activeDiagramType()` тем же правилом, что и `SpaceCommandSheet` / command palette.
- Global shortcuts (`Ctrl+S`, `Ctrl+O`, `Ctrl+F`, undo/redo, focus `0/1/2/3`, zoom/pan, help) остаются видимыми для всех типов диаграмм.
- Sequence diagram help не показывает DB-only команды.
- Database diagram help не показывает sequence-only команды.
- Если активная диаграмма отсутствует или тип неизвестен, поведение безопасное: не падает и не показывает заведомо неверную подсказку.
- Добавлен headless тест для формирования shortcut/help модели или минимальный presenter/helper, чтобы не тестировать JavaFX overlay напрямую.
- `mvn -q test` проходит на локальном Java/Maven toolchain.

dependencies/risks -
- Зависит от текущих `ShortcutMap`, `SpaceCommandSheet`, `CommandRouter` semantics.
- Риск: прямое использование `ShortcutMap.asMap()` в UI снова приведет к drift; лучше вынести фильтрацию в отдельный helper/presenter.
- Связано с отсутствующим/утерянным canonical issue `032-context-aware-command-availability-and-diagram-type-guards.md` из progress history; перед переносом в спринт проверить, не восстановился ли старый файл.

notes -
- Обнаружено scheduler-3 project analysis/backlog refinement 2026-05-17 21:32 MSK.
- Взято scheduler-2 2026-05-17 22:00 MSK: единственная backlog-задача подходящего размера S.
- Реализован `ShortcutHelpModel`, F1 использует scoped entries по `activeDiagramType()`; добавлены headless тесты.
- `mvn -q test` проходит на локальном Java/Maven toolchain.
