plugins {
    id("hyosik.plugin.library")
    id("hyosik.plugin.library.compose")
}

android {
    namespace = "com.example.ledbillboard.features.common"
}

dependencies {

    implementation (project(":model"))
    implementation (project(":core"))

    implementation (libs.androidX.core.ktx)
    implementation (libs.androidX.lifecycle.runtimeTesting)
    implementation (libs.androidX.activity.compose)
}