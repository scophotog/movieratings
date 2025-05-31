import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.dependencies
import org.sco.movieratings.libs

class MoshiConventionPlugin: Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            apply(plugin = "com.google.devtools.ksp")

            dependencies{
                "implementation"(libs.findLibrary("retrofit").get())
                "implementation"(libs.findLibrary("retrofitMoshi").get())
                "implementation"(libs.findLibrary("loggingInterceptor").get())
                "implementation"(libs.findLibrary("moshiKotlin").get())
                "ksp"(libs.findLibrary("moshiKotlinCodegen").get())
            }
        }
    }
}