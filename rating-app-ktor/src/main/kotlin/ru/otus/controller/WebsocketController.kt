package ru.otus.controller

import io.ktor.http.cio.websocket.*
import io.ktor.routing.*
import io.ktor.websocket.*
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import ru.otus.mappers.toInternal
import ru.otus.mappers.toResponse
import ru.otus.model.context.ContextStatus.*
import ru.otus.model.context.EmptySession
import ru.otus.model.context.ExchangeContext
import ru.otus.model.context.ProjectError
import ru.otus.service.RatingCrud.create
import ru.otus.service.RatingCrud.read
import ru.otus.service.RatingCrud.update
import ru.otus.session.WsKtorSession
import ru.otus.transport.openapi.models.*
import ru.otus.utils.fromJson
import ru.otus.utils.toJson
import java.util.concurrent.ConcurrentHashMap

private val sessions = ConcurrentHashMap<DefaultWebSocketSession, WsKtorSession>()

fun Routing.websocketRouting(testing: Boolean) {
    webSocket("/ws") {
        sessions[this] = WsKtorSession(this)
        val ctx = ExchangeContext(userSession = sessions[this] ?: EmptySession(), status = INIT, useAuth = !testing)
        for (frame in incoming) {
            if (frame is Frame.Text) {
                try {
                    ctx.status = RUNNING
                    val request = fromJson(frame.readText(), BaseRequest::class.java)
                    outgoing.send(Frame.Text(toJson(ctx.handleRequest(request))))
                } catch (e: ClosedReceiveChannelException) {
                    ctx.status = NONE
                    sessions -= this
                } catch (e: Throwable) {
                    e.printStackTrace()
                    val projectError = ProjectError(e.localizedMessage)
                    ctx.errors.add(projectError)
                    ctx.status = ERROR
                    outgoing.send(Frame.Text(toJson(projectError)))
                }
            }
        }
    }
}

private suspend fun ExchangeContext.handleRequest(request: BaseRequest): RatingResponse {
    when (request) {
        is VoteRequest -> {
            vote = request.toInternal()
            update()
        }
        is RatingRequest -> {
            rating = request.toInternal()
            read()
        }
        is RatingCreateRequest -> {
            rating = request.toInternal()
            create()
        }
    }
    return rating.toResponse()
}
