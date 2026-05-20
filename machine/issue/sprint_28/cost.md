# Sprint 28 plan / scheduler-2 execution

Актуализировано: 2026-05-19 14:30 Europe/Moscow.

## Взято из `machine/issue/backlog`

| Issue | Status | Priority | Cost | Комментарий |
|---|---|---:|---:|---|
| `066-import-composite-primary-key-metadata.md` | backlog → done | mid | M | Единственная доступная backlog-задача; размер M входит в лимит scheduler-2. |

## Что сделано

- `066` перенесена в `machine/issue/sprint_28/` и закрыта как `status - done`.
- PostgreSQL PK metadata теперь читается с детерминированным порядком `KEY_SEQ`, fallback — имя колонки.
- `DbImportTable` хранит ordered `primaryKeyColumnNames`.
- `DbImportDiagramReplacer` создает table-level `COMPOSITE_PRIMARY_KEY` constraint для multi-column PK, не затрагивая single-column PK.
- Добавлены headless tests на ordering, constraint creation и JSON round-trip.

## Проверка

```bash
JAVA_HOME=/home/openclaw/workspace/java/openjdk-25.0.2 \
PATH=/home/openclaw/workspace/java/apache-maven-3.9.11/bin:/home/openclaw/workspace/java/openjdk-25.0.2/bin:$PATH \
mvn -q -pl core -Dtest=PostgreSqlMetadataImporterTest,DbImportDiagramReplacerTest test
```

Результат: passed.

## Блокеры

Блокеров на текущем срезе нет. Реализация опирается на уже присутствующие в рабочем дереве `DbTableConstraint` / `DbTableConstraintType`; широкий незакоммиченный фон проекта сохранен без нормализации.

## Hourly backlog analysis — scheduler-1, 2026-05-19 15:03 Europe/Moscow

### Найдено в `machine/issue/backlog`

Backlog пуст: файлов задач в `machine/issue/backlog/` не найдено.

### Актуализация текущего спринта

- Текущий последний спринт: `sprint_28`.
- `066-import-composite-primary-key-metadata.md` остается `status - done`, `priority - mid`, `cost - M`.
- Новых задач для включения в следующий спринт нет.

### Оценка / приоритезация / перенос

- Оценено новых задач: 0.
- Приоритизировано новых задач: 0.
- Перенесено в новый спринт: 0.

### L / XL

- L-задач в backlog нет — уточняющие вопросы не требуются.
- XL-задач в backlog нет — декомпозиция не выполнялась.

### Блокеры

- Блокеров по структуре backlog нет; структура ясна, но backlog сейчас пуст.
