plugins {
    alias(libs.plugins.movieratings.android.library)
    alias(libs.plugins.movieratings.android.room)
    id("kotlin-parcelize")
}

android {
    namespace = "org.sco.movieratings.db"
}
dependencies {
    api(project(":core:db:api"))

    androidTestImplementation(libs.junitAndroidExt)
    androidTestImplementation(libs.androidCoreTesting)
    androidTestImplementation(libs.androidx.test.core)
    androidTestImplementation(libs.androidx.test.core.ktx)
}
