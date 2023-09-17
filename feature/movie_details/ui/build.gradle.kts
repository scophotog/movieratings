plugins {
    id("movieratings.android-lib")
    id("movieratings.android-compose")
    id("movieratings.hilt")
    id("kotlin-android")
    id("kotlin-kapt")
}

android.namespace = "org.sco.movieratings.moviedetails.ui.moviedetails"

dependencies {
    api(project(":feature:movie_list:api"))
    api(project(":feature:movie_details:api"))

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.material)
    implementation(libs.androidx.compose.toolingPreview)
    debugImplementation(libs.androidx.compose.uiTooling)

    implementation(libs.coroutinesCore)
    implementation(libs.coroutinesAndroid)

    implementation(libs.hilt.navigation.compose)

    implementation(libs.coil)
    implementation(libs.coilCompose)
}