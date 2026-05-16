summary - Подготовить релизный запуск и packaging smoke для Linux/Windows/macOS

goal - Приблизить приложение к реальному desktop-релизу на заявленных MVP-платформах.

description - MVP требует Java JAR / shell script и поддержку Linux, Windows, macOS. Сейчас есть shell launcher для локального Linux окружения и Maven build. Нужно оформить portable runnable artifact: fat/lib distribution или documented zip, launch scripts для Linux/macOS и Windows `.bat`/`.ps1`, понятную настройку Java 25/JavaFX runtime, версионирование приложения и release README. Нужно проверить, что запуск не зависит от локального `~/workspace/java` и специфичного окружения разработчика, кроме явно документированных требований.

priority - mid
cost - M
status - in_progress
note - Взята как минимальная доступная задача без оценки размера: локальная packaging-секция сводится к безопасным launcher-скриптам и не требует менять доменную логику.
progress - Исправлен POSIX launcher: больше не перетирает внешний JAVA_HOME, оставлен fallback на локальный JDK. Добавлены Windows launchers `bin/schemeonyou.bat` и `bin/schemeonyou.ps1` с тем же classpath и поддержкой runtime deps из `target/lib`.
