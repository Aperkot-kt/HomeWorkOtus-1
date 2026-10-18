plugins {
    id("jvm-convention")
}

dependencies {
    implementation(project(":parcel-delivery-common"))
    implementation(project(":parcel-delivery-app-common"))
    implementation(project(":parcel-delivery-lib"))
    implementation(project(":parcel-delivery-api-log"))
    implementation(libs.kotlinx.datetime)
    implementation(libs.slf4j.api)

    testImplementation(libs.coroutines.test)
    testImplementation(libs.logback.classic)

}