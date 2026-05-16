# Sprint 8 plan / hourly backlog analysis

Актуализировано: 2026-05-16 16:05 Europe/Moscow.

## Найдено в `machine/issue/backlog`

| Issue | Status | Priority | Cost | Решение |
|---|---|---:|---:|---|
| `035-keyboard-tab-stays-inside-functional-area.md` | backlog | high | S | перенести в `sprint_8`; базовое правило keyboard/focus model |
| `037-focus-first-object-on-functional-area-switch.md` | backlog | high | S | перенести в `sprint_8`; зависит от той же focus-area model |
| `036-empty-background-and-padded-functional-containers.md` | backlog | mid | S | перенести в `sprint_8`; layout-основа для fieldset/focus areas |
| `038-functional-area-fieldsets-with-names.md` | backlog | mid | S | перенести в `sprint_8`; visual grouping поверх `036` |
| `026-extract-application-presenters-for-headless-ui-tests.md` | done | mid | L | перенести в `sprint_8`; done-задачи не должны оставаться в backlog |

Активных XL-задач не найдено.

## Состав нового `sprint_8`

| Порядок | Issue | Status | Priority | Cost | Комментарий |
|---:|---|---|---:|---:|---|
| 1 | `035-keyboard-tab-stays-inside-functional-area.md` | work | high | S | moved to `machine/issue/work/`; scoped Tab/Shift+Tab implemented |
| 2 | `037-focus-first-object-on-functional-area-switch.md` | planned | high | S | затем поведение входа в область через `0/1/2/3` |
| 3 | `036-empty-background-and-padded-functional-containers.md` | planned | mid | S | затем привести контейнеры/padding к модели областей |
| 4 | `038-functional-area-fieldsets-with-names.md` | planned | mid | S | последним добавить fieldset-like оформление и названия |
| done | `026-extract-application-presenters-for-headless-ui-tests.md` | done | mid | L | закрытый parent/umbrella перенесен из backlog в последний sprint по решению SEE 2026-05-16 16:53 |

## Что изменено

- Создан `machine/issue/sprint_8/`.
- Перенесены из `machine/issue/backlog/` в `machine/issue/sprint_8/`: `035`, `036`, `037`, `038`, а также done-parent `026`.
- В перенесенных задачах `status - backlog` заменен на `status - planned` и добавлена progress-запись.
- `sprint_7/cost.md` актуализирован: sprint_7 считается закрытым normalization-sprint; новые UI/focus задачи вынесены в sprint_8.
- Root mirror-файлы `issue/backlog/035-038` обновлены: canonical path теперь указывает на `machine/issue/sprint_8/...`.

## Перемещено / оценено / приоритизировано

- Перемещено: `035`, `037`, `036`, `038`.
- Оценки сохранены: все четыре задачи = `S`.
- Приоритеты сохранены: `035`, `037` = `high`; `036`, `038` = `mid`.
- Приоритизация sprint_8: `035` → `037` → `036` → `038`.

## L / вопросы

Done L-задача `026` перенесена из backlog в `sprint_8`; parent уже закрыт решением SEE от 2026-05-16 15:44. Новых уточняющих вопросов по ней нет.

## XL-декомпозиция

XL-задач не найдено; декомпозиция не выполнялась.

## Блокеры / неясности

- Структура backlog понятна.
- Done-задачи в `machine/issue/backlog/` больше не оставлены.

## Update 2026-05-16 16:53 MSK — cleanup done backlog tasks

По решению SEE перенесены все `status - done` задачи из backlog в последний sprint. Перенесена `026-extract-application-presenters-for-headless-ui-tests.md` из `machine/issue/backlog/` в `machine/issue/sprint_8/`.

## Update 2026-05-16 17:00 MSK — scheduler-2

- `035-keyboard-tab-stays-inside-functional-area.md` moved from `sprint_8/` planned to `machine/issue/work/` and status set to `work`.
- Runtime change: Tab/Shift+Tab now cycles through focusable nodes only inside the current functional area (`0` top menu, `1` left menu, `2` canvas, `3` inspector).

## Update 2026-05-16 17:05 MSK — hourly backlog analysis

## Найдено в `machine/issue/backlog`

| Issue | Status | Priority | Cost | Решение |
|---|---|---:|---:|---|
| `040-apply-monaspace-krypton-font-weights.md` | backlog | mid | S | перенести в `sprint_8`; UI/theme polish связан с `036`/`038`, безопасный S-slice |

Активных XL-задач не найдено.

## Что изменено

