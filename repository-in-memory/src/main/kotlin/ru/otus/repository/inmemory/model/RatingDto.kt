package ru.otus.repository.inmemory.model

import ru.otus.model.Rating
import java.util.*

data class RatingDto(
    val id: String? = null,
    val groupId: String? = null,
    var value: Double? = null
) {

    fun fromDto(votes: List<VoteDto>?): Rating = Rating(
        id = id ?: "",
        groupId = groupId ?: "",
        votes = votes?.map { it.fromDto() }.orEmpty(),
        value = value ?: 0.0
    )
}

fun Rating.toCreateDto(): RatingDto = RatingDto(
    id = UUID.randomUUID().toString(),
    groupId = groupId.takeIf { it.isNotBlank() }
)
