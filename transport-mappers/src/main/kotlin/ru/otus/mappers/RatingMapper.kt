package ru.otus.mappers

import ru.otus.model.IRatingGroupId
import ru.otus.model.IRatingId
import ru.otus.model.Rating
import ru.otus.model.Vote
import ru.otus.transport.openapi.models.RatingCreateRequest
import ru.otus.transport.openapi.models.RatingRequest
import ru.otus.transport.openapi.models.RatingResponse
import ru.otus.transport.openapi.models.VoteRequest

fun Rating.toResponse(): RatingResponse = RatingResponse(
    id, groupId, value, voterIdAndTime.mapValues { it.value.toString() }
)

fun RatingRequest.toInternal(): IRatingId = Rating(id = id ?: "")

fun RatingCreateRequest.toInternal(): IRatingGroupId = Rating(groupId = groupId ?: "")

fun VoteRequest.toInternal(): Vote = Vote(
    id = id ?: "",
    groupId = groupId ?: "",
    value = value ?: -1,
    voterId = voterId ?: ""
)
