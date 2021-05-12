package ru.otus.repository.schema

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ReferenceOption
import ru.otus.model.Rating
import ru.otus.model.Vote
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

object RatingsTable : UUIDTable("ratings") {
    val groupId = uuid("group_id").nullable()
    val value = decimal("value", 3, 2).nullable()
}

object VotesTable : UUIDTable("votes") {
    val ratingId = reference("rating_id", RatingsTable, ReferenceOption.CASCADE)
    val voterId = uuid("voter_id").nullable()
    val value = integer("value")
    val voteDateTime = varchar("vote_date_time", 25)
}

class RatingDto(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<RatingDto>(RatingsTable)

    var groupId by RatingsTable.groupId
    var value by RatingsTable.value
    val votes by VoteDto referrersOn VotesTable.ratingId

    fun toModel(): Rating = Rating(
        id = id.toString(),
        groupId = groupId?.toString().orEmpty(),
        value = value?.toDouble() ?: 0.0,
        votes = votes.map { it.toModel() }
    )
}

class VoteDto(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<VoteDto>(VotesTable)

    var ratingId by RatingDto referencedOn VotesTable.ratingId
    var voterId by VotesTable.voterId
    var value by VotesTable.value
    var voteDateTime by VotesTable.voteDateTime

    fun toModel(): Vote = Vote(
        id = id.toString(),
        ratingId = ratingId.id.toString(),
        voterId = voterId?.toString().orEmpty(),
        value = value,
        voteDateTime = LocalDateTime.parse(voteDateTime, dateTimeFormatter)
    )
}

val dateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
