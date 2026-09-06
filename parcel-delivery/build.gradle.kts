plugins {
    id("org.openapi.generator") version "7.23.0" apply false
}

group = "ru.parceldelivery"
version = "0.0.1"

allprojects {
    repositories {
        mavenCentral()
    }
}

subprojects {
    group = rootProject.group
    version = rootProject.version
}

ext {
    val specDir = layout.projectDirectory.dir("./parcel-delivery-api-v1/src/resources/ru/parceldelivery/api.v1.spec")
    set("spec-v1", specDir.file("parcel-delivery-v1-api.yaml").toString())
    val logSpecDir = layout.projectDirectory.dir("./parcel-delivery-api-log/src/resources/ru/parceldelivery/api.log.v1.spec")
    set("spec-log-v1", logSpecDir.file("parcel-delivery-log-v1-api.yaml").toString())
}

tasks {
    register("build") {
        group = "build"
    }
}