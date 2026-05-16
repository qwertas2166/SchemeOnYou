status - done
result - Added persisted canvas viewport controls: Ctrl+Plus/Ctrl+Minus zoom, Ctrl+0 fit, Ctrl+1 actual size, Ctrl+Arrow keyboard pan, mouse drag/scroll pan, Ctrl+scroll zoom, toolbar buttons, viewport status overlay; SVG export remains model-coordinate based.
check - /home/openclaw/workspace/java/openjdk-25.0.2/bin/javac --release 25 on SchemeOnYou sources passed; whitespace check passed; mvn check could not run because mvn is not installed in PATH.
summary - Добавить zoom, pan/scroll и auto-fit canvas

goal - Закрыть требования Canvas MVP и сделать работу комфортной на диаграммах больше демо-примера.

description - Текущий canvas рисуется в фиксированных координатах без полноценного viewport state. Нужно добавить zoom in/out, fit whole diagram, actual size, keyboard pan через Ctrl+Arrow, scroll/pan мышью как опциональное дублирование, и хранение viewport/canvas state там, где это нужно для проекта. Shortcuts из дизайна: Ctrl+Plus/Ctrl+Minus, Ctrl+0 fit, Ctrl+1 100%. Export SVG должен оставаться независимым от текущего viewport.

priority - high
