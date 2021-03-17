package ru.otus.controller

import io.ktor.routing.*
import ru.otus.controller.context.Context
import ru.otus.model.Rating
import ru.otus.service.RatingService.createRating
import ru.otus.service.RatingService.getRating
import ru.otus.service.RatingService.updateRating
import ru.otus.transport.openapi.models.RatingCreateRequest
import ru.otus.transport.openapi.models.RatingRequest
import ru.otus.transport.openapi.models.VoteRequest

fun Routing.ratingRouting() {
    route("/rating") {
        post("/create") {
            Context().run {
                withRequest<RatingCreateRequest>()
                createRating()
                respond<Rating>()
            }
        }
        post("/get") {
            Context().run {
                withRequest<RatingRequest>()
                getRating()
                respond<Rating>()
            }
        }
        post("/update") {
            Context().run {
                withRequest<VoteRequest>()
                updateRating()
                respond<Rating>()
            }
        }
    }
}
