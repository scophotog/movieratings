plugins {
    alias(libs.plugins.movieratings.android.library)
    alias(libs.plugins.movieratings.android.room)
    alias(libs.plugins.movieratings.hilt)
    id("kotlin-parcelize")
}

android {
    namespace = "org.sco.movieratings.db"
}
dependencies {
    api(project(":core:model"))

    androidTestImplementation(libs.androidx.test.core)
    androidTestImplementation(libs.androidx.test.runner)
    androidTestImplementation(libs.kotlinx.coroutines.test)
}
