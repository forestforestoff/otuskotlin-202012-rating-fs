package ru.otus.mappers

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import ru.otus.model.Rating
import ru.otus.transport.openapi.models.RatingCreateRequest
import ru.otus.transport.openapi.models.RatingRequest
import ru.otus.transport.openapi.models.VoteRequest
import java.time.LocalDateTime
import java.util.*

class RatingMapperTest : FunSpec() {

    init {
        test("rating to response") {
            val id = randomId
            val groupId = randomId
            val value = 4.9
            val voterId = randomId
            val voteTime = LocalDateTime.now()
            val voters = mapOf(voterId to voteTime)
            val rating = Rating(id, groupId, value, voters)
            rating.toResponse().should { response ->
                response.id shouldBe id
                response.groupId shouldBe groupId
                response.value shouldBe value
                response.voterIdAndTime shouldBe voters.mapValues { it.value.toString() }
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
            val groupId = randomId
            val value = 5
            val voterId = randomId
            val voteRequest = VoteRequest(id, groupId, value, voterId)
            voteRequest.toInternal().should {
                it.id shouldBe id
                it.groupId shouldBe groupId
                it.value shouldBe value
                it.voterId shouldBe voterId
            }
        }
    }

    private val randomId: String
        get() = UUID.randomUUID().toString()
}