- `040-apply-monaspace-krypton-font-weights.md` перенесена из `machine/issue/backlog/` в `machine/issue/sprint_8/`.
- В `040` статус изменен `backlog` → `planned`, добавлена progress-запись.
- Root mirror `issue/backlog/040-...` обновлен: canonical path теперь `machine/issue/sprint_8/...`, status `mirrored`.

## Перемещено / оценено / приоритизировано

- Перемещено: `040`.
- Оценка сохранена: `S`.
- Приоритет сохранен: `mid`.
- Приоритизация sprint_8 после обновления: `035` → `037` → `036` → `038` → `040`.
- Обоснование: сначала закрыть keyboard/focus и layout/fieldset основу; затем применять Monaspace Krypton и веса через общую theme/CSS-модель.

## L / вопросы

Новых L-задач в `machine/issue/backlog` не найдено; вопросов по L нет. Historical done-parent `026` уже находится в `sprint_8` и не требует новых вопросов.

## XL-декомпозиция

XL-задач не найдено; декомпозиция не выполнялась.

## Блокеры / неясности

- Структура backlog понятна.
- Для `040` есть риск лицензии/источника шрифта: перед bundling нужен license/source check или documented fallback.

## Update 2026-05-16 17:30 MSK — scheduler-2

- `machine/issue/backlog/` пуст; взята следующая high/S planned-задача из `sprint_8`: `037-focus-first-object-on-functional-area-switch.md`.
- `037` перенесена в `machine/issue/work/`, статус `work`; root mirror обновлен на новый canonical path.
- Runtime change: переключение `0`/`1`/`2`/`3` теперь фокусирует первый доступный focusable-объект области; для canvas дополнительно выбирается первый table/participant или безопасно очищается selection в пустой области.
- Tests added: `FocusAreaNavigatorTest` покрывает несколько объектов, один объект/root fallback, пустую область, hidden/disabled/non-focusable skip.
- Gate: `mvn -q -pl client test` не запущен — в окружении нет `mvn`; `java`/`javac` тоже отсутствуют.

## Update 2026-05-16 18:00 MSK — scheduler-2

- `machine/issue/backlog/` пуст; взята следующая S planned-задача из `sprint_8`: `036-empty-background-and-padded-functional-containers.md`.
- `036` перенесена в `machine/issue/work/`, статус `work`; root mirror обновлен на новый canonical path.
- Runtime change: добавлен нижний mouse-transparent empty background layer; top/left/canvas/inspector/footer завернуты в dedicated functional-area containers; padding вынесен в общие CSS classes.
- Gate: `JAVA_HOME=/home/openclaw/workspace/java/openjdk-25.0.2 PATH=/home/openclaw/workspace/java/apache-maven-3.9.11/bin:/home/openclaw/workspace/java/openjdk-25.0.2/bin:$PATH mvn -q -pl client -am test` — pass.

## Update 2026-05-16 18:04 MSK — hourly backlog analysis

## Найдено в `machine/issue/backlog`

`machine/issue/backlog/` пуст: новых backlog-кандидатов для переноса нет.

## Актуализация текущего `sprint_8`

| Issue | Location | Status | Priority | Cost | Комментарий |
|---|---|---|---:|---:|---|
| `035-keyboard-tab-stays-inside-functional-area.md` | `work/` | work | high | S | Tab/Shift+Tab scope уже реализован scheduler-2; остается финальное закрытие/верификация статуса. |
| `037-focus-first-object-on-functional-area-switch.md` | `work/` | work | high | S | Focus-on-area-switch уже реализован scheduler-2; gate ранее был заблокирован отсутствием Maven/JDK в PATH, но локальный toolchain теперь известен. |
| `036-empty-background-and-padded-functional-containers.md` | `work/` | work | mid | S | Реализован scheduler-2 в 18:00; `mvn -q -pl client -am test` прошел с локальным JDK/Maven. |
| `038-functional-area-fieldsets-with-names.md` | `sprint_8/` | planned | mid | S | Следующая UI/layout polish-задача после контейнеров `036`. |
| `040-apply-monaspace-krypton-font-weights.md` | `sprint_8/` | planned | mid | S | Брать после `038`; перед bundling нужен license/source check или fallback. |
| `026-extract-application-presenters-for-headless-ui-tests.md` | `sprint_8/` | done | mid | L | Historical done parent; вопросов нет, в backlog не лежит. |

## Что изменено

- Обновлен `sprint_8/cost.md` текущей актуализацией.
- Файлы задач не переносились и metadata задач не менялась.
- Новый спринт не создан: backlog пуст.

## Перемещено / оценено / приоритизировано

