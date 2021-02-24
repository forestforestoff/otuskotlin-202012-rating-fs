rootProject.name = "otuskotlin-202012-rating-fs"

pluginManagement {
    val kotlinVersion: String by settings

    plugins {
        kotlin("multiplatform") version kotlinVersion
        kotlin("jvm") version kotlinVersion
        kotlin("js") version kotlinVersion
    }
}

include("ok-general-multiplatform")
include("ok-general-backend")
include("ok-general")