package ru.otus.repository.inmemory.model

import ru.otus.model.Vote
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class VoteDto(
    val id: String? = null,
    val ratingId: String? = null,
    val voterId: String? = null,
    var value: Int? = null,
    val voteDateTime: String? = null
) {

    fun fromDto() = Vote(
        id = id ?: "",
        ratingId = ratingId ?: "",
        voterId = voterId ?: "",
        value = value ?: -1,
        voteDateTime = voteDateTime?.let { LocalDateTime.parse(voteDateTime, dateTimeFormatter) } ?: LocalDateTime.now()
    )
}

fun Vote.toDto() = VoteDto(
    id = id.takeIfNotBlank(),
    ratingId = ratingId.takeIfNotBlank(),
    voterId = voterId.takeIfNotBlank(),
    value = value.takeIf { it != -1 },
    voteDateTime = voteDateTime.format(dateTimeFormatter)
)

private fun String.takeIfNotBlank() = takeIf { it.isNotBlank() }

private val dateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
