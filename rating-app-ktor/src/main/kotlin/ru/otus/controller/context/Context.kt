package ru.otus.controller.context

import io.ktor.application.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.util.pipeline.*
import ru.otus.mappers.toInternal
import ru.otus.mappers.toResponse
import ru.otus.model.Rating
import ru.otus.model.Vote
import ru.otus.transport.openapi.models.RatingCreateRequest
import ru.otus.transport.openapi.models.RatingRequest
import ru.otus.transport.openapi.models.VoteRequest

data class Context(
    var rating: Rating = Rating.NONE,
    var vote: Vote = Vote.NONE
) {

    suspend inline fun <reified T : Any> PipelineContext<*, ApplicationCall>.respond() {
        when (T::class) {
            Rating::class -> call.respond(rating.toResponse())
            Vote::class -> call.respond(vote.toResponse())
        }
    }

    suspend inline fun <reified T : Any> PipelineContext<*, ApplicationCall>.withRequest() {
        when (T::class) {
            VoteRequest::class -> vote = call.receive<VoteRequest>().toInternal()
            RatingRequest::class -> rating = call.receive<RatingRequest>().toInternal()
            RatingCreateRequest::class -> rating = call.receive<RatingCreateRequest>().toInternal()
        }
    }
}
