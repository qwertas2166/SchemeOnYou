summary - Отполировать UI-доступность и согласовать keymap перед релизом

goal - Убрать UX-шероховатости, мешающие keyboard-first использованию и первому впечатлению от MVP.

description - После недавних изменений появились прямые focus shortcuts `0/1/2/3`, footer overlay и key log. Нужно привести подсказки, F1 overlay, ShortcutMap и требования/design к одному состоянию; решить судьбу F6/Tab vs 0-3; добавить видимый focus indicator для всех областей; проверить контрастность, tab order внутри панелей, поведение Space в текстовых полях и отсутствие mouse-only MVP-действий. Debug key log по F12 нужно либо оставить как dev-only tool, либо скрыть/документировать перед релизом.

priority - high
clarification - Решения SEE от 2026-05-15:
- Финальная навигация по крупным зонам: `0` верхнее меню, `1` левое меню, `2` canvas, `3` inspector.
- `Tab` используется только внутри панелей/полей, не для переключения крупных зон.
- `F12` key log остается в релизной сборке.
- Footer-overlay должен использовать пропорции из макета, а не произвольные 10%.

status - done
result - Согласована релизная keymap-политика: 0/1/2/3 для крупных зон, Tab только внутри панелей; F6 убран из runtime hints/handler, F12 key log оставлен. Добавлены видимые focus indicators для top menu/left menu/canvas/inspector, Space/цифровые focus shortcuts не перехватывают ввод в TextInputControl, canvas hint footer переведен на высоту макета 78px.
check - Выполнены: git diff --check для измененных sprint_1/ui файлов; попытки SEE_REPO=http://89.223.121.28:8181 mvn -q -DskipTests compile и javac compile не выполнены из-за отсутствия mvn/javac в окружении.
clarification - Решение SEE от 2026-05-16 15:44 MSK:
- Для `012` достаточно как ранее зафиксировано; дополнительных требований к этой done-задаче не добавлять.
