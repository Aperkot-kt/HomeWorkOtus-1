plugins {
    id("jvm-convention")
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency.management)
    alias(libs.plugins.kotlin.spring)
}

dependencies {
    implementation(libs.spring.boot.starter.webflux)
    implementation(libs.spring.boot.starter.actuator)
    implementation(libs.springdoc.openapi.webflux.ui)
    implementation(libs.jackson.module.kotlin)
    implementation(kotlin("reflect"))
    implementation(kotlin("stdlib"))

    implementation(libs.coroutines.core)
    implementation(libs.coroutines.reactor)

    // Внутренние модели
    implementation(project(":parcel-delivery-common"))
    implementation(project(":parcel-delivery-app-common"))

    // v1 api
    implementation(project(":parcel-delivery-api-v1"))
    implementation(project(":parcel-delivery-api-v1-transport-mappers"))

    // tests
    testImplementation(kotlin("test-junit5"))
    testImplementation(libs.spring.boot.starter.test)
}

tasks {
    withType<ProcessResources> {
        val files = listOf("spec-v1").map {
            rootProject.ext[it]
        }
        from(files) {
            into("/static")
            filter {
                // Устанавливаем версию в сваггере
                it.replace("\${VERSION_APP}", project.version.toString())
            }
        }
    }

    bootBuildImage {
        imageName.set("parcel-delivery-app-spring")
    }
}