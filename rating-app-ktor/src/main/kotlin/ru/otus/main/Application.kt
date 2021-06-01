package ru.otus.main

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.jackson.*
import io.ktor.routing.*
import io.ktor.websocket.*
import ru.otus.authentication.authFeature
import ru.otus.controller.ratingRouting
import ru.otus.controller.websocketRouting

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.module(testing: Boolean = false) {
    install(ContentNegotiation) {
        jackson()
    }
    authFeature(testing)
    install(WebSockets)
    registerRatingRoutes(testing)
}

fun Application.registerRatingRoutes(testing: Boolean) {
    routing {
        ratingRouting(testing)
        websocketRouting(testing)
    }
}
