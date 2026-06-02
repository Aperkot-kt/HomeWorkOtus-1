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
    set("spec-v1", specDir.file("parcel-deliveiry-v1-api.yaml").toString())
}

tasks {
    register("build") {
        group = "build"
    }
}