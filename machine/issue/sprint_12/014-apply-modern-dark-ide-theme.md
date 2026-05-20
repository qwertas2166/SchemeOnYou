summary - Применить modern dark IDE-like theme к SchemeOnYou UI

status - done
priority - high
cost - M

goal - Привести визуальный стиль приложения к зафиксированному направлению: самостоятельный modern canvas tool, dark-only, compact IDE-like UI, спокойный темно-серый canvas.

description - На основе `design/ui-theme-decisions-v01.md` обновить JavaFX UI: фон приложения, панели, canvas, table cards, inspector, context/status footer, selection/focus, semantic labels/colors. Дизайн должен быть не прямым клоном IntelliJ, но сохранять IDE-плотность и понятную иерархию.

requirements -
- Только dark theme в MVP.
- Canvas темный спокойный серый, не black.
- Inspector плотный IDE-like.
- Context line остается снизу.
- Максимум текстовых labels для состояний: PK/FK/PIN/WARN/ERROR.
- Семантические цвета фиксированы.
- Color-blind mode не входит в MVP.
- Drag-and-drop остается free movement без snap/grid guides.

acceptance -
- UI использует палитру из `design/ui-theme-decisions-v01.md` или близкие значения.
- Canvas, panels, cards, footer визуально согласованы.
- Selected/focused/pinned/validation states различимы текстом и цветом.
- Inspector стал визуально плотным: меньше лишних отступов, IDE-like rows.
- Context/status footer не перегружен и сохраняет v07 priority model.
- `mvn test` и `mvn -DskipTests package` проходят.

risks -
- Нельзя ухудшить читаемость canvas на темном фоне.
- Нельзя спрятать смысл состояния только в цвете; нужны labels.
- Не превратить UI в прямую копию IntelliJ: нужен самостоятельный modern canvas feel.

progress 2026-05-15 scheduler-2 -
- moved from backlog to work
- added fixed dark MVP palette in `SchemeOnYouApplication`
- added JavaFX stylesheet `client/src/main/resources/see/schemeonyou/ui/theme.css` for controls/list/buttons/text fields
- updated canvas/panel/footer/table/FK preview colors toward `design/ui-theme-decisions-v01.md`
- tightened inspector spacing and column editor rows

verification -
- static brace balance check passed for `SchemeOnYouApplication.java`
- grep check found no old hard-coded dark palette literals in `SchemeOnYouApplication.java`
- blocked: `mvn`, `java`, and `javac` are not available in this runner, so `mvn test` / package could not be executed here

progress -
- 2026-05-17 02:01 MSK: взята в работу как следующая доступная задача из backlog.
validation -
- 2026-05-17 02:12 MSK: `JAVA_HOME=/home/openclaw/workspace/java/openjdk-25.0.2 PATH=/home/openclaw/workspace/java/apache-maven-3.9.11/bin:/home/openclaw/workspace/java/openjdk-25.0.2/bin:$PATH mvn -q test` — passed.
- 2026-05-17 02:12 MSK: `JAVA_HOME=/home/openclaw/workspace/java/openjdk-25.0.2 PATH=/home/openclaw/workspace/java/apache-maven-3.9.11/bin:/home/openclaw/workspace/java/openjdk-25.0.2/bin:$PATH mvn -q -DskipTests package && git diff --check` — passed.

progress - 2026-05-17 13:05 MSK scheduler-1: moved from `work/` to `sprint_12/` as done artifact during hourly backlog normalization; `work/` kept for active tasks only.
