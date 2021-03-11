package ru.otus.mappers

import ru.otus.model.Rating
import ru.otus.model.Vote
import ru.otus.model.context.Context
import ru.otus.transport.openapi.models.*
import java.time.LocalDateTime

fun Context.withRequest(voteRequest: VoteRequest): Context = apply {
    vote = voteRequest.toInternal()
}

fun Context.withRequest(ratingRequest: RatingRequest): Context = apply {
    rating = ratingRequest.toInternal()
}

fun Context.withRequest(ratingCreateRequest: RatingCreateRequest): Context = apply {
    rating = ratingCreateRequest.toInternal()
}

fun Context.formRatingResponse(): RatingResponse = rating.toResponse()

fun Context.formVoteResponse(): VoteResponse = vote.toResponse()

private fun Vote.toResponse(): VoteResponse = VoteResponse(
    id, value, voterId, voteDateTime.toString()
)

private fun Rating.toResponse(): RatingResponse = RatingResponse(
    id = id, groupId = groupId, value = value, votes = votes.map { it.toResponse() }
)

private fun VoteRequest.toInternal(): Vote = Vote(
    id = id ?: "",
    value = value ?: -1,
    voterId = voterId ?: "",
    voteDateTime = LocalDateTime.now()
)

private fun RatingRequest.toInternal(): Rating = Rating(id = id ?: "")

private fun RatingCreateRequest.toInternal(): Rating = Rating(groupId = groupId ?: "")
