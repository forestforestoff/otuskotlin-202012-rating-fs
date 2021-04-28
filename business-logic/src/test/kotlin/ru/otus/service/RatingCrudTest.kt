package ru.otus.service

import io.kotest.assertions.throwables.shouldThrowMessage
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import ru.otus.model.Rating
import ru.otus.model.Vote
import ru.otus.model.context.ExchangeContext
import ru.otus.service.RatingCrud.create
import ru.otus.service.RatingCrud.read
import ru.otus.service.RatingCrud.update

class RatingCrudTest : FunSpec() {

    init {
        givenContext.run {
            test("Rating create test") {
                create()
                rating.should {
                    it.id shouldBe "someNewId"
                    it.groupId shouldBe ""
                    it.value shouldBe 0.0
                    it.votes shouldBe emptyList()
                }
            }
            test("Rating read test") {
                read()
                rating.should { rating ->
                    rating.id shouldBe "someNewId"
                    rating.groupId shouldBe "someId"
                    rating.value shouldBe 5.0
                    rating.votes.first().should {
                        it.id shouldBe "someVoteId"
                        it.voterId shouldBe "someVoterId"
                        it.value shouldBe 5
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

