package ru.otus.repository.dao

import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import ru.otus.model.Rating
import ru.otus.model.Vote
import ru.otus.model.context.ExchangeContext
import ru.otus.repository.config.DbConfig.testDb
import ru.otus.repository.dao.RatingRepoPostgresqlExposed.create
import ru.otus.repository.dao.RatingRepoPostgresqlExposed.read
import ru.otus.repository.dao.RatingRepoPostgresqlExposed.update
import ru.otus.repository.schema.RatingsTable
import ru.otus.repository.schema.VotesTable
import java.util.*

@Testcontainers
class RatingRepoPostgresqlExposedTest {

    @Container
    private val postgresqlContainer = PostgresqlContainer()
        .withDatabaseName("rating")
        .withUsername("postgres")
        .withPassword("postgres")

    @BeforeEach
    private fun setUp() {
        testDb = Database.connect(
            url = postgresqlContainer.jdbcUrl,
            driver = postgresqlContainer.driverClassName,
            user = postgresqlContainer.username,
            password = postgresqlContainer.password
        )
        transaction(testDb) {
            SchemaUtils.create(RatingsTable, VotesTable)
        }
    }

    @Test
    fun repoTest() {
        ExchangeContext().run {
            runBlocking {
                val someGroupId = UUID.randomUUID().toString()
                rating = Rating(groupId = someGroupId)
                create()
                assert(rating.id.isNotBlank())
                assertEquals(rating.groupId, someGroupId)
                assertEquals(rating.votes, emptyList<Vote>())
                assertEquals(rating.value, 0.0)
                vote = Vote(ratingId = rating.id, value = 5)
                update()
                assertEquals(rating.groupId, someGroupId)
                assertEquals(rating.value, 5.0)
                assertEquals(rating.votes.size, 1)
                val firstVote = rating.votes.first()
                assertEquals(firstVote.value, 5)
                assertEquals(firstVote.ratingId, rating.id)
                vote = Vote(ratingId = rating.id, value = 3)
                update()
                assertEquals(rating.groupId, someGroupId)
                assertEquals(rating.value, 4.0)
                assertEquals(rating.votes.size, 2)
                val id = rating.id
                rating = Rating(id)
                assertEquals(rating.groupId, "")
                assertEquals(rating.value, 0.0)
                assertEquals(rating.votes.size, 0)
                read()
                assertEquals(rating.groupId, someGroupId)
                assertEquals(rating.value, 4.0)
                assertEquals(rating.votes.size, 2)
            }
        }
    }
}

private class PostgresqlContainer : PostgreSQLContainer<PostgresqlContainer>(POSTGRESQL_DOCKER_IMAGE)

private const val POSTGRESQL_DOCKER_IMAGE = "postgres:latest"
