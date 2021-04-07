package ru.otus.service.pipelines

import ru.otus.cor.IOperation
import ru.otus.cor.pipeline
import ru.otus.model.Rating
import ru.otus.model.context.ExchangeContext
import ru.otus.service.utils.completePipeline
import ru.otus.service.utils.initPipeline
import ru.otus.service.utils.voteUpdateValidation

object RatingUpdate : IOperation<ExchangeContext> by pipeline({
    execute(initPipeline)
    validate(voteUpdateValidation)
    execute {
        rating = Rating(
            id = vote.id,
            groupId = "someGroupId",
            value = vote.value.toDouble(),
            votes = listOf(vote)
        )
    }
    execute(completePipeline)
})
