plugins {
    kotlin("jvm")
}

dependencies {
    val coroutinesVersion: String by project
    val logbackVersion: String by project
    val logbackEncoderVersion: String by project
    val logbackKafkaVersion: String by project
    val janinoVersion: String by project

    implementation(kotlin("stdlib"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
    implementation("net.logstash.logback:logstash-logback-encoder:$logbackEncoderVersion")
    implementation("com.github.danielwegener:logback-kafka-appender:$logbackKafkaVersion")
    implementation("org.codehaus.janino:janino:$janinoVersion")
    runtimeOnly("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.12.3")
    api("ch.qos.logback:logback-classic:$logbackVersion")
    testImplementation(kotlin("test"))
    testImplementation(kotlin("test-junit"))
}
