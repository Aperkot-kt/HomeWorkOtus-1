
pluginManagement {
    includeBuild("../gradle-plugins")

    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

rootProject.name = "parcel-delivery"

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            from(files("../gradle-plugins/gradle/libs.versions.toml"))
        }
    }
}


include("parcel-delivery-api-v1")
include("parcel-delivery-common")
include("parcel-delivery-api-v1-mappers")
include("parcel-delivery-api-v1-transport-mappers")

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")