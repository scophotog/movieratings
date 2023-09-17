plugins {
    id("movieratings.android-lib")
    id("movieratings.hilt")
    id("kotlin-android")
    id("kotlin-kapt")
}

android.namespace = "org.sco.movieratings.shared.impl"

dependencies {
    api(project(":feature:shared:api"))
    api(project(":core:db:api"))
    implementation(project(":core:network:wiring"))


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