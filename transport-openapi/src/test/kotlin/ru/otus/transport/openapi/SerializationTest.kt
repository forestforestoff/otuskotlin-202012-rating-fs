package ru.otus.transport.openapi

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import ru.otus.transport.openapi.models.*
import java.time.LocalDateTime
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SerializationTest {

    private val jackson = jacksonObjectMapper().registerKotlinModule()

    private val randomId: String
        get() = UUID.randomUUID().toString()

    @Test
    fun ratingRequestTest() {
        val id = randomId
        val dto = RatingRequest(id)
        val jsonString = jackson.writeValueAsString(dto)
        assertTrue("Json does not contain ID") { jsonString.contains(id) }
        assertTrue("Json does not contain type") { jsonString.contains(dto::class.java.simpleName) }
        assertEquals(dto, jackson.readValue(jsonString, BaseRequest::class.java))
    }

    @Test
    fun ratingCreateRequestTest() {
        val groupId = randomId
        val dto = RatingCreateRequest(groupId)
        val jsonString = jackson.writeValueAsString(dto)
        assertTrue("Json does not contain ID") { jsonString.contains(groupId) }
        assertTrue("Json does not contain type") { jsonString.contains(dto::class.java.simpleName) }
        assertEquals(dto, jackson.readValue(jsonString, BaseRequest::class.java))
    }

    @Test
    fun ratingUpdateRequestTest() {
        val id = randomId
        val value = 5
        val voterId = randomId
        val dto = VoteRequest(id = id, value = value, voterId = voterId)
        val jsonString = jackson.writeValueAsString(dto)
        assertTrue("Json does not contain all fields") {
            jsonString.contains(value.toString()) && jsonString.contains(voterId)
                    && jsonString.contains(dto::class.java.simpleName)
        }
        assertEquals(dto, jackson.readValue(jsonString, BaseRequest::class.java))
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
        val jsonString = jackson.writeValueAsString(dto)
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
        val jsonString = jackson.writeValueAsString(dto)
        println(jsonString)
        assertTrue("Json does not contain all fields") {
            jsonString.contains(id) && jsonString.contains(value.toString()) && jsonString.contains(voterId)
                    && jsonString.contains(dto::class.java.simpleName)
        }
    }

    @Test
    fun voteResponseTest() {
        val id = randomId
        val value = 4
        val voterId = randomId
        val voteTime = LocalDateTime.now().toString()
        val dto = VoteResponse(voterId = voterId, id = id, value = value, voteDateTime = voteTime)
        val jsonString = jackson.writeValueAsString(dto)
        assertTrue("Json does not contain all fields") {
            jsonString.contains(id) && jsonString.contains(value.toString()) && jsonString.contains(voterId)
                    && jsonString.contains(voteTime)
        }
    }
}
