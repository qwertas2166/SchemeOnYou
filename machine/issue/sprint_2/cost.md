# Sprint 2 plan / status

Актуализировано: 2026-05-15 14:05 Europe/Moscow.

## Состав спринта

| Issue | Status | Priority | Cost | Комментарий |
|---|---|---:|---:|---|
| `014-done-sync-runtime-keymap-with-machine-design-docs.md` | done | high | S | Закрыт consistency-fix runtime keymap 0/1/2/3 vs legacy F6 в docs/design/machine summary. |
| `018-done-sync-machine-requirements-keymap-runtime.md` | done | high | S | Закрыт остаточный drift в `machine/requirements.json`; F6 оставлен только legacy/replaced. |
| `015-non-modal-keyboard-flows-for-frequent-actions.md` | planned | high | M | Следующий главный UX debt: убрать blocking dialogs из частых keyboard-first flows. |
| `016-harden-json-and-svg-string-escaping.md` | planned | mid | S | Writer/export hardening и regression tests для спецсимволов; хорошо брать как короткий срез. |
| `017-release-readiness-environment-and-ci-parity.md` | planned | mid | M | Reproducible local/CI verification contract, снимает ложные blockers по Maven/JDK. |

## Актуальная приоритезация sprint_2

1. `015` — high/M: максимальный UX-impact и зависимость для будущего delete/join non-modal подтверждения.
2. `016` — mid/S: компактный hardening, полезен для save/load/export надежности.
3. `017` — mid/M: нужен для release gates и устранения повторяющихся environment blockers.

## Что изменилось за проход

- Добавлена завершенная `018` в план sprint_2.
- Приоритеты пересобраны с учетом двух закрытых S consistency-задач: активный фокус теперь `015`, затем `016`, затем `017`.
- Новый доступный backlog перенесен в `sprint_3/`; sprint_2 не расширялся, чтобы не размывать текущий фокус.

## L/XL

В `sprint_2/` L и XL не найдено.
