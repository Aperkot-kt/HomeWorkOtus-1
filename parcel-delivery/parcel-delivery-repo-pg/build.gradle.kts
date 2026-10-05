plugins {
    id("jvm-convention")
}

dependencies {

    implementation("org.postgresql:postgresql:42.7.3")

    implementation("org.jetbrains.exposed:exposed-core:0.44.1")
    implementation("org.jetbrains.exposed:exposed-dao:0.44.1")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.44.1")
    implementation("org.jetbrains.exposed:exposed-java-time:0.44.1")

    implementation("org.testcontainers:testcontainers:2.0.5")
    implementation("org.testcontainers:testcontainers-postgresql:2.0.5")

    implementation("com.benasher44:uuid:0.8.4")

    // Внутренние модели
    implementation(project(":parcel-delivery-common"))
    implementation(project(":parcel-delivery-app-common"))
    implementation(project(":parcel-delivery-repo-tests"))
    implementation(project(":parcel-delivery-repo-common"))

    // v1 api
    implementation(project(":parcel-delivery-api-v1"))
    implementation(project(":parcel-delivery-api-v1-transport-mappers"))

    // tests
    testImplementation(kotlin("test-junit5"))
    testImplementation(libs.spring.boot.starter.test)
}