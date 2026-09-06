plugins {
    id("jvm-convention")
}

dependencies {
    implementation(project(":parcel-delivery-api-v1"))
    implementation(projects.parcelDeliveryCommon)
    testImplementation(kotlin("test"))
}