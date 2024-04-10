import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.plugin.KotlinBasePluginWrapper
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

class JavaLibraryConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("java-library")
                apply("org.jetbrains.kotlin.jvm")
                apply("kotlin")
            }

            extensions.configure<JavaPluginExtension>
            {
                sourceCompatibility = JavaVersion.VERSION_17
                targetCompatibility = JavaVersion.VERSION_17
            }

            project.plugins.forEach { plugin ->
                if (plugin is KotlinBasePluginWrapper) {
                    project.afterEvaluate {
                        project.tasks.withType(KotlinJvmCompile::class.java).configureEach {
                            kotlinOptions.jvmTarget = JavaVersion.VERSION_17.toString()
                        }
                    }
                }
            }
        }
    }
}