rootProject.name = "parcel-delivery-other"

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            from(files("../../gradle-plugins/gradle/libs.versions.toml"))
        }
    }
}

pluginManagement {
    includeBuild("../../gradle-plugins")
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

include(":pd-migration-pg")
