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
    implementation(kotlin("stdlib"))
    implementation("io.ktor:ktor-server-core:$ktorVersion")
    implementation("io.ktor:ktor-server-netty:$ktorVersion")
    implementation("ch.qos.logback:logback-classic:$logbackVersion")
    implementation("io.ktor:ktor-gson:$ktorVersion")
    implementation("io.ktor:ktor-websockets:$ktorVersion")

    testImplementation("io.ktor:ktor-server-tests:$ktorVersion")
}
