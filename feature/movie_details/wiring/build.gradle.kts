plugins {
    alias(libs.plugins.movieratings.android.library)
    alias(libs.plugins.movieratings.hilt)
}

android {
    namespace = "org.sco.movieratings.moviedetails.di"
}

dependencies {
    implementation(project(":feature:movie_details:impl"))
    implementation(project(":feature:shared:wiring"))
}