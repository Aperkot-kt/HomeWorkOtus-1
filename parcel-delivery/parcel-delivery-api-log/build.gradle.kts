plugins {
    id("jvm-convention")
    id("org.openapi.generator")
}

openApiGenerate {
    generatorName.set("kotlin")
    inputSpec.set(rootProject.ext["spec-log-v1"] as String)
    outputDir.set(layout.buildDirectory.dir("generated").get().asFile.absolutePath)
    packageName.set("${project.group}.api.log.v1")
    modelPackage.set("${project.group}.api.log.v1.models")
    globalProperties.set(
        mapOf(
            "models" to "",
            "modelDocs" to "false",
            "apis" to "false",
            "apiDocs" to "false",
            "supportingFiles" to "false",
        )
    )
    configOptions.set(
        mapOf(
            "serializationLibrary" to "jackson",
        )
    )
}

sourceSets {
    main {
        kotlin {
            srcDir(layout.buildDirectory.dir("generated/src/main/kotlin"))
        }
    }
}

tasks.compileKotlin {
    dependsOn("openApiGenerate")
}

dependencies {
    implementation(project(":parcel-delivery-common"))
    implementation(libs.kotlinx.datetime)
    implementation(libs.jackson.module.kotlin)
}