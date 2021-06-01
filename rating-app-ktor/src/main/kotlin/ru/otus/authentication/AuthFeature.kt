package ru.otus.authentication

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*

fun Application.authFeature(testing: Boolean) {

    val jwtSecret = environment.config.propertyOrNull("jwt.secret")?.getString() ?: "rating-secret"
    val issuer = environment.config.propertyOrNull("jwt.domain")?.getString() ?: "http://localhost/"
    val audience = environment.config.propertyOrNull("jwt.audience")?.getString() ?: "rating-users"
    val myRealm = environment.config.propertyOrNull("jwt.realm")?.getString() ?: "RatingApp"

    install(Authentication) {
        jwt("auth-jwt") {
            skipWhen { testing }
            realm = myRealm
            verifier(
                JWT
                    .require(Algorithm.HMAC256(jwtSecret))
                    .withAudience(audience)
                    .withIssuer(issuer)
                    .build()
            )
            validate { credential ->
                if (credential.payload.audience.contains(audience)) {
                    JWTPrincipal(credential.payload)
                } else {
                    null
                }
            }
        }
    }

}