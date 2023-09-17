plugins {
    id("movieratings.android-lib")
    id("movieratings.hilt")
    id("kotlin-android")
    id("kotlin-kapt")
}

android.namespace = "org.sco.movieratings.movielist.fake.di"

dependencies {
    api(project(":feature:movie_list:api"))
    api(project(":feature:movie_list:fake"))
}