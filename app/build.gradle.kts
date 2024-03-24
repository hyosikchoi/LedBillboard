plugins {
    id("hyosik.plugin.application")
    id("hyosik.plugin.application.compose")
}

apply {
    from(rootProject.file("android.gradle"))
}

dependencies {

    implementation (libs.androidX.core.ktx)
    implementation (libs.androidX.lifecycle.runtimeTesting)
    implementation (libs.androidX.activity.compose)
    testImplementation (libs.junit)
    androidTestImplementation (libs.androidX.test.junit)
    androidTestImplementation (libs.androidX.test.espresso)
    androidTestImplementation (libs.androidX.compose.uiTestJunit)
    debugImplementation (libs.androidX.compose.tooling)
    debugImplementation (libs.androidX.compose.uiTestManifest)

    // color picker
    implementation (libs.colorPicker)

    // system bar
    implementation (libs.accompanist.system.uicontroller)

}

//kapt {
//    useBuildCache = true
//}