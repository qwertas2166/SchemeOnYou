summary - Импортировать таблицы при подключении к БД

status - done
priority - high
cost - L

goal - При подключении к базе данных автоматически получать структуру таблиц и импортировать ее в диаграмму.

description - После настройки и успешного подключения к PostgreSQL приложение должно уметь прочитать metadata/schema базы и заменить текущую импортированную структуру таблиц в модели диаграммы. MVP: импорт таблиц, колонок, типов, nullable/PK признаков и foreign keys из выбранной schema/catalog, если драйвер PostgreSQL предоставляет эту информацию через metadata.

acceptance criteria -
- Пользователь может инициировать подключение к PostgreSQL и импорт структуры таблиц.
- После успешного подключения пользователь выбирает schema/catalog, и приложение получает список таблиц выбранной schema/catalog.
- Для каждой таблицы импортируются колонки, типы, nullable и primary key markers.
- Foreign keys импортируются как связи между таблицами, если доступны через DB metadata.
- Пользователь получает понятную ошибку при неуспешном подключении, недоступной схеме или отсутствии прав.
- Повторный импорт работает в режиме `replace`: полностью всё содержимое выбранной DB-диаграммы заменяется результатом импорта без merge и без сохранения прежних пользовательских элементов этой диаграммы.
- Импортированные данные сохраняются в текущем storage format или через явно описанную миграцию, если потребуется.
- Flow покрыт тестами на metadata mapping; интеграционные тесты могут использовать embedded/test DB или mock metadata.
- UI не зависает на время подключения/импорта; долгие операции показывают статус/progress.
- UI импорта обязателен для MVP: вертикальный flow от модального окна подключения/профилей/schema/catalog до замены данных в модели.
- Saved connection profiles поддерживают полный CRUD.
- Перед replace-import показывается модальное предупреждение, что всё текущее содержимое выбранной DB-диаграммы будет удалено и заменено результатом импорта.
- Preview/diff перед импортом не нужен для MVP.
- Version/migration для локального profiles file не требуется в MVP.

notes -
- Требование SEE от 2026-05-17: "Сделать импорт таблиц при подключении к БД".
- Оценка дана в формате S/M/L/XL: `L`, потому что требуется DB connection flow, metadata mapping, FK import, error handling и merge/replace semantics.
clarification - Решения SEE от 2026-05-17 02:05 MSK:
- Первая БД/драйвер: PostgreSQL.
- Connection settings/secrets хранить локально в файле.
- Repeated import: `replace`, не merge.
- Выбор schema/catalog нужен.
- Scope: MVP release.

clarification - Решения SEE от 2026-05-17 13:49 MSK:
- Пароль в локальном файле допустим для MVP.
- Нужны multiple saved profiles, не только один last-used settings file.

clarification - Решения SEE от 2026-05-17 14:43 MSK:
- UI для импорта нужен в MVP.
- Preview/diff перед импортом не нужен.
- Нужно модальное окно с предупреждением импорта перед replace.

clarification - Решения SEE от 2026-05-17 15:26 MSK:
- Делать первый engineering slice вертикально end-to-end.
- Saved profiles: полный CRUD.
- Version/migration для локального profiles file в MVP не требуется.

clarification - Решение SEE от 2026-05-17 20:48 MSK:
- Repeated DB import заменяет полностью всё содержимое выбранной DB-диаграммы.
- Imported-subset tracking не нужен; source/import metadata вроде `importSource/profile/schema/importBatchId` для MVP не требуется.

clarification - Решения SEE от 2026-05-17 22:21 MSK:
- Local profiles file хранить в папке рядом с launcher; приложение должно быть portable.
- Добавить явную Maven dependency на PostgreSQL JDBC driver.
- Progress: indeterminate statuses `Connecting` / `Reading metadata` / `Importing` / `Done` плюс counter по tables.
- Если DB-диаграммы нет, автоматически создать новую DB-диаграмму.
- В MVP игнорировать всё кроме tables/columns/types/nullable/PK/FK.

analysis - 2026-05-17 13:02 MSK scheduler-3:
- L-задача остается актуальной и не дублируется новой задачей: core/model/UI поддержки PostgreSQL import в коде не найдено.
- Уточнен риск: локальный файл connection settings/secrets не должен попадать в project JSON/export/Git-friendly storage; нужны redaction в UI/errors/logs и понятная политика хранения пароля.

mvp connection profile storage -
- Локальный файл профилей хранится вне project JSON/export/Git-friendly storage, в папке рядом с launcher для portable app behavior.
- MVP допускает plain text пароль в локальном файле профилей.
- Поддерживается несколько saved connection profiles.
- Для saved profiles нужен полный CRUD.
- Каждый profile должен минимум хранить display name, host, port, database, user, password, optional default schema/catalog и driver kind (`postgresql`).
- UI/errors/logs не должны выводить пароль; показывать redacted value при необходимости.
- Профили используются только как локальные настройки подключения и не являются частью диаграммы/модели проекта.
- Version/migration для локального profiles file в MVP не требуется.

