summary - Зафиксировать MVP release gate без обязательных regression tests

goal - Формализовать текущий MVP gate после решения SEE: локальный compile/package без обязательных regression tests, UI smoke, manual checklist и GitHub Actions.

description - Исходный scope про расширение regression test-suite больше не актуален для текущего MVP: SEE 2026-05-17 уточнил, что новые regression tests сейчас не нужны. Оставшийся backlog-срез — зафиксировать единый локальный Maven gate `mvn clean package -DskipTests` с project-local Java/Maven, убрать из планировочных summary ожидание обязательной нарезки test-suite и оставить тесты как optional follow-up, не блокирующий MVP.

priority - mid
cost - S
status - done
startedAt - 2026-05-15T10:50:00+03:00
note - Взят безопасный малый срез: базовые core unit tests для command layer и deterministic JSON writer без UI/DISPLAY.
progress - Added JUnit 5 test dependency/surefire gate and first core tests for undo/redo and deterministic JSON writer. Verification blocked locally: `mvn` and `javac` are not installed in this runtime.

clarification - Решения SEE от 2026-05-15:
- Минимальный release gate: compile + tests + package. UI smoke в MVP-gate не нужен.
- Source of truth для gate: локальный Maven contract. GitHub Actions не обязателен.

clarification - Решения SEE от 2026-05-15 19:06 MSK:
- Отдельный manual release checklist-файл не нужен; достаточно локального Maven gate: compile + tests + package.

clarification - Решения SEE от 2026-05-15 20:28 MSK:
- Подтверждено повторно: достаточно compile + tests + package; отдельный manual release checklist не нужен.

clarification - Решение SEE от 2026-05-17 14:43 MSK:
- Тесты сейчас не нужны: не добавлять новые regression tests и не считать test-suite обязательной частью текущего MVP gate.
- Текущий MVP gate сводится к локальному Maven compile/package без UI smoke/manual checklist и без обязательного GitHub Actions.

mvpGate - Локальный Maven contract: compile + package, без обязательных tests, без отдельного UI smoke/manual checklist и без обязательного GitHub Actions.

Команда gate:
```bash
JAVA_HOME=/home/openclaw/workspace/java/openjdk-25.0.2 \
PATH=/home/openclaw/workspace/java/apache-maven-3.9.11/bin:/home/openclaw/workspace/java/openjdk-25.0.2/bin:$PATH \
mvn clean package -DskipTests
```

Gate считается пройденным, если:
- Maven reactor проходит полностью для root project и всех модулей (`core`, `client`).
- `compile` проходит без ошибок.
- Тесты не являются обязательным gate для текущего MVP-среза; существующие тесты можно запускать отдельно по необходимости.
- `package` собирает артефакты:
  - `core/target/core-0.0.1-SNAPSHOT.jar`
  - `client/target/client-0.0.1-SNAPSHOT.jar`
- Команда не требует `DISPLAY` и не запускает JavaFX UI как часть MVP gate.
- Для planning-only изменений дополнительно достаточно `git diff --check`; Maven compile/package gate нужен перед закрытием задачи/релизного среза.

Acceptance criteria для 010/MVP:
- В проекте зафиксирован единый локальный release gate: `mvn clean package -DskipTests` с локальными Java/Maven из workspace.
- Gate покрывает compile + package одной командой.
- Новые regression tests/test-suite work не входят в текущий MVP scope.
- UI/manual smoke и CI workflow явно вынесены из MVP scope, чтобы не блокировать локальный релизный срез.

analysis - 2026-05-17 14:03 MSK scheduler-1:
- Product decisions по MVP gate обновлены: текущий local Maven contract `mvn clean package -DskipTests`, без обязательных tests/UI smoke/manual checklist в MVP.
- Локальные Java/Maven в workspace доступны; старые notes про отсутствие `mvn/java/javac` больше не считать актуальным блокером окружения.

analysis - 2026-05-17 15:00 MSK scheduler-3:
- Оценка снижена с L до S после решения SEE 2026-05-17 14:43: обязательная test-suite нарезка больше не входит в MVP scope.
- Оставшийся scope — metadata/docs normalization вокруг MVP gate и удаление stale planning expectations про mandatory regression tests.

progress - 2026-05-17 15:04 MSK scheduler-1: moved from backlog to sprint_14; status set to planned. Scope remains S: finalize MVP gate metadata/docs around `mvn clean package -DskipTests`, no new mandatory regression tests.

progress - 2026-05-17 17:31 MSK scheduler-2: взят в работу как безопасный S-срез текущего sprint_14, потому что в backlog нет задач <= M; canonical backlog содержит только L-задачу `049` с replace-boundary blocker. Локальный MVP gate выполнен: `mvn clean package -DskipTests` с project-local Java/Maven прошел успешно, собраны `core/target/core-0.0.1-SNAPSHOT.jar` и `client/target/client-0.0.1-SNAPSHOT.jar`.
