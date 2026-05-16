summary - Добавить полноценные sequence participant/message свойства в model/storage/validation
status - planned
priority - high
cost - M

goal - Закрыть MVP-требования sequence diagram не только визуально, но и на уровне доменной модели, сохранения и проверки целостности.

description - Требования фиксируют participant types (`actor`, `service`, `database`, `external system`) и message types (`sync`, `async`, `return`, `self-call`), а также свойства message: source, target, label, order, type. Сейчас `SequenceParticipant` хранит только `id/name`, `SequenceMessage` хранит `from/to/label/activation`, а reader/writer/export/layout не различают типы, порядок как отдельное свойство и self/return/async semantics. Нужно добавить минимальные enum/value-поля в core model, расширить JSON format с backward-compatible defaults, добавить validation load/runtime checks для missing endpoints, duplicate/invalid order, self-call endpoint rules и unknown type values.

acceptance criteria -
- `SequenceParticipant` хранит participant type с допустимыми значениями actor/service/database/external_system и default для старых JSON.
- `SequenceMessage` хранит message type sync/async/return/self_call и явный order; activation остается отдельным visual flag или выводится документированно из типа/flow.
- JSON writer/reader сохраняет и читает новые поля детерминированно; старые файлы без type/order открываются с безопасными defaults.
- Reader/runtime validation явно отвергает или показывает ошибки для message endpoints, которые не ссылаются на существующих participants.
- SVG/canvas rendering хотя бы минимально отличает return/async/self-call или документирует одинаковую MVP-отрисовку без потери данных.
- Добавлены core tests на round-trip новых fields, backward compatibility и invalid endpoint/type/order cases.

dependencies/risks -
- Related to `work/007-sequence-diagram-mvp-ui.md`, но не дублирует UI-rendering: здесь core model/storage/validation contract.
- Related to `work/001-open-project-loading.md` for reader hardening; важно не сломать текущие save/load tests.
- Risk: миграция JSON формата может разрастись; держать schemaVersion/backward defaults простыми для MVP.

clarification - Решения SEE от 2026-05-15:
- MVP sequence ограничивается participant + message + activation; расширять beyond этого не нужно.
- SVG должен быть semantic enough, pixel-parity не требуется.
