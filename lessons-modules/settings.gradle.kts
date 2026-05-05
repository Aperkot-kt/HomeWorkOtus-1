pluginManagement {
    includeBuild("../gradle-plugins")

    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

rootProject.name = "lessons-modules"

include("submodule-1")
