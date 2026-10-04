plugins {
    id("jvm-convention")
}

dependencies {

    implementation("io.github.reactivecircus.cache4k:cache4k:0.13.0")
    implementation("com.benasher44:uuid:0.8.4")
    implementation(libs.coroutines.core)
    implementation(libs.coroutines.reactor)

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
