package ru.otus.authentication

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.config.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.coroutines.runBlocking
import ru.otus.main.module
import ru.otus.model.PrincipalModel
import ru.otus.model.Rating
import ru.otus.model.UserGroups
import ru.otus.model.context.ExchangeContext
import ru.otus.service.RatingCrud.delete
import ru.otus.transport.openapi.models.RatingCreateRequest
import ru.otus.transport.openapi.models.RatingResponse
import ru.otus.utils.fromJson
import ru.otus.utils.toJson
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals

class JwtAuthTest {
    companion object {
        const val SECRET = "rating-secret"
        const val AUDIENCE = "rating-users"
        const val REALM = "RatingApp"
        const val DOMAIN = "http://localhost/"
        const val USER_ID = "test-user-id"
        const val USER_FNAME = "Ivan"
        const val USER_MNAME = "Ivanovich"
        const val USER_LNAME = "Ivanov"
        val TOKEN = JWT.create()
            .withSubject("Authentication")
            .withIssuer(DOMAIN)
            .withAudience(AUDIENCE)
            .withClaim("id", USER_ID)
            .withClaim("fname", USER_FNAME)
            .withClaim("mname", USER_MNAME)
            .withClaim("lname", USER_LNAME)
            .withArrayClaim("groups", arrayOf(UserGroups.ADMIN.toString()))
            .sign(Algorithm.HMAC256(SECRET))
            .toString()
    }

    @Test
    fun jwtTest() {
        println(TOKEN)
        withTestApplication({
            (environment.config as MapApplicationConfig).apply {
                put("marketplace.auth.jwt.secret", SECRET)
                put("marketplace.auth.jwt.audience", AUDIENCE)
                put("marketplace.auth.jwt.domain", DOMAIN)
                put("marketplace.auth.jwt.realm", REALM)
            }
            module()
        }) {
            val someGroupId = UUID.randomUUID().toString()
            handleRequest(HttpMethod.Post, "/rating/create") {
                val request = RatingCreateRequest(groupId = someGroupId)
                setBody(toJson(request))
                addHeader("Content-Type", "application/json")
                addHeader("Authorization", "Bearer $TOKEN")
            }.apply {
                println(response.content)
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals(ContentType.Application.Json.withCharset(Charsets.UTF_8), response.contentType())
                val ratingResponseFromJson = fromJson(response.content.orEmpty(), RatingResponse::class.java)
                assertEquals(someGroupId, ratingResponseFromJson.groupId, "Wrong groupId!")
                assertEquals(0.0, ratingResponseFromJson.value, "Wrong value!")
                assertEquals(emptyList(), ratingResponseFromJson.votes, "Wrong votes!")
                runBlocking {
                    ExchangeContext(
                        rating = Rating(id = ratingResponseFromJson.id.orEmpty()),
                        principalModel = PrincipalModel(groups = listOf(UserGroups.ADMIN))
                    ).delete()
                }
            }
        }
    }
}
