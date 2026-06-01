pluginManagement {
    includeBuild("../gradle-plugins")

    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

rootProject.name = "parcel-delivery"

include("parcel-delivery-api")
