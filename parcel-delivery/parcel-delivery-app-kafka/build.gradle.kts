plugins {
    id("jvm-convention")
    application
    alias(libs.plugins.shadow)
}

application {
    mainClass.set("ru.parceldelivery.app.kafka.MainKt")
}

dependencies {
    // transport models
    implementation(project(":parcel-delivery-api-v1"))
    implementation(project(":parcel-delivery-api-v1-mappers"))
    implementation(project(":parcel-delivery-api-v1-transport-mappers"))
    implementation(project(":parcel-delivery-common"))
    implementation(project(":parcel-delivery-app-common"))

    implementation(libs.coroutines.core)
    implementation(libs.kafka.clients)
    implementation(libs.logback.classic)
}

tasks {
    shadowJar {
        manifest {
            attributes(mapOf("Main-Class" to application.mainClass.get()))
        }
    }
}