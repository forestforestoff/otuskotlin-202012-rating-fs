package ru.otus.repository.dao

import org.jetbrains.exposed.sql.transactions.transaction
import ru.otus.model.Rating
import ru.otus.model.context.ExchangeContext
import ru.otus.repositories.IRatingRepository
import ru.otus.repository.config.DbConfig.prodDb
import ru.otus.repository.config.DbConfig.testDb
import ru.otus.repository.schema.RatingDto
import ru.otus.repository.schema.VoteDto
import ru.otus.repository.schema.dateTimeFormatter
import java.util.*

object RatingRepoPostgresqlExposed : IRatingRepository {

    private val db = testDb ?: prodDb

    override suspend fun ExchangeContext.read() {
        rating = transaction(db) {
            RatingDto.findById(rating.id.uuid()!!)?.toModel() ?: Rating.NONE
        }
    }

    override suspend fun ExchangeContext.create() {
        rating = transaction(db) {
            RatingDto[RatingDto.new { groupId = rating.groupId.takeIf { it.isNotBlank() }?.uuid() }.id].toModel()
        }
    }

    override suspend fun ExchangeContext.update() {
        rating = transaction(db) {
            VoteDto.new {
                ratingId = RatingDto[(vote.ratingId.uuid()!!)]
                voterId = vote.voterId.uuid()
                value = vote.value
                voteDateTime = vote.voteDateTime.format(dateTimeFormatter)
            }
            RatingDto[vote.ratingId.uuid()!!].apply {
                value = votes.map { it.value }.average().toBigDecimal()
            }.toModel()
        }
    }

    override suspend fun ExchangeContext.delete() {
        transaction(db) {
            RatingDto.findById(rating.id.uuid()!!)?.delete()
        }
        rating = Rating.NONE
    }
}

private fun String.uuid() = takeIf { it.isNotBlank() }?.let { UUID.fromString(this) }
