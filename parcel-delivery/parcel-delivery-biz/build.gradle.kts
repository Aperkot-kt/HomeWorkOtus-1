plugins {
    id("jvm-convention")
}

dependencies {
    implementation(project(":parcel-delivery-common"))
    implementation(project(":parcel-delivery-app-common"))
    implementation(project(":parcel-delivery-lib"))
    implementation(libs.kotlinx.datetime)
    implementation(libs.slf4j.api)

    testImplementation(libs.coroutines.test)
    testImplementation(libs.logback.classic)
//    testImplementation(libs.kotlinx.coroutines.core)
}