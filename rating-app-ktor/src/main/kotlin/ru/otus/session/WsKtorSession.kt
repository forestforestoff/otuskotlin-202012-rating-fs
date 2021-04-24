package ru.otus.session

import io.ktor.http.cio.websocket.*
import ru.otus.model.context.ISessionWrapper

class WsKtorSession(override val specificSession: DefaultWebSocketSession) : ISessionWrapper<DefaultWebSocketSession>
