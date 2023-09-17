plugins {
    id("movieratings.android-lib")
    id("kotlin-android")
    id("kotlin-kapt")
    id("kotlin-parcelize")
}

android.namespace = "org.sco.movieratings.db"

dependencies {
    api(project(":core:db:api"))
    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)

    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    kapt(libs.room.compiler)

    androidTestImplementation(libs.junitAndroidExt)
    androidTestImplementation(libs.androidCoreTesting)
}
