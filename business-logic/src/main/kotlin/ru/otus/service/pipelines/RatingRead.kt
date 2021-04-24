package ru.otus.service.pipelines

import ru.otus.cor.IOperation
import ru.otus.cor.pipeline
import ru.otus.model.Rating
import ru.otus.model.Vote
import ru.otus.model.context.ExchangeContext
import ru.otus.service.utils.completePipeline
import ru.otus.service.utils.idNotBlankValidation
import ru.otus.service.utils.runningPipeline
import java.time.LocalDateTime.now

object RatingRead : IOperation<ExchangeContext> by pipeline({
    execute(runningPipeline)
    validate(idNotBlankValidation)
    execute {
        rating = Rating(
            id = rating.id,
            groupId = "someId",
            value = 5.0,
            votes = listOf(
                Vote(
                    id = rating.id,
                    voterId = "someVoterId",
                    value = 5,
                    voteDateTime = now()
                )
            )
        )
    }
    execute(completePipeline)
})
