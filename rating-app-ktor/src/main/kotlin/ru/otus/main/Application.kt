package ru.otus.main

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.routing.*
import ru.otus.controller.ratingRouting

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.module(testing: Boolean = false) {
    install(ContentNegotiation) {
        gson {
            setPrettyPrinting()
            disableHtmlEscaping()
        }
    }
    registerRatingRoutes()
}

fun Application.registerRatingRoutes() {
    routing {
        ratingRouting()
    }
}
