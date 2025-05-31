plugins {
    alias(libs.plugins.movieratings.android.library)
    alias(libs.plugins.movieratings.hilt)
}

android {
    namespace = "org.sco.movieratings.movielist.fake.di"
}

dependencies {
    api(project(":feature:movie_list:api"))
    api(project(":feature:movie_list:fake"))
}