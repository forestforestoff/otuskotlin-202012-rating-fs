package ru.otus.service

import ru.otus.model.Rating
import ru.otus.model.Vote
import ru.otus.controller.context.Context
import java.time.LocalDateTime

object RatingService {

    fun Context.createRating() {
        rating = Rating(
            id = "someNewId",
            groupId = rating.groupId,
            value = 0.0,
            votes = emptyList()
        )
    }

    fun Context.getRating() {
        rating = Rating(
            id = rating.id,
            groupId = "someId",
            value = 5.0,
            votes = listOf(
                Vote(
                    id = rating.id,
                    voterId = "someVoterId",
                    value = 5,
                    voteDateTime = LocalDateTime.now()
                )
            )
        )
    }

    fun Context.updateRating() {
        rating = Rating(
            id = vote.id,
            groupId = "someGroupId",
            value = vote.value.toDouble(),
            votes = listOf(vote)
        )
    }
}
