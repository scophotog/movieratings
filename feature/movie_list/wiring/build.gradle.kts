plugins {
    alias(libs.plugins.movieratings.android.library)
    alias(libs.plugins.movieratings.hilt)
}

android {
    namespace = "org.sco.movieratings.movielist.di"
}

dependencies {
    implementation(project(":feature:movie_list:impl"))
    implementation(project(":feature:shared:wiring"))
}