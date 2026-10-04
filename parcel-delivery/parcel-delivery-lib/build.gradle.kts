plugins {
    id("jvm-convention")
}

dependencies {
    testImplementation(libs.coroutines.test)
//    testImplementation(libs.kotlinx.coroutines.core)
}
