pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

rootProject.name = "kotlin-educational-project"

includeBuild("lessons-modules")
includeBuild("parcel-delivery")
includeBuild("gradle-plugins")