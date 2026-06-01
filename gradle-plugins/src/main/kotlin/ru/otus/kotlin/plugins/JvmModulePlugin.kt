package ru.otus.kotlin.plugins

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

/**
 * Convention plugin для JVM модулей.
 */
class JvmModulePlugin : Plugin<Project> {

    override fun apply(project: Project) = with(project) {

        pluginManager.apply("org.jetbrains.kotlin.jvm")
        group = rootProject.group

        repositories.apply {
            mavenCentral()
        }

        extensions.configure<KotlinJvmProjectExtension> {
            jvmToolchain(21)
            compilerOptions {
                freeCompilerArgs.add("-Xjsr305=strict")
                jvmTarget.set(JvmTarget.JVM_21)
            }
        }

        dependencies {
            add("implementation", "org.jetbrains.kotlin:kotlin-stdlib")
            add("testImplementation", "org.jetbrains.kotlin:kotlin-test")
            add("testImplementation", "org.jetbrains.kotlin:kotlin-test-junit5")
        }

        tasks.apply {
            withType<KotlinCompile>().configureEach {
                compilerOptions {
                    jvmTarget.set(JvmTarget.JVM_21)
                }
            }

            withType<Test>().configureEach {
                useJUnitPlatform()
            }
        }

        tasks.register("printJvmInfo") {
            group = "otus"
            description = "Print info about JVM project configuration"

            doLast {
                println("JVM project configured: ${project.path}")
                println("Project: ${project.path}")
                println("Dir: ${project.projectDir}")
                println("Gradle: ${project.gradle.gradleVersion}")
                println("Group: ${project.group}")
            }
        }

        println("JVM Convention Plugin applied to: ${project.path}")
    }
}