summary - Реализовать FK preview с Source/Target chips и Swap

goal - Сделать создание FK безопасным и понятным до изменения модели.

description - Для `Space A F` добавить preview-состояние перед созданием FK. Preview должен показывать явные роли: Source column/table и Target column/table, объяснять направление связи и поддерживать confirm/cancel. Если выбранная и pinned стороны могут трактоваться неоднозначно, preview должен показывать выбранную интерпретацию и локальный `X Swap`. Swap меняет роли только внутри видимого preview. Все действия должны быть keyboard-accessible, а создание FK после confirm должно идти через command layer.

priority - mid

status - done
result - Реализован FK preview для Space A F: создается preview с явными Source/Target role chips в inspector и canvas, Enter подтверждает создание FK через command layer, Esc отменяет preview, X выполняет локальный Swap ролей.
check - SEE_REPO=http://89.223.121.28:8181 mvn -q -DskipTests compile
