package ru.otus.repository.inmemory

import org.cache2k.Cache
import org.cache2k.Cache2kBuilder
import ru.otus.model.context.ExchangeContext
import ru.otus.repositories.IRatingRepository
import ru.otus.repository.inmemory.model.RatingDto
import ru.otus.repository.inmemory.model.VoteDtoWrapper
import ru.otus.repository.inmemory.model.toCreateDto
import ru.otus.repository.inmemory.model.toDto
import java.time.Duration
import java.util.concurrent.TimeUnit

class RatingRepoInMemory(ttl: Duration) : IRatingRepository {

    private val ratingCache: Cache<String, RatingDto> = object : Cache2kBuilder<String, RatingDto>() {}
        .expireAfterWrite(ttl.toMillis(), TimeUnit.MILLISECONDS)
        .build()

    private val voteCache: Cache<String, VoteDtoWrapper> = object : Cache2kBuilder<String, VoteDtoWrapper>() {}
        .expireAfterWrite(ttl.toMillis(), TimeUnit.MILLISECONDS)
        .build()

    override suspend fun ExchangeContext.read() {
        val id = rating.id
        id.ifBlank { throw IllegalArgumentException("No rating id in the context $contextUid") }
        rating = ratingCache.get(id)?.fromDto(voteCache.get(id)?.voteDtos)
            ?: throw IllegalArgumentException("No rating found with id $id and context $contextUid")
    }

    override suspend fun ExchangeContext.create() {
        val dtoModel = rating.toCreateDto()
        ratingCache.put(dtoModel.id!!, dtoModel)
        rating = ratingCache.get(dtoModel.id)?.fromDto(emptyList())!!
    }

    override suspend fun ExchangeContext.update() {
        val id = vote.ratingId
        id.ifBlank { throw IllegalArgumentException("No rating id in the vote $id of the context $contextUid") }
        ratingCache.get(id)?.let { ratingDto ->
            val voteDtos = updateVote(id).voteDtos
            ratingDto.value = voteDtos?.mapNotNull { it.value }?.average() ?: 0.0
            ratingCache.put(id, ratingDto)
            rating = ratingDto.fromDto(voteDtos)
        } ?: throw IllegalArgumentException("No rating with id $id found")
    }

    override suspend fun ExchangeContext.delete() {
        ratingCache.remove(rating.id)
        voteCache.remove(rating.id)
    }

    private fun ExchangeContext.updateVote(id: String): VoteDtoWrapper =
        voteCache.get(id)?.let {
            it.voteDtos?.add(vote.toDto())
                ?: throw IllegalStateException("Vote cache contains empty wrapper with id $id")
            voteCache.put(id, it)
            it
        } ?: VoteDtoWrapper(mutableListOf(vote.toDto())).also {
            voteCache.put(id, it)
        }
}
