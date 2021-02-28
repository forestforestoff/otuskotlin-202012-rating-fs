package ru.otus.transport.openapi

import ru.otus.transport.openapi.infrastructure.Serializer.moshi
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
        val jsonString = moshi.adapter(RatingRequest::class.java).toJson(dto)
        assertTrue("Json does not contain ID") { jsonString.contains(id) }
        assertEquals(dto, moshi.adapter(RatingRequest::class.java).fromJson(jsonString))
    }

    @Test
    fun ratingCreateRequestTest() {
        val groupId = randomId
        val dto = RatingCreateRequest(groupId)
        val jsonString = moshi.adapter(RatingCreateRequest::class.java).toJson(dto)
        assertTrue("Json does not contain ID") { jsonString.contains(groupId) }
        assertEquals(dto, moshi.adapter(RatingCreateRequest::class.java).fromJson(jsonString))
    }

    @Test
    fun ratingUpdateRequestTest() {
        val id = randomId
        val groupId = randomId
        val value = 5
        val voterId = randomId
        val dto = VoteRequest(id, groupId, value, voterId)
        val jsonString = moshi.adapter(VoteRequest::class.java).toJson(dto)
        assertTrue("Json does not contain all fields") {
            jsonString.contains(groupId) && jsonString.contains(value.toString()) && jsonString.contains(voterId)
        }
        assertEquals(dto, moshi.adapter(VoteRequest::class.java).fromJson(jsonString))
    }

    @Test
    fun ratingResponseTest() {
        val id = randomId
        val groupId = randomId
        val value = 4.9
        val voterId = randomId
        val voteDate = LocalDateTime.now().toString()
        val voters = mapOf(voterId to voteDate)
        val dto = RatingResponse(id, groupId, value, voters)
        val jsonString = moshi.adapter(RatingResponse::class.java).toJson(dto)
        assertTrue("Json does not contain all fields") {
            jsonString.contains(groupId) && jsonString.contains(value.toString()) && jsonString.contains(voterId)
        }
    }
}
