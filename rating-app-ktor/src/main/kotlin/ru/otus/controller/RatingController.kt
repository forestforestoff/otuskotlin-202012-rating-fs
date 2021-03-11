package ru.otus.controller

import io.ktor.application.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import ru.otus.mappers.formRatingResponse
import ru.otus.mappers.withRequest
import ru.otus.model.context.Context
import ru.otus.service.RatingService.createRating
import ru.otus.service.RatingService.getRating
import ru.otus.service.RatingService.updateRating
import ru.otus.transport.openapi.models.RatingCreateRequest
import ru.otus.transport.openapi.models.RatingRequest
import ru.otus.transport.openapi.models.VoteRequest

fun Routing.ratingRouting() {
    route("/rating") {
        post("/create") {
            Context().withRequest(call.receive<RatingCreateRequest>()).run {
                createRating()
                call.respond(formRatingResponse())
            }
        }
        post("/get") {
            Context().withRequest(call.receive<RatingRequest>()).run {
                getRating()
                call.respond(formRatingResponse())
            }
        }
        post("/update") {
            Context().withRequest(call.receive<VoteRequest>()).run {
                updateRating()
                call.respond(formRatingResponse())
            }
        }
    }
}
