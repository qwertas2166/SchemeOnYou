summary - Зафиксировать границу replace-import для DB metadata import

status - done
priority - high
cost - S

goal - Разблокировать реализацию `049` безопасным решением о том, какие элементы можно заменять при повторном импорте схемы БД.

description - Задача `049-import-tables-from-database-connection.md` требует repeated import в режиме `replace`, но сейчас не зафиксирована product/model граница replace. Без этого vertical slice может либо потерять пользовательские таблицы/связи в активной DB-диаграмме, либо оставить дубли после повторного импорта. Нужно принять и записать короткое решение: заменяется вся выбранная DB-диаграмма или только элементы, помеченные как imported-from-DB; нужны ли source/import metadata в модели для безопасного повторного replace; как UI предупреждает пользователя перед replace.

acceptance criteria -
- В `049` или отдельном machine/design note зафиксировано явное решение replace boundary: whole selected DB diagram vs imported subset.
- Если выбран imported subset, описан минимальный model marker/import batch/source metadata, достаточный для повторного replace без дублей.
- Если выбран whole diagram replace, acceptance criteria `049` уточнены явным предупреждением о полном удалении содержимого выбранной DB-диаграммы.
- UI warning text/semantics для replace-import описаны достаточно, чтобы реализация не делала silent destructive action.
- Решение не добавляет preview/diff requirement, потому что SEE уже исключил preview/diff из MVP.
- После решения `049` можно брать в work без открытого product-safety blocker.

dependencies/risks -
- Depends on SEE/product decision по границе replace-import.
- Related to `049-import-tables-from-database-connection.md`, но не дубликат: `054` фиксирует small decision/research slice, `049` остаётся L implementation slice.
- Risk: imported-subset вариант потребует metadata в storage и миграционное решение; whole-diagram вариант проще, но опаснее для пользовательских элементов.

decision - 2026-05-17 20:48 MSK SEE:
- При повторном DB import полностью заменять всё содержимое выбранной DB-диаграммы.
- Imported-subset replace не выбран.
- Metadata вроде `importSource/profile/schema/importBatchId` в project JSON для MVP не требуется.
- UI warning должен явно предупреждать, что всё текущее содержимое выбранной DB-диаграммы будет удалено и заменено импортом.

open questions -
- Нет открытых вопросов; replace boundary закрыт.


progress - 2026-05-17 18:03 MSK scheduler-1: moved from backlog to sprint_15 as high-priority S product-safety slice that unblocks `049`; status set to planned.

progress - 2026-05-17 19:03 MSK scheduler-1: актуализировано как первая задача sprint_15. Status остается planned: нужен ответ SEE/product decision по replace boundary; без него `049` не переносится в work/sprint implementation.

progress - 2026-05-17 20:03 MSK scheduler-1: актуализировано как первая задача sprint_15. Status остается planned; это минимальный high-priority blocker для `049`. Нужно решение SEE/product по replace boundary и, при imported-subset варианте, по metadata в project JSON.

progress - 2026-05-17 20:48 MSK SEE decision: выбран whole selected DB diagram replace. Status set to done; `049` разблокирована по replace boundary.