open questions -
- Нет открытых вопросов по slice/profile CRUD/profiles version после решения SEE от 2026-05-17 15:26 MSK.

analysis - 2026-05-17 14:03 MSK scheduler-1:
- Оценка подтверждена: cost L, priority high. Это MVP-релизная capability, но для sprint_13 не переносится: текущий sprint уже загружен M-задачами; безопаснее сначала закрыть UI/refactor seams.
- Открытых product-решений по profiles/secrets/repeated import после уточнений 13:49 не осталось; остается engineering-split вопрос по первому вертикальному slice.
- Подходящий кандидат на следующий sprint после нарезки: PostgreSQL metadata mapping + local profiles + modal import UI + replace import без preview/diff и без расширенного UI-polish.

analysis - 2026-05-17 15:04 MSK scheduler-1:
- Оценка подтверждена: L, priority high. Product scope для MVP в целом ясен.
- Первый engineering slice выбран: вертикальный end-to-end UI → profiles CRUD → PostgreSQL metadata import → replace into model.
- Version/migration для profiles file не требуется в MVP; preview/diff не требуется.

analysis - 2026-05-17 16:00 MSK scheduler-3:
- L-задача остается актуальной и единственной задачей в `machine/issue/backlog`.
- Кодовой поддержки PostgreSQL/JDBC import/profile CRUD/schema selection/replace import не найдено; это не дублируется другими issue.
- Product slice уже выбран: vertical end-to-end от UI до модели, saved profiles full CRUD, без profiles version/migration и без preview/diff.
- Оставшийся блокирующий вопрос перед реализацией: replace boundary — заменять только DB-imported элементы в активной DB-диаграмме или всё содержимое выбранной DB-диаграммы?
analysis - 2026-05-17 16:03 MSK scheduler-1:
- Оценка/приоритет подтверждены: high / L. Это единственная задача в backlog и главный MVP-capability candidate.
- В текущий новый sprint не перенесена: перед реализацией нужно закрыть replace boundary, иначе есть риск потерять/перезаписать пользовательские элементы.
- Первый engineering slice остается выбранным: vertical end-to-end UI → profiles CRUD → PostgreSQL metadata import → replace into model, без preview/diff и без profiles version/migration.

analysis - 2026-05-17 16:30 MSK scheduler-2:
- По правилу scheduler-2 можно брать только M/S/XS или минимальную задачу не выше среднего размера; в backlog сейчас только `049-import-tables-from-database-connection.md` с cost L.
- Задача high priority, но не взята в работу из-за превышения лимита размера и существующего блокера replace boundary.
- Безопасное следующее действие: дождаться уточнения replace boundary или нарезать L-задачу на отдельные M/S/XS engineering slices.

analysis - 2026-05-17 17:00 MSK scheduler-3:
- L/high подтверждены: в `core/src`/`client/src` нет JDBC/PostgreSQL metadata import, connection profiles, schema/catalog selection или replace-import service/UI.
- Задача не дублируется: это единственный canonical backlog item; связанные release/UI/refactor gaps уже покрыты sprint-задачами.
- Перед реализацией остается product-safety вопрос по границе replace-import: заменять только ранее импортированные DB-элементы активной DB-диаграммы или полностью все содержимое выбранной DB-диаграммы.

analysis - 2026-05-17 17:03 MSK scheduler-1:
- Оценка/приоритет подтверждены: high / L. Scope не изменился: PostgreSQL connection UI, profiles CRUD, schema/catalog selection, metadata mapping, FK import, replace semantics, progress/error handling.
- В `sprint_14` не переносится: это L-задача с открытым replace-boundary вопросом, а текущий sprint содержит только безопасный S cleanup `010`.
- Следующее действие после ответа SEE: либо перенести `049` целиком в следующий sprint как vertical end-to-end L, либо нарезать на M-срезы, начиная с profiles + metadata mapping + replace-service seam.

analysis - 2026-05-17 17:32 MSK scheduler-3:
- Повторно подтверждено: это единственная canonical backlog-задача и главный незакрытый MVP gap.
- В коде по-прежнему нет PostgreSQL/JDBC metadata import, local connection profile CRUD, schema/catalog picker и replace-import service/UI.
- Дубликаты не создавались: UI/refactor/release/Space/modal-flow риски уже покрыты sprint-задачами, а DB import scope принадлежит `049`.
- Блокер перед реализацией сохраняется: нужна граница replace-import, чтобы не заложить опасное удаление пользовательских DB-элементов.

