package ru.otus.repository.config

import org.jetbrains.exposed.sql.Database

object DbConfig {
    val prodDb by lazy {
        Database.connect(
            url = "jdbc:postgresql://localhost:5432/rating",
            driver = "org.postgresql.Driver",
            user = "postgres",
            password = "postgres"
        )
    }
    var testDb: Database? = null
}