- Перемещено: ничего.
- Новых оценок не добавлено: текущие задачи sprint_8 остаются S; historical parent `026` остается L/done.
- Приоритизация sprint_8 сейчас:
  1. `035` — high/S, work: закрыть/проверить Tab-scope.
  2. `037` — high/S, work: закрыть/проверить first-focus behavior.
  3. `036` — mid/S, work: закрыть после уже пройденного Maven gate.
  4. `038` — mid/S, planned: fieldset/names поверх новых containers.
  5. `040` — mid/S, planned: font weights/theme polish после `038`.

## L / вопросы

Новых L-задач нет. `026` — L, но уже done/umbrella и не требует уточнений.

## XL-декомпозиция

XL-задач не найдено; декомпозиция не выполнялась.

## Блокеры / неясности

- Структура backlog понятна.
- Для `040` остается риск лицензии/источника шрифта: перед bundling нужен license/source check или documented fallback.

## Update 2026-05-16 18:30 MSK — scheduler-2

- `machine/issue/backlog/` пуст; взята следующая S planned-задача из `sprint_8`: `038-functional-area-fieldsets-with-names.md`.
- `038` перенесена в `machine/issue/work/`, статус `done`; root mirror обновлен на новый canonical path.
- Runtime/UI change: функциональные области получили fieldset-like border и non-focusable/mouse-transparent legends: `0 Top menu`, `1 Left menu`, `2 Canvas`, `3 Inspector`, `Status`.
- Focus behavior сохранен: `functionalAreaFor(...)` и direct area shortcuts продолжают использовать прежние content roots (`topMenu`, `leftMenu`, `canvas`, `inspectorPanel`), legends не становятся Tab-stop.
- Gate: `JAVA_HOME=/home/openclaw/workspace/java/openjdk-25.0.2 PATH=/home/openclaw/workspace/java/apache-maven-3.9.11/bin:/home/openclaw/workspace/java/openjdk-25.0.2/bin:$PATH mvn -q -pl client -am test` — pass.
## Update 2026-05-16 18:33 MSK — scheduler-3 backlog refinement

- `machine/issue/backlog/` пуст; новых backlog-задач не создано.
- Статусы `035`, `036`, `037` актуализированы `work` → `done` после общей проверки: `mvn -q -pl client -am test`, `mvn -q -DskipTests package`, `git diff --check` — pass.
- `038` уже был `done`; sprint_8 UI/focus/layout пачка `035`/`036`/`037`/`038` теперь закрыта.
- Следующая planned задача sprint_8: `040-apply-monaspace-krypton-font-weights.md` — mid/S; перед bundling нужен license/source check или fallback.
- Остальные обнаруженные gaps остаются покрыты существующими planned/work задачами: `015`, `019`, `021`, `025`, `033`, `017`/`010`, `001`, `024`, `028`, `032`.


## Update 2026-05-16 19:03 MSK — hourly backlog analysis

- В `machine/issue/backlog/` найдена одна новая задача: `041-prune-or-wire-unused-ui-shell-stubs.md` (`low`/`S`).
- Создан новый `machine/issue/sprint_9/`; `041` перенесена туда со статусом `planned`.
- Текущий `sprint_8` не расширялся: в нем остается planned `040-apply-monaspace-krypton-font-weights.md`, а done-parent `026` остается historical.
- Приоритет: завершить `040` перед `041`; active high/M release gaps в `work/` выше low-priority cleanup.
- L: новых L в backlog нет; `026`/`007` done, `010` in_progress и уже уточнена. XL не найдено.

## Update 2026-05-16 20:00 MSK — scheduler-2

- Взята `040-apply-monaspace-krypton-font-weights.md` (mid/S): backlog пуст, это следующая planned S-задача sprint_8 перед low/S cleanup `041`.
- Перемещена из `sprint_8/` в `work/`, статус выставлен `done` после реализации и gate-проверок.
- Реализовано: Monaspace Krypton как preferred UI font family с fallback stack, body вес 200, headings/fieldset/section titles вес 400, canvas font helpers, source/license fallback note, синхронизация active Figma/mockup docs с Monaspace Krypton вместо Inter.
- Проверки: `mvn -q -pl client -am test` pass; `mvn -q -DskipTests package` pass; `git diff --check` pass.
- Блокеров нет; font binaries не бандлились из fonts-online, потому что безопаснее держать system-font fallback и документировать официальный upstream GitHub Next Monaspace / SIL OFL 1.1.

## Update 2026-05-16 20:07 MSK — hourly backlog analysis sync

- `040-apply-monaspace-krypton-font-weights.md` уже находится в `machine/issue/work/` со статусом `done` после scheduler-2 20:00.
- `sprint_8` не расширялся; актуальный следующий спринт для cleanup — `sprint_9` с `041`.
- Риск по font license/source снят для MVP через documented fallback без bundling font binaries.
