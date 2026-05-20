summary - Показывать derived DB relation meaning в FK preview и синхронизировать preview warnings с composite uniqueness
status - done
result - FK preview now uses shared FkPreviewSemantics/DbRelationSemantics for errors, warnings, and derived meaning; inspector/context line show many-to-one/one-to-one/ambiguous labels.
check - JAVA_HOME=/home/openclaw/workspace/java/openjdk-25.0.2 PATH=/home/openclaw/workspace/java/apache-maven-3.9.11/bin:/home/openclaw/workspace/java/openjdk-25.0.2/bin:$PATH mvn -q -pl core,client -Dtest=FkPreviewSemanticsTest,InspectorPresenterTest,DatabaseConstraintValidationTest test
priority - mid
cost - S

goal - Закрыть оставшийся UX-gap после `025`: FK preview должен показывать понятную derived meaning строку и использовать тот же uniqueness helper, что validator/semantic model.

description - `sprint_4/025` добавил core `DbRelationSemantics`/`DbRelationMeaning` и validator теперь корректно различает individual uniqueness и composite PK/UNQ. Но runtime FK preview в `SchemeOnYouApplication` всё ещё показывает только статичное `Creates: source column references target column`, а warnings вычисляет локально через `targetColumn.isPrimaryKey() || targetColumn.isUnique()`. Из-за этого preview не выполняет design v07 promise про derived meaning строку и может дать ложный `target column is not PK or unique` для single-column table-level constraint, хотя validator/semantics считают такую колонку individually unique. Нужно переиспользовать общую semantics/uniqueness логику в preview/context line, не вводя editable relationship metadata и не расширяя MVP до crow's foot.

acceptance criteria -
- FK preview inspector/context line показывает derived relation meaning для валидного preview: `many-to-one`, `one-to-one` или ambiguous/unknown target в человекочитаемой форме.
- `fkPreviewWarnings(...)` использует `DbRelationSemantics.isIndividuallyUnique(...)` или общий helper, а не расходящуюся ручную проверку только column flags.
- Single-column table-level `COMPOSITE_PRIMARY_KEY`/`COMPOSITE_UNIQUE` не вызывает ложный preview warning `target column is not PK or unique`.
- Composite multi-column PK/UNQ по-прежнему не считается uniqueness для одиночного FK target и warning остаётся.
- Не добавляется editable relationship/cardinality metadata; meaning остаётся derived from FK/PK/unique/composite constraints.
- Добавлен regression test для preview/formatter/helper или выделенного presenter path без запуска JavaFX stage.

dependencies/risks -
- Depends on completed `sprint_4/025-db-composite-constraints-and-relation-semantics.md`.
- Related to done `sprint_1/005-02`/`005-03`, but not duplicate: those created FK preview/warnings before composite table-level uniqueness semantics existed.
- Risk: если formatter останется внутри `SchemeOnYouApplication`, тестирование будет сложнее; предпочтительно вынести маленький pure helper/presenter slice.
