plugins {
    id("jvm-convention")
}

dependencies {
    implementation(project(":parcel-delivery-api-v1"))
    implementation("com.fasterxml.jackson.core:jackson-databind:2.18.0")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.18.0")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.21.2")
    testImplementation(kotlin("test"))
}