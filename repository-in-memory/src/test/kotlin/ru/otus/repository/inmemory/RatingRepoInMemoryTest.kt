package ru.otus.repository.inmemory

import kotlinx.coroutines.runBlocking
import ru.otus.model.Rating
import ru.otus.model.Vote
import ru.otus.model.context.ExchangeContext
import java.time.Duration
import kotlin.test.Test
import kotlin.test.assertEquals

class RatingRepoInMemoryTest {

    @Test
    fun createReadUpdateTest() {
        val repo = RatingRepoInMemory(
            ttl = Duration.ofMinutes(1)
        )
        ExchangeContext(rating = Rating(groupId = "someGroupId")).run {
            repo.run {
                runBlocking {
                    create()
                    assertEquals(rating.groupId, "someGroupId")
                    assertEquals(rating.votes, emptyList())
                    vote = Vote(ratingId = rating.id, value = 5)
                    update()
                    assertEquals(rating.groupId, "someGroupId")
                    assertEquals(rating.value, 5.0)
                    assertEquals(rating.votes.size, 1)
                    val firstVote = rating.votes.first()
                    assertEquals(firstVote.value, 5)
                    assertEquals(firstVote.ratingId, rating.id)
                    vote = Vote(ratingId = rating.id, value = 3)
                    update()
                    assertEquals(rating.groupId, "someGroupId")
                    assertEquals(rating.value, 4.0)
                    assertEquals(rating.votes.size, 2)
                    val id = rating.id
                    rating = Rating(id)
                    assertEquals(rating.groupId, "")
                    assertEquals(rating.value, 0.0)
                    assertEquals(rating.votes.size, 0)
                    read()
                    assertEquals(rating.groupId, "someGroupId")
                    assertEquals(rating.value, 4.0)
                    assertEquals(rating.votes.size, 2)
                }
            }
        }
    }
}
