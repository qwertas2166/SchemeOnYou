summary - Зафиксировать воспроизводимые локальные/CI команды для build/test/package
status - planned
priority - mid
cost - M

goal - Убрать неоднозначность проверок: в issue встречаются блокеры `mvn/java/javac not installed`, хотя в repo окружении доступны project-local Java/Maven paths.

description - Несколько задач помечают verification blocked из-за отсутствия `mvn`, `java`, `javac` в PATH, при этом рабочие команды используют `/home/openclaw/workspace/java/openjdk-25.0.2` и `/home/openclaw/workspace/java/apache-maven-3.9.11`. Нужно оформить единый reproducible verification contract: скрипт или README-секцию с export `JAVA_HOME`, `PATH`, `SEE_REPO`, командами `mvn test`, `mvn -DskipTests package`, `git diff --check`, `--core-only`; по возможности подключить это к GitHub Actions/локальному release gate. Это снижает ложные блокеры и помогает закрывать L-задачу tests/release gates.

acceptance criteria -
- Добавлен `docs`/`machine` release verification note или script с точными командами и переменными окружения.
- Issue-шаблон/README указывает project-local Maven/JDK fallback, чтобы новые агенты не писали ложный blocker.
- `mvn test` и `mvn -DskipTests package` запускаются одинаково локально и в CI/release gate, насколько возможно.
- Если внешний `SEE_REPO` обязателен, это явно документировано с безопасным failure message.
- Java 25 / JavaFX native-access warnings классифицированы: либо задокументированы как ожидаемые build/test warnings, либо для runtime/test/package команд добавлен согласованный `--enable-native-access=ALL-UNNAMED`/эквивалент без маскировки настоящих ошибок.
- Обновлены активные release/test issue notes ссылкой на новый contract.

dependencies/risks -
- Related to `work/010-tests-and-release-gates.md` and `sprint_1/011-packaging-and-cross-platform-launch.md`; задача меньше L и ограничена reproducibility contract.
- Risk: нельзя зашивать приватные/нестабильные пути как единственный способ для внешних contributors; нужен fallback/override.

clarification - Решения SEE от 2026-05-15:
- Минимальный release gate: compile + tests + package.
- UI smoke не входит в обязательный gate.
- GitHub Actions не является source of truth; достаточно локального Maven contract.


analysis - 2026-05-16 23:01 Europe/Moscow scheduler-3:
- Расширен scope существующей release-readiness задачи вместо создания дубля: Maven gate на локальном JDK 25 проходит, но JavaFX тесты печатают warning `Restricted methods will be blocked in a future release unless native access is enabled`. Это относится к release verification/runtime flags и покрывается этой M-задачей.
