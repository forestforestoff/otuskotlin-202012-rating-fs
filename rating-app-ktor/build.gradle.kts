plugins {
    kotlin("jvm")
}

dependencies {
    val ktorVersion: String by project
    val logbackVersion: String by project

    implementation(project(":backend-common"))
    implementation(project(":transport-openapi"))
    implementation(project(":transport-mappers"))
    implementation(project(":business-logic"))
    implementation(project(":logging"))
    implementation(kotlin("stdlib"))
    implementation("io.ktor:ktor-server-core:$ktorVersion")
    implementation("io.ktor:ktor-server-netty:$ktorVersion")
    implementation("ch.qos.logback:logback-classic:$logbackVersion")
    implementation("io.ktor:ktor-jackson:$ktorVersion")
    implementation("io.ktor:ktor-websockets:$ktorVersion")
    implementation("io.ktor:ktor-auth:$ktorVersion")
    implementation("io.ktor:ktor-auth-jwt:$ktorVersion")

    testImplementation("io.ktor:ktor-server-tests:$ktorVersion")
}
