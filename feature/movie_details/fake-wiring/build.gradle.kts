plugins {
    alias(libs.plugins.movieratings.android.library)
    id("kotlin-kapt")
}

android {
    namespace = "org.sco.movieratings.movielist.fake.di"
}

dependencies {
    api(project(":feature:movie_details:api"))
    api(project(":feature:movie_details:fake"))

    implementation(libs.hilt.android)
//    kapt(libs.hilt.android.compiler)
}