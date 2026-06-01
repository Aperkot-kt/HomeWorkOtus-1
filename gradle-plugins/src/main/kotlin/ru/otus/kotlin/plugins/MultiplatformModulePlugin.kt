package ru.otus.kotlin.buildplugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

/**
 * Convention plugin для Kotlin Multiplatform модулей.
 */
class MultiplatformModulePlugin : Plugin<Project> {

    override fun apply(project: Project) = with(project) {
        pluginManager.apply("org.jetbrains.kotlin.multiplatform")


        group = rootProject.group

        repositories.apply {
            mavenCentral()
        }

        extensions.configure<KotlinMultiplatformExtension> {
            jvmToolchain(21)

            jvm()

            sourceSets.apply {
                val commonMain = getByName("commonMain")
                commonMain.dependencies {
                    implementation("org.jetbrains.kotlin:kotlin-stdlib")
                }

                val commonTest = getByName("commonTest")
                commonTest.dependencies {
                    implementation("org.jetbrains.kotlin:kotlin-test")
                }

                val jvmTest = getByName("jvmTest")
                jvmTest.dependencies {
                    implementation("org.jetbrains.kotlin:kotlin-test-junit5")
                }
            }
        }

        tasks.apply {
            withType<Test>().configureEach {
                useJUnitPlatform()
            }
        }

        tasks.register("printKmpInfo") {
            group = "otus"
            description = "Print info about configured KMP targets"

            doLast {
                println("KMP project configured: ${project.path}")
                println("Project: ${project.path}")
                println("Dir: ${project.projectDir}")
                println("Gradle: ${project.gradle.gradleVersion}")
                println("Group: ${project.group}")
            }
        }

        println("KMP Convention Plugin applied to: ${project.path}")
    }
}