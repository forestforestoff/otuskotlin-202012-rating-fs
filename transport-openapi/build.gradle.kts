plugins {
    kotlin("jvm")
    id("org.openapi.generator")
}

dependencies {
    val moshiVersion: String by project
    val okhttp3Version: String by project

    implementation(kotlin("stdlib"))
    implementation("com.squareup.moshi:moshi-kotlin:$moshiVersion")
    implementation("com.squareup.okhttp3:okhttp:$okhttp3Version")
    testImplementation(kotlin("test"))
    testImplementation(kotlin("test-junit"))
}

openApiGenerate {
    val basePackage = "${project.group}.transport.openapi"
    packageName.set(basePackage)
    generatorName.set("kotlin")
    inputSpec.set("${rootProject.projectDir}/specs/rating-main-api.yaml")
}

sourceSets.main {
    java.srcDirs("$buildDir/generate-resources/main/src/main/kotlin")
}

tasks {
    compileKotlin.get().dependsOn(openApiGenerate)
}
