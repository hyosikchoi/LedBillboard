plugins {
    id("hyosik.plugin.library")
    id("hyosik.plugin.library.compose")
    id("hyosik.plugin.hilt")
}

android {
    namespace = "com.example.ledbillboard.presentation"
}

dependencies {

    implementation (project(":model"))
    implementation (project(":domain"))
    implementation (project(":core"))
    implementation(project(":core-android"))

    implementation (libs.androidX.core.ktx)
    implementation (libs.androidX.lifecycle.runtimeTesting)
    implementation (libs.androidX.activity.compose)
    implementation (libs.androidX.hilt.navigation.compose)
    testImplementation (libs.junit)
    androidTestImplementation (libs.androidX.test.junit)
    androidTestImplementation (libs.androidX.test.espresso)
    androidTestImplementation (libs.androidX.compose.uiTestJunit)
    debugImplementation (libs.androidX.compose.tooling)
    debugImplementation (libs.androidX.compose.uiTestManifest)

    // color picker
    implementation (libs.colorPicker)

}