plugins {
    id("movieratings.android-lib")
    id("movieratings.hilt")
    id("kotlin-android")
    id("kotlin-kapt")
}

android.namespace = "org.sco.movieratings.network"

dependencies {
    implementation(libs.retrofit)
    implementation(libs.retrofitMoshi)
    implementation(libs.loggingInterceptor)

    implementation(libs.moshiKotlin)
    kapt(libs.moshiKotlinCodegen)
}
