plugins {
    alias(libs.plugins.movieratings.android.library)
    alias(libs.plugins.movieratings.android.library.compose)
    alias(libs.plugins.movieratings.hilt)
}
android {
    namespace = "org.sco.movieratings.screenshottesting"
}

dependencies {
    api(libs.bundles.androidx.compose.ui.test)

    api(libs.app.cash.paparazzi)
    implementation(libs.androidx.compose.ui.test)
    implementation(libs.androidx.activity.compose)
    implementation(project(":core:designsystem"))
}