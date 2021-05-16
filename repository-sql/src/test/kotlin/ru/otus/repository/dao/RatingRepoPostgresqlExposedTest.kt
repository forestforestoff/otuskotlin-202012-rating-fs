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

    private val someGroupId = UUID.randomUUID().toString()

    @Test
    fun repoTest() {
        ExchangeContext().run {
            runBlocking {
                rating = Rating(groupId = someGroupId)
                create()
                assert(rating.id.isNotBlank()) { "Rating id should not be empty after create" }
                assertEquals(rating.groupId, someGroupId) { "Group id should be $someGroupId after create" }
                assertEquals(rating.votes, emptyList<Vote>()) { "Votes list should be empty after create" }
                assertEquals(rating.value, 0.0) { "Value should be empty after create" }

                vote = Vote(ratingId = rating.id, value = 5)
                update()
                assertEquals(rating.groupId, someGroupId) { "Group id should be $someGroupId after update" }
                assertEquals(rating.value, 5.0) { "Value should be 5.0 after update" }
                assertEquals(rating.votes.size, 1) { "Votes list should have one vote after update" }
                val firstVote = rating.votes.first()
                assertEquals(firstVote.value, 5) { "Vote value should be 5 after update" }
                assertEquals(firstVote.ratingId, rating.id) { "Vote rating id should be ${rating.id} after update" }

                vote = Vote(ratingId = rating.id, value = 3)
                update()
                assertEquals(rating.groupId, someGroupId) { "Group id should be $someGroupId after update" }
                assertEquals(rating.value, 4.0) { "Value should be 4.0 after update" }
                assertEquals(rating.votes.size, 2) { "Votes list should have two votes after update" }

                val id = rating.id
                rating = Rating(id)
                assertEquals(rating.groupId, "") { "Group id should be empty before read" }
                assertEquals(rating.value, 0.0) { "Value should be 0.0 before read" }
                assertEquals(rating.votes.size, 0) { "Votes list should be empty before read" }
                read()
                assertEquals(rating.groupId, someGroupId) { "Group id should be $someGroupId after read" }
                assertEquals(rating.value, 4.0) { "Value should be 4.0 after read" }
                assertEquals(rating.votes.size, 2) { "Votes list should have two votes after read" }
            }
        }
    }
}

private class PostgresqlContainer : PostgreSQLContainer<PostgresqlContainer>(POSTGRESQL_DOCKER_IMAGE)

private const val POSTGRESQL_DOCKER_IMAGE = "postgres:latest"
