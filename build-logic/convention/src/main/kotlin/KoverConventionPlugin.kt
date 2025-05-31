import com.android.build.gradle.LibraryExtension
import kotlinx.kover.gradle.plugin.dsl.KoverProjectExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType

class KoverConventionPlugin: Plugin<Project> {
    private val excludedFromCoverage = listOf(
        "dagger.hilt.*",
        "hilt_aggregated_deps.*",
        "*_Factory",
        "*_Factory\$"
    )

    private val excludedAnnotations = listOf(
        "dagger.Module",
        "dagger.internal.DaggerGenerated",
        "javax.annotation.Generated",
    )
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = "org.jetbrains.kotlinx.kover")

            val androidExtension = extensions.getByType<LibraryExtension>()

            androidExtension.buildTypes.configureEach {
                enableAndroidTestCoverage = true
                enableUnitTestCoverage = true
            }

            extensions.configure<KoverProjectExtension> {
                reports {
                    filters {
                        includes {
                            packages("org.sco.movieratings.*")
                        }
                        excludes {
                            classes.addAll(excludedFromCoverage)
                            androidGeneratedClasses()
                            annotatedBy.addAll(excludedAnnotations)
                        }
                    }
                }
            }
        }
    }
}