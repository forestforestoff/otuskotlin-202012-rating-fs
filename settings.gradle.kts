rootProject.name = "otuskotlin-202012-rating-fs"

pluginManagement {
    val kotlinVersion: String by settings

    plugins {
        kotlin("multiplatform") version kotlinVersion
        kotlin("jvm") version kotlinVersion
        kotlin("js") version kotlinVersion
    }
}

include("multiplatform-common")
include("backend-common")
include("transport-openapi")
