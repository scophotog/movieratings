import com.android.build.gradle.BaseExtension
import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import org.gradle.api.JavaVersion
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Project
import org.gradle.api.artifacts.Dependency
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.sco.movieratings.buildlogic.ApkConfig
import org.sco.movieratings.buildlogic.ApkConfig.VERSION_CODE
import org.sco.movieratings.buildlogic.ApkConfig.VERSION_NAME

fun BaseExtension.commonAndroid(target: Project) {
    configureDefaultConfig(target)
    configureBuildTypes(target)
    configureBuildFeatures()
    configureCompileOptions()

    target.suppressOptIn()
}

private fun BaseExtension.configureDefaultConfig(project: Project) {
    compileSdkVersion(ApkConfig.COMPILE_SDK_VERSION)
    defaultConfig {
        minSdk = ApkConfig.MIN_SDK_VERSION
        targetSdk = ApkConfig.TARGET_SDK_VERSION
        versionCode = project.VERSION_CODE
        versionName = project.VERSION_NAME

        consumerProguardFiles(
            "consumer-rules.pro"
        )

        packagingOptions {
            resources.excludes += "META-INF/LICENSE-LGPL-2.1.txt"
            resources.excludes += "META-INF/LICENSE-LGPL-3.txt"
            resources.excludes += "META-INF/LICENSE-W3C-TEST"
            resources.excludes += "META-INF/DEPENDENCIES"
            resources.excludes += "*.proto"
        }

        testOptions {
            unitTests {
                isIncludeAndroidResources = true
            }
        }
    }
}

private fun BaseExtension.configureBuildTypes(project: Project) {
    val apiKey = gradleLocalProperties(project.rootDir).getProperty("API_KEY")
        ?: System.getenv("API_KEY")
        ?: "NoAPIKeyFound"

    buildTypes {
        defaultConfig {
            buildConfigField("String", "MOVIE_DB_API_KEY", "\"${apiKey}\"")

            javaCompileOptions {
                annotationProcessorOptions {
                    arguments["room.incremental"] = "true"
                }
            }
        }
        maybeCreate("debug").apply {
            buildConfigField("boolean", "INTERNAL", "true")
            multiDexEnabled = true
            isDebuggable = true
        }
        maybeCreate("release").apply {
            buildConfigField("boolean", "INTERNAL", "true")
        }
    }
}

private fun BaseExtension.configureBuildFeatures() {
    // TODO: Disable by default
    //  BuildConfig is java source code. Java and Kotlin at one time affect build speed.
    buildFeatures.buildConfig = true
    // Disable by default. ViewBinding needed only for few modules.
    // No need to enable this feature for all modules.
    buildFeatures.viewBinding = false
    buildFeatures.aidl = false
    buildFeatures.compose = false
    buildFeatures.prefab = false
    buildFeatures.renderScript = false
    buildFeatures.resValues = false
    buildFeatures.shaders = false
}

private fun BaseExtension.configureCompileOptions() {
    compileOptions.sourceCompatibility = JavaVersion.VERSION_11
    compileOptions.targetCompatibility = JavaVersion.VERSION_11
}

private fun Project.suppressOptIn() {
    tasks.withType<KotlinCompile>()
        .configureEach {
            kotlinOptions {
                jvmTarget = "11"

                freeCompilerArgs = freeCompilerArgs + listOf(
                    "-Xopt-in=com.google.accompanist.pager.ExperimentalPagerApi",
                    "-Xopt-in=androidx.compose.ui.ExperimentalComposeUiApi",
                    "-Xopt-in=androidx.compose.foundation.ExperimentalFoundationApi",
                    "-Xopt-in=kotlinx.serialization.ExperimentalSerializationApi",
                    "-Xopt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
                    "-Xopt-in=com.squareup.anvil.annotations.ExperimentalAnvilApi",
                    "-Xopt-in=kotlin.time.ExperimentalTime",
                    "-Xopt-in=kotlin.RequiresOptIn",
                    "-Xopt-in=androidx.compose.animation.ExperimentalAnimationApi",
                    "-Xopt-in=com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi", // ktlint-disable max-line-length
                    "-Xopt-in=androidx.compose.foundation.layout.ExperimentalLayoutApi"
                )
            }
        }
}

/**
 * Adds a dependency to the 'internalImplementation' configuration.
 *
 * @param dependencyNotation notation for the dependency to be added.
 * @return The dependency.
 *
 * @see [DependencyHandler.add]
 */
fun DependencyHandler.internalImplementation(dependencyNotation: Any): Dependency? =
    add("internalImplementation", dependencyNotation)

fun <BuildTypeT> NamedDomainObjectContainer<BuildTypeT>.debug(
    action: BuildTypeT.() -> Unit
) {
    maybeCreate("debug").action()
}

fun <BuildTypeT> NamedDomainObjectContainer<BuildTypeT>.release(
    action: BuildTypeT.() -> Unit
) {
    maybeCreate("release").action()
}