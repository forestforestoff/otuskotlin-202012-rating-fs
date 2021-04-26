package ru.otus.controller

import io.ktor.http.*
import io.ktor.http.ContentType.Application.Json
import io.ktor.server.testing.*
import ru.otus.main.module
import ru.otus.transport.openapi.models.RatingCreateRequest
import ru.otus.transport.openapi.models.RatingRequest
import ru.otus.transport.openapi.models.RatingResponse
import ru.otus.transport.openapi.models.VoteRequest
import ru.otus.utils.fromJson
import ru.otus.utils.toJson
import kotlin.test.Test
import kotlin.test.assertEquals

class RatingControllerTest {

    @Test
    fun testCreateRating() {
        withTestApplication({ module(testing = true) }) {
            handleRequest(HttpMethod.Post, "/rating/create") {
                val request = RatingCreateRequest(groupId = "123")
                setBody(toJson(request))
                addHeader("Content-Type", "application/json")
            }.apply {
                println(response.content)
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals(Json.withCharset(Charsets.UTF_8), response.contentType())
                val ratingResponseFromJson = fromJson(response.content.orEmpty(), RatingResponse::class.java)
                assertEquals("123", ratingResponseFromJson.groupId, "Wrong groupId!")
                assertEquals(0.0, ratingResponseFromJson.value, "Wrong value!")
                assertEquals(emptyList(), ratingResponseFromJson.votes, "Wrong votes!")
            }
        }
    }

    @Test
    fun testGetRating() {
        withTestApplication({ module(testing = true) }) {
            handleRequest(HttpMethod.Post, "/rating/get") {
                val request = RatingRequest(id = "123")
                setBody(toJson(request))
                addHeader("Content-Type", "application/json")
            }.apply {
                println(response.content)
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals(Json.withCharset(Charsets.UTF_8), response.contentType())
                val ratingResponseFromJson = fromJson(response.content.orEmpty(), RatingResponse::class.java)
                assertEquals("123", ratingResponseFromJson.id, "Wrong ID!")
            }
        }
    }

    @Test
    fun testUpdateRating() {
        withTestApplication({ module(testing = true) }) {
            handleRequest(HttpMethod.Post, "/rating/update") {
                val request = VoteRequest(id = "123", value = 5, voterId = "777")
                setBody(toJson(request))
                addHeader("Content-Type", "application/json")
            }.apply {
                println(response.content)
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals(Json.withCharset(Charsets.UTF_8), response.contentType())
                val ratingResponseFromJson = fromJson(response.content.orEmpty(), RatingResponse::class.java)
                assertEquals("123", ratingResponseFromJson.id, "Wrong ID!")
                assertEquals(5.0, ratingResponseFromJson.value, "Wrong value!")
                val voteResponse = ratingResponseFromJson.votes!!.first()
                assertEquals("123", voteResponse.id, "Wrong vote ID!")
                assertEquals(5, voteResponse.value, "Wrong vote value!")
                assertEquals("777", voteResponse.voterId, "Wrong voter ID!")
            }
        }
    }
}
