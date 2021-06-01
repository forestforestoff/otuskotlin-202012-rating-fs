package ru.otus.controller

import io.ktor.http.cio.websocket.*
import io.ktor.server.testing.*
import kotlinx.coroutines.runBlocking
import ru.otus.main.module
import ru.otus.model.Rating
import ru.otus.model.context.ExchangeContext
import ru.otus.model.context.ProjectError
import ru.otus.service.RatingCrud.create
import ru.otus.service.RatingCrud.delete
import ru.otus.transport.openapi.models.RatingCreateRequest
import ru.otus.transport.openapi.models.RatingRequest
import ru.otus.transport.openapi.models.RatingResponse
import ru.otus.transport.openapi.models.VoteRequest
import ru.otus.utils.fromJson
import ru.otus.utils.toJson
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals

class WebsocketControllerTest {

    private fun createNewRating(): String {
        ExchangeContext(useAuth = false).run {
            runBlocking { create() }
            return rating.id
        }
    }

    private fun removeRating(ratingId: String) {
        runBlocking { ExchangeContext(rating = Rating(id = ratingId), useAuth = false).delete() }
    }

    @Test
    fun updateVoteTest() {
        withTestApplication({ module(testing = true) }) {
            val newRatingId = createNewRating()
            handleWebSocketConversation("/ws") { incoming, outgoing ->
                val query = VoteRequest(
                    id = UUID.randomUUID().toString(),
                    ratingId = newRatingId,
                    value = 5,
                    voterId = UUID.randomUUID().toString()
                )
                val requestJson = toJson(query)
                outgoing.send(Frame.Text(requestJson))
                val respJson = (incoming.receive() as Frame.Text).readText()
                println("RESPONSE: $respJson")
                val response = fromJson(respJson, RatingResponse::class.java)
                assertEquals(query.ratingId, response.id)
                assertEquals(query.value?.toDouble(), response.value)
                assertEquals(response.votes?.size, 1)
            }
            removeRating(newRatingId)
        }
    }

    @Test
    fun readRatingTest() {
        withTestApplication({ module(testing = true) }) {
            val newRatingId = createNewRating()
            handleWebSocketConversation("/ws") { incoming, outgoing ->
                val query = RatingRequest(id = newRatingId)
                val requestJson = toJson(query)
                outgoing.send(Frame.Text(requestJson))
                val respJson = (incoming.receive() as Frame.Text).readText()
                println("RESPONSE: $respJson")
                val response = fromJson(respJson, RatingResponse::class.java)
                assertEquals(query.id, response.id)
            }
            removeRating(newRatingId)
        }
    }

    @Test
    fun createRatingTest() {
        withTestApplication({ module(testing = true) }) {
            handleWebSocketConversation("/ws") { incoming, outgoing ->
                val query = RatingCreateRequest(groupId = UUID.randomUUID().toString())
                val requestJson = toJson(query)
                outgoing.send(Frame.Text(requestJson))
                val respJson = (incoming.receive() as Frame.Text).readText()
                println("RESPONSE: $respJson")
                val response = fromJson(respJson, RatingResponse::class.java)
                assertEquals(query.groupId, response.groupId)
                removeRating(response.id!!)
            }
        }
    }

    @Test
    fun someErrorTest() {
        withTestApplication({ module(testing = true) }) {
            handleWebSocketConversation("/ws") { incoming, outgoing ->
                val requestJson = """{"type":"VoteRequest"}"""
                outgoing.send(Frame.Text(requestJson))
                val respJson = (incoming.receive() as Frame.Text).readText()
                println("RESPONSE: $respJson")
                val response = fromJson(respJson, ProjectError::class.java)
                assertEquals(response.message, "Invalid vote request update")
            }
        }
    }
}
