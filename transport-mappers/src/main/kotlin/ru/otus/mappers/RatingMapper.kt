package ru.otus.mappers

import ru.otus.model.Rating
import ru.otus.model.Vote
import ru.otus.transport.openapi.models.*
import java.time.LocalDateTime

fun Vote.toResponse(): VoteResponse = VoteResponse(
    id, ratingId, value, voterId, voteDateTime.toString()
)

fun Rating.toResponse(): RatingResponse = RatingResponse(
    id = id,
    groupId = groupId,
    value = value,
    votes = votes.map { it.toResponse() }
)

fun VoteRequest.toInternal(): Vote = Vote(
    id = id ?: "",
    ratingId = ratingId ?: "",
    value = value ?: -1,
    voterId = voterId ?: "",
    voteDateTime = LocalDateTime.now()
)

fun RatingRequest.toInternal(): Rating = Rating(id = id ?: "")

fun RatingCreateRequest.toInternal(): Rating = Rating(groupId = groupId ?: "")
