group = "ru.otus"
version = "0.0.1"

plugins {
    kotlin("jvm") apply false
}

subprojects {
    group = rootProject.group
    version = rootProject.version

    repositories {
        jcenter()
        mavenCentral()
    }

    tasks.withType<org.jetbrains.kotlin.gradle.dsl.KotlinJvmCompile> {
        kotlinOptions.jvmTarget = "11"
    }
}