analysis - 2026-05-17 18:00 MSK scheduler-3:
- L/high подтверждены: в коде нет JDBC/PostgreSQL metadata import, local connection profiles CRUD, schema/catalog picker, progress/error flow или replace-import service/UI.
- Создана отдельная малая backlog-задача `054-define-db-import-replace-boundary.md`, чтобы закрыть product-safety blocker перед реализацией `049` без дублирования implementation scope.
- `049` остается canonical L implementation task после решения replace boundary.

analysis - 2026-05-17 18:03 MSK scheduler-1:
- Оценка/приоритет подтверждены: high / L. Scope остается vertical end-to-end PostgreSQL import с profiles CRUD, schema/catalog selection, metadata mapping, FK import, replace semantics, progress/error flow.
- Не переносится в sprint_15 напрямую: перед реализацией нужен маленький product-safety slice `054` по replace boundary.
- После закрытия `054` задача `049` может быть взята как canonical L implementation task или дополнительно нарезана на M-срезы.

analysis - 2026-05-17 18:30 MSK scheduler-2:
- По правилу scheduler-2 подходящих backlog-задач размера M/S/XS нет: в `machine/issue/backlog` осталась только `049` с cost L.
- `049` не взята в работу: превышает лимит размера scheduler-2 и зависит от product-safety решения `054` по replace boundary.
- Безопасное следующее действие: закрыть/уточнить `054` или нарезать `049` на отдельные M/S/XS engineering slices после решения boundary.

analysis - 2026-05-17 18:31 MSK scheduler-3:
- L/high подтверждены: в коде нет JDBC/PostgreSQL metadata import, local connection profile CRUD, schema/catalog picker, replace-import service/UI или progress/error flow для DB import.
- Дубликаты не создавались: `054` уже покрывает product-safety решение по replace boundary, а `049` остается canonical implementation task.
- Связанные текущие gaps по non-modal flows, sequence properties/order, composite constraints, focus/layout/status, drag/drop, presenter extraction и release gate уже покрыты существующими sprint-задачами.
- Блокер перед реализацией сохраняется: нужно закрыть `sprint_15/054` — граница repeated replace-import и необходимость import metadata в project JSON.

analysis - 2026-05-17 19:00 MSK scheduler-2:
- Проверен `machine/issue/backlog`: доступна только `049-import-tables-from-database-connection.md` с cost L.
- По правилу scheduler-2 задача L не подходит для взятия в работу; M/S/XS задач в backlog нет.
- `049` дополнительно заблокирована до закрытия `sprint_15/054` по безопасной границе replace-import.
- Новая задача не взята; безопасное следующее действие — закрыть уже запланированную S-задачу `sprint_15/054` или после этого нарезать `049` на M-срезы.

analysis - 2026-05-17 19:03 MSK scheduler-1:
- Проверен `machine/issue/backlog`: доступна только `049-import-tables-from-database-connection.md`.
- Оценка/приоритет подтверждены: high / L. Scope остается DB connection UI + profiles CRUD + schema/catalog selection + PostgreSQL metadata mapping/FK import + replace-import + progress/error handling.
- В новый sprint напрямую не переносится: L-задача заблокирована до закрытия `sprint_15/054` по безопасной границе replace-import и metadata для повторного replace.
- Подходящих M/S/XS задач в backlog нет; XL-задач нет.

analysis - 2026-05-17 19:30 MSK scheduler-2:
- Проверен `machine/issue/backlog`: доступна только `049-import-tables-from-database-connection.md` с cost L.
- По правилу scheduler-2 L-задача не подходит для взятия в работу: лимит M/S/XS или минимальная задача не выше среднего размера.
- `049` также небезопасно начинать до закрытия `sprint_15/054` по replace-boundary, чтобы не заложить риск потери пользовательских элементов при repeated import.
- Новая задача не взята; безопасное следующее действие — закрыть `sprint_15/054` или после этого нарезать `049` на M/S/XS engineering slices.

analysis - 2026-05-17 20:00 MSK scheduler-2:
- Проверен `machine/issue/backlog`: доступна только `049-import-tables-from-database-connection.md` с cost L.
- По правилу scheduler-2 L-задача не подходит для взятия в работу: лимит M/S/XS или минимальная задача не выше среднего размера.
- `049` также небезопасно начинать до закрытия `sprint_15/054` по replace-boundary, чтобы не заложить риск потери пользовательских элементов при repeated import.
- Новая backlog-задача не взята; безопасное следующее действие — закрыть `sprint_15/054` или после этого нарезать `049` на M/S/XS engineering slices.

analysis - 2026-05-17 20:03 MSK scheduler-1:
- Проверен `machine/issue/backlog`: доступна только эта canonical L-задача.
- Оценка/приоритет подтверждены: high / L. Scope остается vertical end-to-end PostgreSQL import: connection UI, profiles CRUD, schema/catalog selection, metadata mapping/FK import, replace semantics, progress/error handling.
- В `sprint_15` напрямую не переносится: реализация заблокирована до закрытия `sprint_15/054` по безопасной границе replace-import и необходимости import metadata.
- Подходящих M/S/XS задач в backlog нет; XL-задач нет.

