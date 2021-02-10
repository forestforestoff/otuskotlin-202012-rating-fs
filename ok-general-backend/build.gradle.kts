plugins {
    kotlin("jvm") version "1.4.30"
}

val groupName: String by project
val groupVersion: String by project

group = groupName
version = groupVersion

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    testImplementation(kotlin("test"))
    testImplementation(kotlin("test-junit"))
}
