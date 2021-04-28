package ru.otus.controller

import io.ktor.http.cio.websocket.*
import io.ktor.server.testing.*
import org.junit.Test
import ru.otus.main.module
import ru.otus.model.context.ProjectError
import ru.otus.transport.openapi.models.RatingCreateRequest
import ru.otus.transport.openapi.models.RatingRequest
import ru.otus.transport.openapi.models.RatingResponse
import ru.otus.transport.openapi.models.VoteRequest
import ru.otus.utils.fromJson
import ru.otus.utils.toJson
import kotlin.test.assertEquals

class WebsocketControllerTest {

    @Test
    fun updateVoteTest() {
        withTestApplication({ module(testing = true) }) {
            handleWebSocketConversation("/ws") { incoming, outgoing ->
                val query = VoteRequest(
                    id = "1",
                    ratingId = "123",
                    value = 5,
                    voterId = "777"
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
        }
    }

    @Test
    fun readRatingTest() {
        withTestApplication({ module(testing = true) }) {
            handleWebSocketConversation("/ws") { incoming, outgoing ->
                val query = RatingRequest(id = "1")
                val requestJson = toJson(query)
                outgoing.send(Frame.Text(requestJson))
                val respJson = (incoming.receive() as Frame.Text).readText()
                println("RESPONSE: $respJson")
                val response = fromJson(respJson, RatingResponse::class.java)
                assertEquals(query.id, response.id)
            }
        }
    }

    @Test
    fun createRatingTest() {
        withTestApplication({ module(testing = true) }) {
            handleWebSocketConversation("/ws") { incoming, outgoing ->
                val query = RatingCreateRequest(groupId = "1")
                val requestJson = toJson(query)
                outgoing.send(Frame.Text(requestJson))
                val respJson = (incoming.receive() as Frame.Text).readText()
                println("RESPONSE: $respJson")
                val response = fromJson(respJson, RatingResponse::class.java)
                assertEquals(query.groupId, response.groupId)
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
