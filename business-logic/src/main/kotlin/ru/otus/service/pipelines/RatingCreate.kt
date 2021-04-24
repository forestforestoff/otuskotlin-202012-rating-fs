package ru.otus.service.pipelines

import ru.otus.cor.IOperation
import ru.otus.cor.pipeline
import ru.otus.model.Rating
import ru.otus.model.context.ExchangeContext
import ru.otus.service.utils.completePipeline
import ru.otus.service.utils.runningPipeline

object RatingCreate : IOperation<ExchangeContext> by pipeline({
    execute(runningPipeline)
    execute {
        rating = Rating(
            id = "someNewId",
            groupId = rating.groupId,
            value = 0.0,
            votes = emptyList()
        )
    }
    execute(completePipeline)
})
