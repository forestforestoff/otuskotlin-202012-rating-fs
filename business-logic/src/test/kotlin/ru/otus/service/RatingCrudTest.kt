package ru.otus.service

import io.kotest.assertions.throwables.shouldThrowMessage
import io.kotest.core.annotation.Ignored
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import ru.otus.model.Rating
import ru.otus.model.Vote
import ru.otus.model.context.ExchangeContext
import ru.otus.service.RatingCrud.create
import ru.otus.service.RatingCrud.read
import ru.otus.service.RatingCrud.update
import java.util.*

@Ignored
class RatingCrudTest : FunSpec() {

    init {
        givenContext.run {
            val someGroupId = UUID.randomUUID().toString()
            test("Rating create test") {
                rating = Rating(groupId = someGroupId)
                create()
                rating.should {
                    it.id.isNotBlank()
                    it.groupId shouldBe someGroupId
                    it.value shouldBe 0.0
                    it.votes shouldBe emptyList()
                }
            }
            test("Rating update test") {
                vote = Vote(ratingId = rating.id, value = 5)
                update()
                rating.should { rating ->
                    rating.id.isNotBlank()
                    rating.groupId shouldBe someGroupId
                    rating.value shouldBe 5.0
                    rating.votes.first().should {
                        it.id.isNotBlank()
                        it.ratingId shouldBe rating.id
                        it.value shouldBe 5
                    }
                }
            }
            test("Rating update test 2") {
                val someVoterId = UUID.randomUUID().toString()
                vote = Vote(ratingId = rating.id, value = 3, voterId = someVoterId)
                update()
                rating.should { rating ->
                    rating.id.isNotBlank()
                    rating.groupId shouldBe someGroupId
                    rating.value shouldBe 4.0
                    rating.votes.size shouldBe 2
                    rating.votes.first { it.voterId.isNotBlank() }.should {
                        it.voterId shouldBe someVoterId
                        it.value shouldBe 3
                        it.ratingId shouldBe rating.id
                    }
                }
            }
            test("Validation test") {
                rating = Rating.NONE
                shouldThrowMessage("Rating ID should not be empty") {
                    read()
                }

                vote = Vote.NONE
                shouldThrowMessage("Invalid vote request update") {
                    update()
                }
            }
        }
    }
}

private val givenContext = ExchangeContext()

