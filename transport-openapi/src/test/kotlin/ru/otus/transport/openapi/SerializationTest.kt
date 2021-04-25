package ru.otus.transport.openapi

import ru.otus.transport.openapi.infrastructure.Serializer.gson
import ru.otus.transport.openapi.models.*
import java.time.LocalDateTime
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SerializationTest {

    private val randomId: String
        get() = UUID.randomUUID().toString()

    @Test
    fun ratingRequestTest() {
        val id = randomId
        val dto = RatingRequest(id)
        val jsonString = gson.toJson(dto)
        assertTrue("Json does not contain ID") { jsonString.contains(id) }
        assertEquals(dto, gson.fromJson(jsonString, RatingRequest::class.java))
    }

    @Test
    fun ratingCreateRequestTest() {
        val groupId = randomId
        val dto = RatingCreateRequest(groupId)
        val jsonString = gson.toJson(dto)
        assertTrue("Json does not contain ID") { jsonString.contains(groupId) }
        assertEquals(dto, gson.fromJson(jsonString, RatingCreateRequest::class.java))
    }

    @Test
    fun ratingUpdateRequestTest() {
        val id = randomId
        val value = 5
        val voterId = randomId
        val dto = VoteRequest(id, value, voterId)
        val jsonString = gson.toJson(dto)
        assertTrue("Json does not contain all fields") {
            jsonString.contains(value.toString()) && jsonString.contains(voterId)
        }
        assertEquals(dto, gson.fromJson(jsonString, VoteRequest::class.java))
    }

    @Test
    fun ratingResponseTest() {
        val id = randomId
        val groupId = randomId
        val value = 4.0
        val voterId = randomId
        val voteTime = LocalDateTime.now().toString()
        val voteResponse = VoteResponse(
            voterId = voterId, id = id, value = value.toInt(), voteDateTime = voteTime
        )
        val dto = RatingResponse(id, groupId, value, listOf(voteResponse))
        val jsonString = gson.toJson(dto)
        assertTrue("Json does not contain all fields") {
            jsonString.contains(id) && jsonString.contains(groupId) && jsonString.contains(value.toString())
                    && jsonString.contains(voterId) && jsonString.contains(value.toInt().toString())
                    && jsonString.contains(voteTime)
        }
    }

    @Test
    fun voteRequestTest() {
        val id = randomId
        val value = 4
        val voterId = randomId
        val dto = VoteRequest(voterId = voterId, id = id, value = value)
        val jsonString = gson.toJson(dto)
        assertTrue("Json does not contain all fields") {
            jsonString.contains(id) && jsonString.contains(value.toString()) && jsonString.contains(voterId)
        }
    }

    @Test
    fun voteResponseTest() {
        val id = randomId
        val value = 4
        val voterId = randomId
        val voteTime = LocalDateTime.now().toString()
        val dto = VoteResponse(voterId = voterId, id = id, value = value, voteDateTime = voteTime)
        val jsonString = gson.toJson(dto)
        assertTrue("Json does not contain all fields") {
            jsonString.contains(id) && jsonString.contains(value.toString()) && jsonString.contains(voterId)
                    && jsonString.contains(voteTime)
        }
    }
}
