import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `kotlin-dsl`
}

group = "com.example.ledbillboard.buildlogic"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.ksp.gradlePlugin)
    compileOnly(libs.hilt.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("AndroidApplicationPlugin") {
            id = "hyosik.plugin.application"
            implementationClass = "AndroidApplicationConventionPlugin"
        }
        register("AndroidApplicationComposePlugin") {
            id = "hyosik.plugin.application.compose"
            implementationClass = "AndroidApplicationComposeConventionPlugin"
        }

        register("AndroidLibraryPlugin") {
            id = "hyosik.plugin.library"
            implementationClass = "AndroidLibraryConventionPlugin"
        }

        register("AndroidLibraryComposePlugin") {
            id = "hyosik.plugin.library.compose"
            implementationClass = "AndroidLibraryComposeConventionPlugin"
        }

        register("AndroidHiltPlugin") {
            id = "hyosik.plugin.hilt"
            implementationClass = "AndroidHiltConventionPlugin"
        }

        register("JavaLibraryPlugin") {
            id = "hyosik.plugin.java"
            implementationClass = "JavaLibraryConventionPlugin"
        }

    }
}
