package ru.otus.service

import ru.otus.mappers.toInternal
import ru.otus.model.Rating
import ru.otus.model.context.ExchangeContext
import ru.otus.service.RatingCrud.create
import ru.otus.service.RatingCrud.read
import ru.otus.service.RatingCrud.update
import ru.otus.transport.openapi.models.RatingCreateRequest
import ru.otus.transport.openapi.models.RatingRequest
import ru.otus.transport.openapi.models.VoteRequest

class RatingService(crud: RatingCrud) {

    suspend fun update(ctx: ExchangeContext, request: VoteRequest): Rating = with(ctx) {
        vote = request.toInternal()
        update()
        rating
    }

    suspend fun create(ctx: ExchangeContext, request: RatingCreateRequest): Rating = with(ctx) {
        rating = request.toInternal()
        create()
        rating
    }

    suspend fun read(ctx: ExchangeContext, request: RatingRequest): Rating = with(ctx) {
        rating = request.toInternal()
        read()
        rating
    }
}
