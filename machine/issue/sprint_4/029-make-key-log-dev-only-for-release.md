summary - Сделать F12 key log dev-only или убрать из release UI

status - done
priority - high
cost - S

goal - Не выпускать MVP с отладочным key-logger surface как обычной пользовательской функцией и не засорять keyboard-first подсказки нерелизным tooling.

description - В runtime есть F12 key log window: он логирует нажатия клавиш и прямо рекламируется в footer/F1/ShortcutMap. Это полезно для разработки keyboard UX, но не является MVP-требованием и выглядит как release/privacy/polish риск для desktop sketch editor. В `sprint_1/012` зафиксировано, что F12 key log нужно либо оставить как dev-only tool, либо скрыть/документировать перед релизом; отдельной follow-up задачи на это сейчас нет.

acceptance criteria -
- F12 key log не показывается в обычных release-oriented footer/F1/help hints.
- ShortcutMap не рекламирует F12 как пользовательский shortcut в release contract.
- Если key log остается, он включается только явно dev/debug режимом, системным property/env flag или documented developer build path.
- В dev/debug режиме F12 поведение сохраняется и не конфликтует с основными shortcuts.
- Release README/checklist фиксирует, что key log выключен или dev-only.
- Добавлен минимальный regression/manual check: default run не показывает F12 в help; debug-enabled run показывает/открывает key log.

progress -
- 2026-05-15 scheduler-2: default/release shortcut map and footer no longer advertise F12 key log; F12 opens key log only when explicitly enabled with `-Dschemeonyou.debug.keyLog=true` or `SCHEMEONYOU_DEBUG_KEY_LOG=true`. Added headless ShortcutMap regression for release/default vs debug mode and documented release/dev toggle in `machine/README.md`.

dependencies/risks -
- Related to `sprint_1/012-done-ui-polish-accessibility-and-keymap-consistency.md` and `sprint_2/017-release-readiness-environment-and-ci-parity.md`.
- Risk: key log полезен для тестирования keyboard-first flows; не удалять без нужды, лучше спрятать за явным debug flag.
- Risk: если release/debug profile еще не оформлен, держать реализацию простой: property/env flag and hidden hint by default.
