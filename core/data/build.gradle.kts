plugins {
    alias(libs.plugins.movieratings.android.library)
    alias(libs.plugins.movieratings.kover)
    alias(libs.plugins.movieratings.hilt)
}

android {
    namespace = "org.sco.movieratings.core.data"
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }
}

dependencies {
    api(project(":core:common"))
    api(project(":core:db"))
    api(project(":core:network"))

    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(project(":core:testing"))
}