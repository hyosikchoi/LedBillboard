plugins {
    id("hyosik.plugin.library")
    id("hyosik.plugin.hilt")
}

android {
    namespace = "com.example.ledbillboard.data"
}

dependencies {

    implementation (project(":model"))
    implementation (project(":domain"))

    implementation (libs.kotlin.coroutine.core)
    implementation (libs.androidX.dataStore)
    testImplementation(libs.junit)
}