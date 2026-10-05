package ru.otus.kotlin.plugins

import org.gradle.api.Action
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.Exec

/**
 * Спецификация docker-образа, который собирается задачей `dockerBuild<name>`.
 */
open class DockerImageSpec {
    var buildContext: String = ""
    var imageName: String = ""
    var imageTag: String = "latest"
    var dockerFile: String = "Dockerfile"
}

/**
 * Реестр спецификаций docker-образов, объявленных через расширение `docker`.
 */
open class DockerImagesContainer {
    internal val specs = LinkedHashMap<String, DockerImageSpec>()

    fun register(name: String, configure: Action<DockerImageSpec>) {
        val spec = DockerImageSpec()
        configure.execute(spec)
        specs[name] = spec
    }
}

/**
 * Расширение `docker` для объявления собираемых docker-образов.
 */
open class DockerExtension {
    val images = DockerImagesContainer()
}

/**
 * Convention plugin для сборки docker-образов.
 */
class DockerModulePlugin : Plugin<Project> {

    override fun apply(project: Project) = with(project) {

        extensions.create("docker", DockerExtension::class.java)

        afterEvaluate {
            val extension = extensions.getByType(DockerExtension::class.java)
            extension.images.specs.forEach { (name, spec) ->
                val taskName = "dockerBuild" + name.replace("-", "")
                tasks.register(taskName, Exec::class.java) {
                    group = "docker"
                    description = "Build docker image ${spec.imageName}:${spec.imageTag}"
                    workingDir = project.file(spec.buildContext)
                    commandLine(
                        "docker", "build",
                        "-t", "${spec.imageName}:${spec.imageTag}",
                        "-f", spec.dockerFile,
                        ".",
                    )
                }
            }
        }

        println("Docker Convention Plugin applied to: ${project.path}")
    }
}
