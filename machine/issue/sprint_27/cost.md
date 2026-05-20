# Sprint 27 plan / hourly backlog analysis

Актуализировано: 2026-05-19 14:06 Europe/Moscow.

## Найдено в `machine/issue/backlog`

На входе найдено 2 задачи:

| Порядок | Issue | Status | Priority | Cost | Комментарий |
|---:|---|---|---:|---:|---|
| 1 | `065-surface-all-db-import-warnings.md` | backlog → planned | mid → high | S | executable follow-up после закрытой `059`: UI должен показывать все import warnings, а не только первый. |
| 2 | `066-import-composite-primary-key-metadata.md` | backlog | mid | M | важная semantic-correctness задача, но зависит от стабилизации `025`/table-level constraints; пока оставлена в backlog. |

После анализа в `machine/issue/backlog/` осталась 1 задача: `066-import-composite-primary-key-metadata.md`.

## Актуализация текущего sprint

`sprint_26` актуализирован как закрытый DB-import policy sprint: `059-db-import-composite-foreign-key-policy.md` имеет `status - done`, `priority - mid`, `cost - M`, с пройденными core/client Maven checks по заметкам scheduler-2.

Параллельно `sprint_4/025-db-composite-constraints-and-relation-semantics.md` сейчас `in_progress` и закрывает core/model часть table-level constraints; это влияет на готовность `066`.

## Новый active sprint_27

Цель sprint_27: закрыть малый UI/diagnostics follow-up после `059`, чтобы DB import не скрывал множественные warning cases.

| Порядок | Issue | Status | Priority | Cost | Комментарий |
|---:|---|---|---:|---:|---|
| 1 | `065-surface-all-db-import-warnings.md` | planned | high | S | повышен до high: после `059` warnings уже являются частью safety/degradation contract, но UI показывает только первый warning. |

## Что изменено

- Создан `machine/issue/sprint_27/`.
- `065-surface-all-db-import-warnings.md` перенесена из backlog в `sprint_27/`.
- В `065` изменены metadata: `status - backlog` → `status - planned`, `priority - mid` → `priority - high`; cost `S` сохранен.
- `066` не переносилась и не менялась: оставлена в backlog до стабилизации `025`.

## Перемещено / оценено / приоритизировано

- Перемещено:
  - `backlog/065-surface-all-db-import-warnings.md` → `sprint_27/065-surface-all-db-import-warnings.md`.
- Оценено/подтверждено:
  - `065` — S/high/planned: небольшой UI-slice без расширения в persistent import report.
  - `066` — M/mid/backlog: import/replacer slice для composite PK; executable после core/model readiness из `025`.
- Приоритизация ближайшего пула:
  1. `sprint_27/065` — high/S/planned: немедленный follow-up к уже реализованным import warnings.
  2. `sprint_4/025` — high/M/in_progress: завершить UI/canvas/SVG/inspector labels и relation-semantics acceptance.
  3. `backlog/066` — mid/M/backlog: брать после стабилизации table-level constraints.

## L / вопросы

L-задач в доступном backlog и новом sprint_27 не найдено; уточняющие вопросы не требуются.

## XL-декомпозиция

XL-задач в доступном backlog не найдено; декомпозиция не выполнялась.

## Блокеры / неясности

- Структура backlog ясна: `backlog/` содержит только backlog-задачи; `sprint_N/` — canonical planning/history.
- Для `066` функциональный блокер: дождаться завершения/стабилизации `025` table-level composite constraints, иначе есть риск делать import mapping поверх меняющейся модели.
- В git уже есть широкий набор незакоммиченных issue-tree изменений из предыдущих проходов; этот проход менял только planning files (`065` move/status/priority и новый `sprint_27/cost.md`).
- Production code не менялся; тесты не запускались, потому что это planning-only проход.

## Scheduler-2 execution — 2026-05-19 15:30 Europe/Moscow

### Выбор

`machine/issue/backlog/` на момент запуска пуст, поэтому взята ближайшая уже спланированная подходящая задача: `sprint_27/065-surface-all-db-import-warnings.md` — high/S. Это самый приоритетный доступный S/M-or-less follow-up: после `059` backend уже возвращает warnings, но UI скрывал все, кроме первого.

### Что сделано

- `065` переведена `planned → done`.
- `DbImportDialog` теперь формирует success status через отдельный форматтер:
  - no warnings: прежний текст, auto-close сохраняется;
  - single warning: прежний простой inline warning;
  - multiple warnings: `Warnings (N)` + полный список строк.
- Warning text редактируется через существующую redaction-логику перед показом.
- Добавлен headless `DbImportDialogTest` на no/single/multiple warnings без PostgreSQL.

### Проверка

```bash
JAVA_HOME=/home/openclaw/workspace/java/openjdk-25.0.2 \
PATH=/home/openclaw/workspace/java/apache-maven-3.9.11/bin:/home/openclaw/workspace/java/openjdk-25.0.2/bin:$PATH \
mvn -q -pl client -am -Dtest=DbImportDialogTest -Dsurefire.failIfNoSpecifiedTests=false test
```

Результат: passed.

### Блокеры

Backlog как директория пуст, поэтому задача взята не из `backlog/`, а из ближайшего planned sprint. В рабочем дереве остается широкий незакоммиченный фон из других проходов; этот проход ограничен `DbImportDialog`, новым тестом и metadata `065`/`sprint_27`.
