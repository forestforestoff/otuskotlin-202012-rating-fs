package ru.otus.controller

import io.ktor.http.cio.websocket.*
import io.ktor.routing.*
import io.ktor.websocket.*
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import ru.otus.mappers.toResponse
import ru.otus.model.context.ContextStatus.*
import ru.otus.model.context.EmptySession
import ru.otus.model.context.ExchangeContext
import ru.otus.model.context.ProjectError
import ru.otus.service.RatingService
import ru.otus.session.WsKtorSession
import ru.otus.transport.openapi.infrastructure.Serializer.gson
import ru.otus.transport.openapi.models.VoteRequest
import java.util.concurrent.ConcurrentHashMap

private val sessions = ConcurrentHashMap<DefaultWebSocketSession, WsKtorSession>()

fun Routing.websocketRouting(ratingService: RatingService) {
    webSocket("/ws") {
        sessions[this] = WsKtorSession(this)
        val ctx = ExchangeContext(userSession = sessions[this] ?: EmptySession(), status = INIT)
        for (frame in incoming) {
            if (frame is Frame.Text) {
                try {
                    ctx.status = RUNNING
                    val voteRequest = gson.fromJson(frame.readText(), VoteRequest::class.java)
                    val updatedRating = ratingService.update(ctx, voteRequest)
                    outgoing.send(Frame.Text(gson.toJson(updatedRating.toResponse())))
                } catch (e: ClosedReceiveChannelException) {
                    ctx.status = NONE
                    sessions -= this
                } catch (e: Throwable) {
                    e.printStackTrace()
                    val projectError = ProjectError(e.localizedMessage)
                    ctx.errors.add(projectError)
                    ctx.status = ERROR
                    outgoing.send(Frame.Text(gson.toJson(projectError)))
                }
            }
        }
    }
}
