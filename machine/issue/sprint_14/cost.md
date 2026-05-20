# Sprint 14 plan / hourly backlog analysis

Актуализировано: 2026-05-17 17:31 Europe/Moscow.

## Найдено в `machine/issue/backlog`

| Issue | Status | Priority | Cost | Решение |
|---|---|---:|---:|---|
| `049-import-tables-from-database-connection.md` | backlog | high | L | оставить в backlog до ответа на replace-boundary вопрос; не переносить в sprint_14 |

## Актуальный `sprint_14`

| Порядок | Issue | Status | Priority | Cost | Комментарий |
|---:|---|---|---:|---:|---|
| 1 | `010-tests-and-release-gates.md` | done | mid | S | MVP gate подтвержден локально: `mvn clean package -DskipTests`; без обязательных новых regression tests/UI smoke/CI |

## Что изменено

- Актуализирован hourly summary для `sprint_14` на 17:31 MSK.
- `sprint_14` не расширялся: единственная backlog-задача `049` имеет размер L и открытый product-safety blocker.
- В `049` добавлена свежая analysis-запись scheduler-1: оценка/приоритет подтверждены, перенос заблокирован replace-boundary вопросом.
- `010` закрыта scheduler-2: локальный gate `mvn clean package -DskipTests` успешно прошел с project-local Java/Maven.

## Перемещено / оценено / приоритизировано

- Перемещено в новый sprint: ничего — подходящих S/M задач в backlog нет.
- Оценки подтверждены:
  - `049` = L: DB connection flow + saved profiles CRUD + PostgreSQL metadata mapping + replace import + UI/progress/error handling.
  - `010` = S: текущий sprint_14 cleanup, без обязательной test-suite нарезки.
- Приоритеты подтверждены:
  1. `049` — high / L, главный backlog item, но требует уточнения replace boundary перед взятием в sprint.
  2. `010` — mid / S, единственная задача текущего sprint_14; закрыта после успешного локального package gate.

## L / вопросы

### `049-import-tables-from-database-connection.md`

Остался один уточняющий вопрос:
1. При repeated replace-import нужно заменять только DB-imported элементы активной DB-диаграммы или полностью всё содержимое выбранной DB-диаграммы?

## XL-декомпозиция

XL-задач в `machine/issue/backlog` не найдено; декомпозиция не выполнялась.

## Блокеры / неясности

- Структура backlog ясна: `backlog/`, `sprint_N/`, `work/` используются ожидаемо.
- Блокер по переносу `049`: нужно решение по replace boundary, чтобы не заложить опасную семантику удаления/перезаписи пользовательских элементов.

## Progress 2026-05-17 17:31 MSK scheduler-2

- Подходящих backlog-задач <= M нет: единственный backlog item `049` имеет cost L и blocker по replace boundary.
- Безопасно взят и закрыт текущий S-срез `010` из `sprint_14`.
- Verification: `mvn clean package -DskipTests` с `/home/openclaw/workspace/java/openjdk-25.0.2` и `/home/openclaw/workspace/java/apache-maven-3.9.11` — BUILD SUCCESS.
