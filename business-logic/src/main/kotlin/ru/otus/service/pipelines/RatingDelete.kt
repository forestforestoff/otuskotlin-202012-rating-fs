package ru.otus.service.pipelines

import ru.otus.cor.IOperation
import ru.otus.cor.pipeline
import ru.otus.model.context.ExchangeContext
import ru.otus.repository.dao.RatingRepoPostgresqlExposed.delete
import ru.otus.service.utils.completePipeline
import ru.otus.service.utils.idNotBlankValidation
import ru.otus.service.utils.runningPipeline

object RatingDelete : IOperation<ExchangeContext> by pipeline({
    execute(runningPipeline)
    validate(idNotBlankValidation)
    execute { delete() }
    execute(completePipeline)
})
