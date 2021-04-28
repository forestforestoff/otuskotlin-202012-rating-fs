package ru.otus.transport.openapi

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import ru.otus.transport.openapi.models.*
import java.time.LocalDateTime
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SerializationTest {

    private val jackson = jacksonObjectMapper()

    private val randomId: String
        get() = UUID.randomUUID().toString()

    @Test
    fun ratingRequestTest() {
        val id = randomId
        val dto = RatingRequest(id = id)
        val jsonString = jackson.writeValueAsString(dto)
        assertTrue("Json does not contain type") {
            jsonString.contains(Regex(""""type":"${dto::class.java.simpleName}""""))
        }
        assertTrue("Json does not contain ID") {
            jsonString.contains(Regex(""""id":"$id""""))
        }
        assertEquals(dto, jackson.readValue(jsonString, BaseRequest::class.java))
    }

    @Test
    fun ratingCreateRequestTest() {
        val groupId = randomId
        val dto = RatingCreateRequest(groupId = groupId)
        val jsonString = jackson.writeValueAsString(dto)
        assertTrue("Json does not contain group ID") {
            jsonString.contains(Regex(""""groupId":"$groupId""""))
        }
        assertTrue("Json does not contain type") {
            jsonString.contains(Regex(""""type":"${dto::class.java.simpleName}""""))
        }
        assertEquals(dto, jackson.readValue(jsonString, BaseRequest::class.java))
    }

    @Test
    fun ratingUpdateRequestTest() {
        val id = randomId
        val value = 5
        val voterId = randomId
        val dto = VoteRequest(id = id, value = value, voterId = voterId)
        val jsonString = jackson.writeValueAsString(dto)
        assertTrue("Json does not contain type") {
            jsonString.contains(Regex(""""type":"${dto::class.java.simpleName}""""))
        }
        assertTrue("Json does not contain necessary fields") {
            jsonString.contains(Regex(""""id":"$id","ratingId":null,"value":$value,"voterId":"$voterId""""))
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
        assertTrue { jsonString.contains(Regex(""""id":"$id","groupId":"$groupId","value":$value""")) }
        assertTrue {
            jsonString.contains(
                Regex(""""id":"$id","ratingId":null,"value":${value.toInt()},"voterId":"$voterId","voteDateTime":"$voteTime"""")
            )
        }
    }

    @Test
    fun voteRequestTest() {
        val id = randomId
        val value = 4
        val voterId = randomId
        val dto = VoteRequest(voterId = voterId, id = id, value = value)
        val jsonString = jackson.writeValueAsString(dto)
        assertTrue {
            jsonString.contains(
                Regex(""""id":"$id","ratingId":null,"value":$value,"voterId":"$voterId"""")
            )
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
        assertTrue {
            jsonString.contains(
                Regex(""""id":"$id","ratingId":null,"value":$value,"voterId":"$voterId","voteDateTime":"$voteTime"""")
            )
        }
    }
}
