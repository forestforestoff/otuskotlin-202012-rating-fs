plugins {
    kotlin("jvm")
    id("org.openapi.generator")
}

dependencies {
    val gsonVersion: String by project
    val okhttp3Version: String by project

    implementation(kotlin("stdlib"))
    implementation("com.google.code.gson:gson:$gsonVersion")
    implementation("com.squareup.okhttp3:okhttp:$okhttp3Version")
    testImplementation(kotlin("test"))
    testImplementation(kotlin("test-junit"))
}

openApiGenerate {
    val basePackage = "${project.group}.transport.openapi"
    packageName.set(basePackage)
    generatorName.set("kotlin")
    inputSpec.set("${rootProject.projectDir}/specs/rating-main-api.yaml")
    configOptions.put("serializationLibrary", "gson")
}

sourceSets.main {
    java.srcDirs("$buildDir/generate-resources/main/src/main/kotlin")
}

tasks {
    compileKotlin.get().dependsOn(openApiGenerate)
}
