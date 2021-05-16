package ru.otus.service.pipelines

import ru.otus.cor.IOperation
import ru.otus.cor.pipeline
import ru.otus.model.context.ExchangeContext
import ru.otus.repository.dao.RatingRepoPostgresqlExposed.update
import ru.otus.service.utils.completePipeline
import ru.otus.service.utils.runningPipeline
import ru.otus.service.utils.voteUpdateValidation

object RatingUpdate : IOperation<ExchangeContext> by pipeline({
    execute(runningPipeline)
    validate(voteUpdateValidation)
    execute { update() }
    execute(completePipeline)
})
