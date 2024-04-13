plugins {
    id("hyosik.plugin.java")
}

dependencies {
    implementation (project(":model"))
    implementation(libs.kotlin.coroutine.core)
}