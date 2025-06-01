plugins {
    alias(libs.plugins.movieratings.android.library)
    alias(libs.plugins.movieratings.hilt)
}
android {
    namespace = "org.sco.movieratings.testing"
}

dependencies {
    api(libs.kotlinx.coroutines.test)

    implementation(libs.androidx.test.rules)
    implementation(libs.hilt.android.testing)
}