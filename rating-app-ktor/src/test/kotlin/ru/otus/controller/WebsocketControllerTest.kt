package ru.otus.controller

import io.ktor.http.cio.websocket.*
import io.ktor.server.testing.*
import org.junit.Test
import ru.otus.main.module
import ru.otus.model.context.ProjectError
import ru.otus.transport.openapi.infrastructure.Serializer.gson
import ru.otus.transport.openapi.models.RatingResponse
import ru.otus.transport.openapi.models.VoteRequest
import kotlin.test.assertEquals

class WebsocketControllerTest {

    @Test
    fun updateVoteTest() {
        withTestApplication({ module(testing = true) }) {
            handleWebSocketConversation("/ws") { incoming, outgoing ->
                val query = VoteRequest(
                    id = "1",
                    value = 5,
                    voterId = "777"
                )
                val requestJson = gson.toJson(query)
                outgoing.send(Frame.Text(requestJson))
                val respJson = (incoming.receive() as Frame.Text).readText()
                println("RESPONSE: $respJson")
                val response = gson.fromJson(respJson, RatingResponse::class.java)
                assertEquals(query.id, response.id)
                assertEquals(query.value?.toDouble(), response.value)
                assertEquals(response.votes?.size, 1)
            }
        }
    }

    @Test
    fun updateVoteErrorTest() {
        withTestApplication({ module(testing = true) }) {
            handleWebSocketConversation("/ws") { incoming, outgoing ->
                val requestJson = """{"error":"unknown"}"""
                outgoing.send(Frame.Text(requestJson))
                val respJson = (incoming.receive() as Frame.Text).readText()
                println("RESPONSE: $respJson")
                val response = gson.fromJson(respJson, ProjectError::class.java)
                assertEquals(response.message, "Invalid vote request update")
            }
        }
    }
}
