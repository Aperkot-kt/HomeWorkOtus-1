plugins {
    id("jvm-convention")
}

dependencies {
    implementation(project(":parcel-delivery-common"))
    implementation(libs.kotlinx.datetime)
    implementation(libs.slf4j.api)

    testImplementation(project(":parcel-delivery-api-v1"))
    testImplementation(project(":parcel-delivery-api-v1-transport-mappers"))
    testImplementation(libs.coroutines.test)
    testImplementation(libs.logback.classic)
}