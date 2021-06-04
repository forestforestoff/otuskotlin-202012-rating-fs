package ru.otus.service.pipelines

import ru.otus.cor.IOperation
import ru.otus.cor.pipeline
import ru.otus.model.context.ExchangeContext
import ru.otus.repository.dao.RatingRepoPostgresqlExposed.create
import ru.otus.service.utils.completePipeline
import ru.otus.service.utils.moderatorsPermission
import ru.otus.service.utils.runningPipeline

object RatingCreate : IOperation<ExchangeContext> by pipeline({
    validate(moderatorsPermission)
    execute(runningPipeline)
    execute { create() }
    execute(completePipeline)
})
