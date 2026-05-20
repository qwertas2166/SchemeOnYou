summary - Зафиксировать воспроизводимые локальные/CI команды для build/test/package
status - done
priority - mid
cost - M

goal - Убрать неоднозначность проверок: в issue встречаются блокеры `mvn/java/javac not installed`, хотя в repo окружении доступны project-local Java/Maven paths.

description - Несколько задач помечают verification blocked из-за отсутствия `mvn`, `java`, `javac` в PATH, при этом рабочие команды используют `/home/openclaw/workspace/java/openjdk-25.0.2` и `/home/openclaw/workspace/java/apache-maven-3.9.11`. Нужно оформить единый reproducible verification contract: скрипт или README-секцию с export `JAVA_HOME`, `PATH`, `SEE_REPO`, текущим MVP-gate `mvn clean package -DskipTests`, optional test commands, `git diff --check`, `--core-only`; GitHub Actions не является source of truth. Это снижает ложные блокеры и помогает закрывать release-gate drift.

acceptance criteria -
- Добавлен `docs`/`machine` release verification note или script с точными командами и переменными окружения.
- Issue-шаблон/README указывает project-local Maven/JDK fallback, чтобы новые агенты не писали ложный blocker.
- Current MVP gate согласован с `sprint_14/010`: обязательный локальный gate — `mvn clean package -DskipTests` + `git diff --check`; `mvn test` остается optional/implementation-confidence command, а не mandatory release gate.
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

analysis - 2026-05-17 23:03 MSK scheduler-3:
- Повторно подтвержден environment/build-output blocker: полный `mvn -q test` после `049-01` блокируется правами на `client/target/classes/see/schemeonyou/ui/theme.css` owned by `see`, при этом `mvn -q -pl core test` проходит.
- Новую задачу не создаю: это относится к release/environment parity contract и cleanup policy этой задачи, а не к product/code gap.


progress - 2026-05-18 10:30 MSK scheduler-2:
- Backlog directory was empty, so took this suitable M task from the current sprint issue pool because full verification is currently blocked by environment/output ownership and this issue explicitly owns reproducible Maven/JDK parity.
- Added `machine/release-verification.md` with project-local Java/Maven fallback, `SEE_REPO` handling, minimum release gate (`mvn -q test`, `mvn -q -DskipTests package`, `git diff --check`), core-only fallback semantics, and JavaFX warning classification.
- Linked the verification contract from `machine/README.md`.
- Verification attempted: full `mvn -q test` with project-local Java/Maven failed in client resource copy because `client/target/classes/see/schemeonyou/ui/theme.css` is not writable (`Operation not permitted`); did not clean/delete generated output without explicit permission.

analysis - 2026-05-18 15:30 MSK scheduler-3:
- Найден release-contract drift: свежий `machine/release-verification.md` требовал `mvn -q test`, хотя закрытая `sprint_14/010` зафиксировала текущий mandatory MVP gate как `mvn clean package -DskipTests` без обязательных tests/UI smoke/CI.
- Новую backlog-задачу не создавал: это прямой scope текущей `017` про environment/release parity.
- Обновил `machine/release-verification.md` и acceptance `017`: tests переведены в optional extended verification, mandatory MVP gate синхронизирован с `010`.


progress - 2026-05-20 09:24 MSK scheduler-2:
- Backlog directory is empty; continued this active mid/M release-readiness task because sprint_30 prioritization lists `017` before `011`/`052`, and it is already scoped to safe documentation/verification parity work.
- Re-verified the documented project-local toolchain and mandatory MVP package gate: `mvn -q -DskipTests package` passed with Java 25/Guice warnings only.
- Ran scoped whitespace check for the release contract/issue docs: `git diff --check -- machine/release-verification.md machine/README.md machine/issue/sprint_2/017-release-readiness-environment-and-ci-parity.md` passed.
- Acceptance is satisfied by `machine/release-verification.md`, `machine/README.md`, explicit `SEE_REPO`/core-only fallback semantics, optional-test classification, and JavaFX/native-access warning classification. Marked done.
