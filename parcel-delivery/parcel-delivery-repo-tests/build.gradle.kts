plugins {
    id("jvm-convention")
}

dependencies {

    implementation(kotlin("test-junit5"))
    implementation(libs.coroutines.core)
    implementation(libs.coroutines.reactor)
    implementation(libs.coroutines.test)

    // Внутренние модели
    implementation(project(":parcel-delivery-common"))
    implementation(project(":parcel-delivery-app-common"))
    implementation(project(":parcel-delivery-repo-common"))
    implementation(project(":parcel-delivery-stubs"))

    // v1 api
    implementation(project(":parcel-delivery-api-v1"))
    implementation(project(":parcel-delivery-api-v1-transport-mappers"))

    // tests
    testImplementation(libs.spring.boot.starter.test)
}