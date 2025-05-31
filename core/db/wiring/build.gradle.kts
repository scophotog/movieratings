plugins {
    alias(libs.plugins.movieratings.android.library)
    alias(libs.plugins.movieratings.android.room)
    alias(libs.plugins.movieratings.hilt)
}

android {
    namespace = "org.sco.movieratings.db.wiring"
}

dependencies {
    implementation(project(":core:db:impl"))
}