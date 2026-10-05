plugins {
    id("jvm-convention")
    id("maven-publish")
}

group = "ru.parceldelivery"
version = "0.0.1"

base {
    archivesName.set("dcompose")
}

allprojects {
    repositories {
        mavenCentral()
    }
}

subprojects {
    group = rootProject.group
    version = rootProject.version
}

val migrationProject = project.findProject(":parcel-delivery-other:pd-migration-pg")
    ?: project.findProject(":pd-migration-pg")

tasks {
    register("buildInfra") {
        group = "build"
        if (migrationProject != null) {
            dependsOn(migrationProject.getTasksByName("buildImages", false))
        }
    }
}
