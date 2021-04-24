package ru.otus.service

import ru.otus.mappers.toInternal
import ru.otus.model.Rating
import ru.otus.model.context.ExchangeContext
import ru.otus.service.RatingCrud.update
import ru.otus.transport.openapi.models.VoteRequest

class RatingService(crud: RatingCrud) {

    suspend fun update(ctx: ExchangeContext, request: VoteRequest): Rating = with(ctx) {
        vote = request.toInternal()
        update()
        rating
    }
}
