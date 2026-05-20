summary - Укрепить JSON/SVG escaping и тесты на спецсимволы пользовательских имён
status - done
priority - mid
cost - S

goal - Гарантировать валидный Git-friendly JSON и SVG export при пользовательских именах с кавычками, backslash, переводами строк, табами, CR и XML-значимыми символами.

description - Parser/open flow активно hardenится в `001-open-project-loading.md`, но writer/export тоже должны быть устойчивыми. `SchemeProjectJsonWriter.Json.q()` сейчас экранирует backslash, quote и LF, но не все JSON control chars (`\r`, `\t`, другие `< 0x20`). `SvgExporter.escape()` защищает `&`, `<`, `>`, но стоит добавить regression coverage для кавычек, apostrophe, control chars/line breaks и убедиться, что SVG/XML остаётся валидным или явно нормализует недопустимые символы. Это важно для readable deterministic storage/export и будущих save/load round-trip тестов.

acceptance criteria -
- JSON writer экранирует все JSON control chars согласно JSON spec, включая tab/CR/backspace/form-feed или unicode escapes.
- Save -> load round-trip проходит для имён project/diagram/table/column/participant/message label со спецсимволами.
- SVG export корректно экранирует/нормализует текстовые значения и не создаёт невалидный XML для поддерживаемых символов.
- Добавлены regression tests для JSON writer/reader и SVG exporter.
- Детерминированность output сохранена.

dependencies/risks -
- Related to active issue `work/001-open-project-loading.md`, но scope writer/export, не parser-only.
- Risk: XML 1.0 control-char правила отличаются от JSON; нужно выбрать простую нормализацию и зафиксировать её в тестах.

progress -
- 2026-05-16 scheduler-2: взято как S-задача для cron-слота; расширен JSON writer на все control chars, SVG text escaping/normalization, добавлены regression tests для JSON round-trip и SVG XML parse. Валидация заблокирована окружением: `mvn` и `java` отсутствуют.
- 2026-05-19 21:33 MSK scheduler-3: повторно проверено с project-local Java/Maven: JSON writer экранирует quote/backslash/LF/CR/tab/backspace/form-feed/other control chars, SVG exporter экранирует XML-sensitive chars и заменяет invalid XML text chars. Verification: `mvn -q test`, `mvn -q -DskipTests package`, JSON validation and `git diff --check` passed; Java/Guice/JavaFX warnings only. Status set to done.
