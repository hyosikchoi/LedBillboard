plugins {
    id("hyosik.plugin.library")
}

android {
    namespace = "com.example.ledbillboard.data"
}

dependencies {

    implementation (project(":domain"))

    implementation (libs.kotlin.coroutine.core)
    testImplementation(libs.junit)
}