plugins {
    alias(libs.plugins.movieratings.android.feature)
    alias(libs.plugins.movieratings.android.library)
    alias(libs.plugins.movieratings.android.library.compose)
}

android {
    namespace = "org.sco.movieratings.moviedetails.ui.moviedetails"
}

dependencies {
//    api(project(":feature:movie_list:api"))
//    api(project(":feature:movie_details:api"))
    implementation(project(":core:domain"))
    implementation(libs.coil)
    implementation(libs.coilCompose)
}