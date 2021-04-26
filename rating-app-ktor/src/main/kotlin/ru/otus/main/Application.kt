package ru.otus.main

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.jackson.*
import io.ktor.routing.*
import io.ktor.websocket.*
import ru.otus.controller.ratingRouting
import ru.otus.controller.websocketRouting

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.module(testing: Boolean = false) {
    install(ContentNegotiation) {
        jackson()
    }
    install(WebSockets)
    registerRatingRoutes()
}

fun Application.registerRatingRoutes() {
    routing {
        ratingRouting()
        websocketRouting()
    }
}

val jackson = jacksonObjectMapper().registerKotlinModule()
