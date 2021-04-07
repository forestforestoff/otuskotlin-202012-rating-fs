package ru.otus.controller

import io.ktor.application.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.util.pipeline.*
import ru.otus.mappers.toInternal
import ru.otus.mappers.toResponse
import ru.otus.model.Rating
import ru.otus.model.Vote
import ru.otus.model.context.ExchangeContext
import ru.otus.service.RatingCrud.create
import ru.otus.service.RatingCrud.read
import ru.otus.service.RatingCrud.update
import ru.otus.transport.openapi.models.RatingCreateRequest
import ru.otus.transport.openapi.models.RatingRequest
import ru.otus.transport.openapi.models.VoteRequest

fun Routing.ratingRouting() {
    route("/rating") {
        post("/create") {
            withRequest<RatingCreateRequest>().run {
                create()
                respondBy(rating)
            }
        }
        post("/get") {
            withRequest<RatingRequest>().run {
                read()
                respondBy(rating)
            }
        }
        post("/update") {
            withRequest<VoteRequest>().run {
                update()
                respondBy(rating)
            }
        }
    }
}

private suspend inline fun <reified T : Any> PipelineContext<*, ApplicationCall>.respondBy(internalModel: T) {
    when (internalModel) {
        is Rating -> call.respond(internalModel.toResponse())
        is Vote -> call.respond(internalModel.toResponse())
    }
}

private suspend inline fun <reified T : Any> PipelineContext<*, ApplicationCall>.withRequest(): ExchangeContext =
    ExchangeContext().apply {
        when (T::class) {
            VoteRequest::class -> vote = call.receive<VoteRequest>().toInternal()
            RatingRequest::class -> rating = call.receive<RatingRequest>().toInternal()
            RatingCreateRequest::class -> rating = call.receive<RatingCreateRequest>().toInternal()
        }
    }
