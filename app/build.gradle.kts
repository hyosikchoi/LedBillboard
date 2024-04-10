plugins {
    id("hyosik.plugin.application")
    id("hyosik.plugin.hilt")
}

android {
    namespace = "com.example.ledbillboard"

    defaultConfig {
        applicationId = "com.example.ledbillboard"
        versionCode = 1
        versionName = "1.0.0"
    }
}

dependencies {

    implementation (project(":data"))
    implementation (project(":domain"))
    implementation (project(":presentation"))

    implementation (libs.androidX.core.ktx)
    implementation (libs.androidX.lifecycle.runtimeTesting)
    testImplementation (libs.junit)
    androidTestImplementation (libs.androidX.test.junit)
    androidTestImplementation (libs.androidX.test.espresso)

}

