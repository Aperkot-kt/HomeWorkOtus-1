
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

include("parcel-delivery-lib")
include("parcel-delivery-biz")
include("parcel-delivery-api-v1")
include("parcel-delivery-common")
include("parcel-delivery-api-v1-mappers")
include("parcel-delivery-api-v1-transport-mappers")
include("parcel-delivery-app-common")
include("parcel-delivery-app-kafka")
include("parcel-delivery-app-spring")
include("parcel-delivery-stubs")
include("parcel-delivery-api-log")

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")