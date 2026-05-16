summary - Доработать canvas rendering для FK direction, selection и pin badges

goal - Сделать FK-flow визуально читаемым: пользователь должен видеть направление связей, выбранные объекты и pinned объект.

description - Улучшить отрисовку DB canvas: directional FK lines/arrow hint, визуальный marker selected table/column, badge pinned table/column, визуальное отличие FK validation warning/error, и подготовка к FK edge selection без полной реализации edge navigation. Rendering должен учитывать viewport transform и не ломать SVG export. Достаточно MVP-графики без crow's foot notation.

priority - mid
status - done
result - Canvas rendering доработан для FK/pin/selection: FK edges стали направленными с arrowhead и label, preview использует направленную dashed edge, validation edges сохраняют warning/error цвета, pin badges и selected table/column markers отображаются вместе с viewport transform.
check - SEE_REPO=http://89.223.121.28:8181 mvn -q -DskipTests compile
