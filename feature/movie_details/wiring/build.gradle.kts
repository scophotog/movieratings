plugins {
    id("movieratings.android-lib")
    id("movieratings.hilt")
    id("kotlin-android")
    id("kotlin-kapt")
}

android.namespace = "org.sco.movieratings.moviedetails.di"

dependencies {
    implementation(project(":core:db:impl"))
    implementation(project(":core:network:wiring"))
    implementation(project(":feature:movie_details:impl"))
    implementation(project(":feature:shared:wiring"))


    implementation(libs.retrofit)
    implementation(libs.retrofitMoshi)
    kapt(libs.moshiKotlinCodegen)
    implementation(libs.moshiKotlin)

    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    kapt(libs.room.compiler)

    implementation(libs.coroutinesCore)
    implementation(libs.coroutinesAndroid)
}