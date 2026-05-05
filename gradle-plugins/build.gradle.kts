plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
}

group = "ru.otus.kotlin.plugins"

repositories {
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    implementation(libs.kotlin.gradle.plugin)
}

gradlePlugin {
    plugins {
        create("jvm-convention") {
            id = "jvm-convention"
            implementationClass = "ru.otus.kotlin.plugins.JvmModulePlugin"
        }
        create("multiplatform-convention") {
            id = "multiplatform-convention"
            implementationClass = "ru.otus.kotlin.plugins.MultiplatformModulePlugin"
        }
    }
}