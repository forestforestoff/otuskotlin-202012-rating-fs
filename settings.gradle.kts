rootProject.name = "otuskotlin-202012-rating-fs"

pluginManagement {
    val kotlinVersion: String by settings
    val openApiVersion: String by settings

    plugins {
        kotlin("multiplatform") version kotlinVersion
        kotlin("jvm") version kotlinVersion
        kotlin("js") version kotlinVersion
        kotlin("plugin.serialization") version kotlinVersion

        id("org.openapi.generator") version openApiVersion
    }
}

include("multiplatform-common")
include("backend-common")
include("transport-openapi")
include("transport-mappers")
include("rating-app-ktor")
include("pipeline")
include("business-logic")
include("repository-in-memory")
include("repository-sql")
include("logging")
