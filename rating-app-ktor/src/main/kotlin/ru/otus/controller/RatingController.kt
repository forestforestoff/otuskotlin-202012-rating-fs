package ru.otus.controller

import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.util.pipeline.*
import ru.otus.mappers.toInternal
import ru.otus.mappers.toResponse
import ru.otus.model.PrincipalModel
import ru.otus.model.Rating
import ru.otus.model.UserGroups
import ru.otus.model.Vote
import ru.otus.model.context.ExchangeContext
import ru.otus.service.RatingCrud.create
import ru.otus.service.RatingCrud.delete
import ru.otus.service.RatingCrud.read
import ru.otus.service.RatingCrud.update
import ru.otus.transport.openapi.models.RatingCreateRequest
import ru.otus.transport.openapi.models.RatingRequest
import ru.otus.transport.openapi.models.VoteRequest

fun Routing.ratingRouting(testing: Boolean) {
    authenticate("auth-jwt") {
        route("/rating") {
            post("/create") {
                withRequest<RatingCreateRequest>().run {
                    useAuth = !testing
                    create()
                    respondBy(rating)
                }
            }
            post("/get") {
                withRequest<RatingRequest>().run {
                    useAuth = !testing
                    read()
                    respondBy(rating)
                }
            }
            post("/update") {
                withRequest<VoteRequest>().run {
                    useAuth = !testing
                    update()
                    respondBy(rating)
                }
            }
            post("/delete") {
                withRequest<RatingRequest>().run {
                    useAuth = !testing
                    delete()
                    call.respond(HttpStatusCode.OK, "Rating with votes was deleted")
                }
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
        principalModel = call.principal<JWTPrincipal>()?.toModel() ?: PrincipalModel.NONE
    }

private fun JWTPrincipal.toModel() = PrincipalModel(
    id = payload.getClaim("id")?.asString().orEmpty(),
    fname = payload.getClaim("fname")?.asString().orEmpty(),
    mname = payload.getClaim("mname")?.asString().orEmpty(),
    lname = payload.getClaim("lname")?.asString().orEmpty(),
    groups = payload
        .getClaim("groups")
        ?.asList(String::class.java)
        ?.mapNotNull {
            try {
                UserGroups.valueOf(it)
            } catch (e: Throwable) {
                null
            }
        }.orEmpty()
)
