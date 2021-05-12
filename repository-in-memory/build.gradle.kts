plugins {
    kotlin("jvm")
}

dependencies {
    val cache2kVersion: String by project
    val coroutinesVersion: String by project

    implementation(project(":backend-common"))

    implementation(kotlin("stdlib"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
    implementation("org.cache2k:cache2k-api:${cache2kVersion}")
    runtimeOnly("org.cache2k:cache2k-core:${cache2kVersion}")

    testImplementation(kotlin("test"))
    testImplementation(kotlin("test-junit"))
}
