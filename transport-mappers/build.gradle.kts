plugins {
    kotlin("jvm")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

dependencies {
    val kotestVersion: String by project
    
    implementation(kotlin("stdlib"))
    implementation(project(":backend-common"))
    implementation(project(":transport-openapi"))
    testImplementation("io.kotest:kotest-runner-junit5:$kotestVersion")
    testImplementation("io.kotest:kotest-assertions-core:$kotestVersion")
    testImplementation("io.kotest:kotest-property:$kotestVersion")
}
