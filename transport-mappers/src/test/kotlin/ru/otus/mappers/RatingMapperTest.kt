package ru.otus.mappers

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import ru.otus.model.Rating
import ru.otus.model.Vote
import ru.otus.transport.openapi.models.RatingCreateRequest
import ru.otus.transport.openapi.models.RatingRequest
import ru.otus.transport.openapi.models.VoteRequest
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

class RatingMapperTest : FunSpec() {

    init {
        test("vote to response") {
            val id = randomId
            val value = 4
            val voterId = randomId
            val voteTime = LocalDateTime.now()
            val vote = Vote(id = id, voterId = voterId, value = value, voteDateTime = voteTime)
            vote.toResponse().should { response ->
                response.id shouldBe id
                response.voterId shouldBe voterId
                response.value shouldBe value
                response.voteDateTime shouldBe voteTime.toString()
            }
        }
        test("vote request to internal") {
            val id = randomId
            val value = 4
            val voterId = randomId
            val voteRequest = VoteRequest(id = id, voterId = voterId, value = value)
            voteRequest.toInternal().should {
                it.id shouldBe id
                it.voterId shouldBe voterId
                it.value shouldBe value
                it.voteDateTime.toLocalDate() shouldBe LocalDate.now()
            }
        }
        test("rating to response") {
            val id = randomId
            val groupId = randomId
            val value = 4.9
            val voterId = randomId
            val voteTime = LocalDateTime.now()
            val voters = listOf(
                Vote(voterId = voterId, id = id, value = value.toInt(), voteDateTime = voteTime)
            )
            val rating = Rating(id, groupId, voters, value)
            rating.toResponse().should { response ->
                response.id shouldBe id
                response.groupId shouldBe groupId
                response.value shouldBe value
                response.votes shouldBe voters.map { it.toResponse() }
            }
        }
        test("rating request to internal model") {
            val id = randomId
            val ratingRequest = RatingRequest(id)
            ratingRequest.toInternal().should {
                it.id shouldBe id
            }
        }
        test("rating create request to internal model") {
            val groupId = randomId
            val ratingCreateRequest = RatingCreateRequest(groupId)
            ratingCreateRequest.toInternal().should {
                it.groupId shouldBe groupId
            }
        }
        test("vote request to internal model") {
            val id = randomId
            val value = 5
            val voterId = randomId
            val voteRequest = VoteRequest(id, value, voterId)
            voteRequest.toInternal().should {
                it.id shouldBe id
                it.value shouldBe value
                it.voterId shouldBe voterId
            }
        }
    }

    private val randomId: String
        get() = UUID.randomUUID().toString()
}
