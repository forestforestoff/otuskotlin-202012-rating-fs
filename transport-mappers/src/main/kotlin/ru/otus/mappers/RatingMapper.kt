package ru.otus.mappers

import ru.otus.model.IRatingGroupId
import ru.otus.model.IRatingId
import ru.otus.model.Rating
import ru.otus.model.Vote
import ru.otus.transport.openapi.models.*
import java.time.LocalDateTime

fun Vote.toResponse(): VoteResponse = VoteResponse(
    id, groupId, value, voterId, voteTime.toString()
)

fun VoteRequest.toInternal(): Vote = Vote(
    id = id ?: "",
    groupId = groupId ?: "",
    value = value ?: -1,
    voterId = voterId ?: "",
    voteTime = LocalDateTime.now()
)

fun Rating.toResponse(): RatingResponse = RatingResponse(
    id = id, groupId = groupId, value = value, votes = votes.map { it.toResponse() }
)

fun RatingRequest.toInternal(): IRatingId = Rating(id = id ?: "")

fun RatingCreateRequest.toInternal(): IRatingGroupId = Rating(groupId = groupId ?: "")
