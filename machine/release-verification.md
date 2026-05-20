# Release verification contract

Canonical local verification commands for SchemeOnYou agents and release checks.

## Toolchain

Use the project-local Java/Maven toolchain when system `java`/`mvn` are absent or older than the project target:

```bash
export JAVA_HOME=/home/openclaw/workspace/java/openjdk-25.0.2
export PATH=/home/openclaw/workspace/java/apache-maven-3.9.11/bin:$JAVA_HOME/bin:$PATH
```

The project targets Java 25. Do not report `mvn`, `java`, or `javac` as unavailable until this fallback has been tried.

## Repository dependencies

The root `pom.xml` reads the private dependency repository from `SEE_REPO`:

```bash
export SEE_REPO=${SEE_REPO:-http://89.223.121.28:8181}
```

If the dependency repository is unavailable, report that as a dependency/repository blocker, not as a Maven or Java blocker.

## Local connection profile privacy

`schemeonyou-connection-profiles.local` and `schemeonyou-connection-profiles.local.tmp` are local-only launcher-adjacent files that may contain PostgreSQL connection secrets. They are not project JSON/export artifacts and must not be committed. The repository `.gitignore` must keep those filenames ignored anywhere inside the checkout; use explicit non-secret fixture names for tests.

## Minimum MVP release gate

Current MVP decision from `machine/issue/sprint_14/010-tests-and-release-gates.md`: the mandatory local gate is compile/package, without mandatory regression tests, UI smoke, manual checklist, or GitHub Actions.

Run from the repository root:

```bash
mvn clean package -DskipTests
git diff --check
```

This gate passes when the Maven reactor compiles and packages `core` and `client` artifacts without starting the JavaFX UI.

## Optional extended verification

Run tests when useful for implementation confidence or when closing test-covered work, but do not treat them as mandatory for the current MVP release gate unless the specific task says so:

```bash
mvn -q test
mvn -q -pl core test
mvn -q -pl core -Dtest=MvpScaleSmokeTest test
```

The `MvpScaleSmokeTest` is a deterministic core-only MVP scale smoke: it builds a 200-table / 500-FK DB diagram, runs layout, validation, JSON round-trip, and SVG export without JavaFX DISPLAY.

## Core-only fallback

When client build output is blocked by local filesystem ownership or stale generated files, the core-only check is still useful but is not a full compile/package gate:

```bash
mvn -q -pl core test
```

If full verification fails with a generated-file permission error under `target/`, report the exact path and do not silently replace the compile/package gate with the core-only gate.

## JavaFX native-access warning

JavaFX tests or runtime commands may print warnings about restricted/native access on Java 25. Treat the warning as expected unless the command exits non-zero; do not hide real failures behind the warning.
