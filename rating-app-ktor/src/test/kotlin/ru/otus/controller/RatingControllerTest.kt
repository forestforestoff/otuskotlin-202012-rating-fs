package ru.otus.controller

import io.ktor.http.*
import io.ktor.http.ContentType.Application.Json
import io.ktor.server.testing.*
import kotlinx.coroutines.runBlocking
import ru.otus.main.module
import ru.otus.model.Rating
import ru.otus.model.context.ExchangeContext
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

class RatingControllerTest {

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
    fun testCreateRating() {
        withTestApplication({ module(testing = true) }) {
            val someGroupId = UUID.randomUUID().toString()
            handleRequest(HttpMethod.Post, "/rating/create") {
                val request = RatingCreateRequest(groupId = someGroupId)
                setBody(toJson(request))
                addHeader("Content-Type", "application/json")
            }.apply {
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals(Json.withCharset(Charsets.UTF_8), response.contentType())
                val ratingResponseFromJson = fromJson(response.content.orEmpty(), RatingResponse::class.java)
                assertEquals(someGroupId, ratingResponseFromJson.groupId, "Wrong groupId!")
                assertEquals(0.0, ratingResponseFromJson.value, "Wrong value!")
                assertEquals(emptyList(), ratingResponseFromJson.votes, "Wrong votes!")
                removeRating(ratingResponseFromJson.id.orEmpty())
            }
        }
    }

    @Test
    fun testGetRating() {
        withTestApplication({ module(testing = true) }) {
            val newRatingId = createNewRating()
            handleRequest(HttpMethod.Post, "/rating/get") {
                val request = RatingRequest(id = newRatingId)
                setBody(toJson(request))
                addHeader("Content-Type", "application/json")
            }.apply {
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals(Json.withCharset(Charsets.UTF_8), response.contentType())
                val ratingResponseFromJson = fromJson(response.content.orEmpty(), RatingResponse::class.java)
                assertEquals(newRatingId, ratingResponseFromJson.id, "Wrong ID!")
            }
            removeRating(newRatingId)
        }
    }

    @Test
    fun testUpdateRating() {
        withTestApplication({ module(testing = true) }) {
            val newRatingId = createNewRating()
            val someVoterId = UUID.randomUUID().toString()
            handleRequest(HttpMethod.Post, "/rating/update") {
                val request = VoteRequest(id = "1", ratingId = newRatingId, value = 5, voterId = someVoterId)
                setBody(toJson(request))
                addHeader("Content-Type", "application/json")
            }.apply {
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals(Json.withCharset(Charsets.UTF_8), response.contentType())
                val ratingResponseFromJson = fromJson(response.content.orEmpty(), RatingResponse::class.java)
                assertEquals(newRatingId, ratingResponseFromJson.id, "Wrong ID!")
                assertEquals(5.0, ratingResponseFromJson.value, "Wrong value!")
                val voteResponse = ratingResponseFromJson.votes!!.first()
                assertEquals(newRatingId, voteResponse.ratingId, "Wrong vote ID!")
                assertEquals(5, voteResponse.value, "Wrong vote value!")
                assertEquals(someVoterId, voteResponse.voterId, "Wrong voter ID!")
            }
            removeRating(newRatingId)
        }
    }
}