analysis - 2026-05-17 20:30 MSK scheduler-2:
- Проверен `machine/issue/backlog`: доступна только `049-import-tables-from-database-connection.md` с cost L.
- По правилу scheduler-2 L-задача не подходит для взятия в работу: лимит M/S/XS или минимальная задача не выше среднего размера.
- `049` также небезопасно начинать до закрытия `sprint_15/054` по replace-boundary, чтобы не заложить риск потери пользовательских элементов при repeated import.
- Новая backlog-задача не взята; безопасное следующее действие — закрыть `sprint_15/054` или после этого нарезать `049` на M/S/XS engineering slices.

analysis - 2026-05-17 20:48 MSK SEE decision:
- Replace boundary закрыт: repeated DB import полностью заменяет всё содержимое выбранной DB-диаграммы.
- Import-source metadata для imported-subset replace не требуется, потому что subset replace не выбран.
- `sprint_15/054` можно закрывать; `049` больше не заблокирована product-safety вопросом replace boundary.

analysis - 2026-05-17 21:00 MSK scheduler-2:
- Проверен `machine/issue/backlog`: доступна только `049-import-tables-from-database-connection.md` с cost L.
- Replace-boundary blocker закрыт через `sprint_15/054`: repeated import заменяет всё содержимое выбранной DB-диаграммы; import metadata для subset replace не требуется.
- По правилу scheduler-2 L-задача всё ещё не подходит для взятия в работу: лимит M/S/XS или минимальная задача не выше среднего размера.
- Новая backlog-задача не взята; безопасное следующее действие — нарезать `049` на M/S/XS engineering slices или передать L-задачу scheduler/исполнителю, которому разрешены L.

analysis - 2026-05-17 21:01 MSK scheduler-3:
- L/high подтверждены: в коде нет PostgreSQL/JDBC metadata import, local connection profile CRUD, schema/catalog picker, whole-diagram replace-import service/UI или progress/error flow для DB import.
- Product blocker закрыт через `sprint_15/054`: repeated import полностью заменяет всё содержимое выбранной DB-диаграммы; imported-subset metadata в project JSON для MVP не требуется.
- Дубликаты не создавались: `049` остается единственной canonical backlog-задачей для DB import implementation.
- Открытый вопрос теперь только scheduling/engineering-size: брать `049` как один L vertical slice или перед исполнением нарезать на M-срезы.

progress - 2026-05-17 21:03 MSK scheduler-1: moved from backlog to sprint_16 as high-priority L implementation slice after `sprint_15/054` closed replace boundary. Status set to planned; no product-safety blockers remain.

decomposition - 2026-05-17 21:43 MSK SEE decision:
- `049` не реализовывать одним L-срезом; нарезать на M-срезы.
- Созданы M-slices:
  1. `049-01-local-connection-profiles-crud.md` — local profiles model/storage + full CRUD + redaction.
  2. `049-02-postgresql-metadata-mapper.md` — PostgreSQL JDBC metadata mapper/import result, headless.
  3. `049-03-import-modal-ui-progress-errors.md` — modal import UI, profile/schema flow, progress/errors, destructive warning.
  4. `049-04-whole-diagram-replace-integration.md` — apply import result by replacing all content of selected DB diagram.
- `049` остается parent/umbrella для полного MVP capability; execution should happen through child M-slices.

progress - 2026-05-18 14:05 MSK scheduler-1: parent/umbrella актуализирован `planned` → `done`, потому что все executable child M-slices `049-01`..`049-04` закрыты, а safety/hardening follow-ups `056` и `057` также закрыты. Открытых L-вопросов по capability не осталось.

engineering decisions - 2026-05-17 22:21 MSK:
- Remaining L engineering questions are closed; child M-slices should use these decisions as implementation constraints.
- Profiles are portable with launcher-adjacent local file storage.
- PostgreSQL JDBC driver is an explicit Maven dependency.
- Progress is stage-based indeterminate with table counter.
- Empty project/no DB diagram path auto-creates a DB diagram.
- Non-MVP DB objects/constraints are ignored: unique, indexes, defaults, check constraints, sequences, views/materialized views and anything beyond tables/columns/types/nullable/PK/FK.

scope sync - 2026-05-18 20:30 MSK scheduler-2:
- Product/design docs now explicitly distinguish this live PostgreSQL metadata import capability from excluded SQL DDL text import/export.
- Scope remains narrow: PostgreSQL live connection metadata import only; no SQL parsing, DDL export, generic DB vendors, or merge import promise.
