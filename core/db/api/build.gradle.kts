plugins {
    id("movieratings.android-lib")
    id("kotlin-android")
    id("kotlin-kapt")
    id("kotlin-parcelize")
}

android.namespace = "org.sco.movieratings.db.api"

dependencies {

    implementation(libs.room.ktx)
    kapt(libs.room.compiler)
}