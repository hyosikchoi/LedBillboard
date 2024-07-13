pluginManagement {
    includeBuild("build-logic") // 필수
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

// build-logic rebuild 시 에러 임시 해결
gradle.startParameter.excludedTaskNames.addAll(listOf(":build-logic:convention:testClasses"))

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "LedBillboard"
include (":app")
include (":data")
include (":domain")
include (":presentation")
include (":model")
include(":core")
