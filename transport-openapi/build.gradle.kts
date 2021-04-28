plugins {
    kotlin("jvm")
    id("org.openapi.generator")
}

dependencies {
    val jacksonVersion: String by project

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonVersion")
    implementation(kotlin("stdlib"))
    testImplementation(kotlin("test"))
    testImplementation(kotlin("test-junit"))
}

openApiGenerate {
    val basePackage = "${project.group}.transport.openapi"
    packageName.set(basePackage)
    generatorName.set("kotlin")
    apiPackage.set("$basePackage.api")
    invokerPackage.set("$basePackage.invoker")
    modelPackage.set("$basePackage.models")
    globalProperties.apply {
        put("models", "")
        put("modelDocs", "false")
        put("invoker", "false")
        put("apis", "false")
    }
    configOptions.set(
        mapOf(
            "dateLibrary" to "string",
            "enumPropertyNaming" to "UPPERCASE",
            "library" to "multiplatform",
            "serializationLibrary" to "jackson",
            "collectionType" to "list"
        )
    )
    inputSpec.set("${rootProject.projectDir}/specs/rating-main-api.yaml")
}

sourceSets.main {
    java.srcDirs("$buildDir/generate-resources/main/src/main/kotlin")
}

tasks {
    compileKotlin.get().dependsOn(openApiGenerate)
}
