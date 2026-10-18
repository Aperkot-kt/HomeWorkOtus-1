plugins {
    id("jvm-convention")
}

dependencies {

    // Внутренние модели
    implementation(project(":parcel-delivery-common"))
    implementation(project(":parcel-delivery-app-common"))

    // v1 api
    implementation(project(":parcel-delivery-api-v1"))
    implementation(project(":parcel-delivery-api-v1-transport-mappers"))

    // tests
    testImplementation(kotlin("test-junit5"))
    testImplementation(libs.spring.boot.starter.test)
}
