package com.example.ledbillboard

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import java.util.*

internal fun Project.configureAndroidCompose(
    commonExtension: CommonExtension<*, *, *, *, *>,
) {
    val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

    commonExtension.apply {
        buildFeatures.compose = true

        composeOptions {
            kotlinCompilerExtensionVersion =  libs.findVersion("composeCompiler").get().toString()
        }
    }

    dependencies {
        // Disabling to work with Alpha
//        "api"(platform(libs.findLibrary("compose.bom").get()))
        "implementation"(libs.findBundle("androidX-compose").get())
        "debugImplementation"(libs.findBundle("androidX-compose-debug").get())
    }
}