package ru.otus.mappers

import io.kotest.core.spec.style.FunSpec
import io.kotest.core.test.TestCase
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import ru.otus.model.Rating
import ru.otus.model.Vote
import ru.otus.model.context.Context
import ru.otus.transport.openapi.models.RatingCreateRequest
import ru.otus.transport.openapi.models.RatingRequest
import ru.otus.transport.openapi.models.VoteRequest
import java.time.LocalDate
import java.util.*

class RatingMapperTest : FunSpec() {

    private lateinit var context: Context

    override fun beforeTest(testCase: TestCase) {
        context = Context()
    }

    private val randomId: String
        get() = UUID.randomUUID().toString()

    init {
        test("vote request to internal and response") {
            val id = randomId
            val value = 4
            val voterId = randomId
            val voteRequest = VoteRequest(id = id, voterId = voterId, value = value)
            context.withRequest(voteRequest).vote
            context.vote.should {
                it.id shouldBe id
                it.voterId shouldBe voterId
                it.value shouldBe value
                it.voteDateTime.toLocalDate() shouldBe LocalDate.now()
            }
            context.rating shouldBe Rating.NONE
            context.formVoteResponse().should {
                it.id shouldBe id
                it.voterId shouldBe voterId
                it.value shouldBe value
                it.voteDateTime.shouldContain(LocalDate.now().toString())
            }
            context.rating shouldBe Rating.NONE
        }
        test("rating request to internal model") {
            val id = randomId
            val ratingRequest = RatingRequest(id)
            context.withRequest(ratingRequest)
            context.rating.should {
                it.id shouldBe id
                it.groupId shouldBe ""
                it.value shouldBe 0.0
                it.votes shouldBe emptyList()
            }
            context.vote shouldBe Vote.NONE
            context.formRatingResponse().should {
                it.id shouldBe id
                it.groupId shouldBe ""
                it.value shouldBe 0.0
                it.votes shouldBe emptyList()
            }
            context.vote shouldBe Vote.NONE
        }
        test("rating create request to internal model and response") {
            val groupId = randomId
            val ratingCreateRequest = RatingCreateRequest(groupId)
            context.withRequest(ratingCreateRequest)
            context.rating.should {
                it.groupId shouldBe groupId
                it.id shouldBe ""
                it.value shouldBe 0.0
                it.votes shouldBe emptyList()
            }
            context.vote shouldBe Vote.NONE
            context.formRatingResponse().should {
                it.groupId shouldBe groupId
                it.id shouldBe ""
                it.value shouldBe 0.0
                it.votes shouldBe emptyList()
            }
            context.vote shouldBe Vote.NONE
        }
    }
}
