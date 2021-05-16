package ru.otus.service.pipelines

import ru.otus.cor.IOperation
import ru.otus.cor.pipeline
import ru.otus.model.context.ExchangeContext
import ru.otus.repository.dao.RatingRepoPostgresqlExposed.read
import ru.otus.service.utils.completePipeline
import ru.otus.service.utils.idNotBlankValidation
import ru.otus.service.utils.runningPipeline

object RatingRead : IOperation<ExchangeContext> by pipeline({
    execute(runningPipeline)
    validate(idNotBlankValidation)
    execute { read() }
    execute(completePipeline)
})
