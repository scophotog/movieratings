plugins {
    id("movieratings.android-app-demo")
    id("movieratings.hilt")
    id("kotlin-android")
    id("kotlin-kapt")
}

android.namespace = "org.sco.movieratings.movielist.demo"

dependencies {
    // Demo Dependencies
    implementation(project(":feature:movie_list:ui"))
    fakeImplementation(project(":feature:movie_list:fake-wiring"))
    realImplementation(project(":feature:movie_list:wiring"))

    // Other Dependencies
    implementation(libs.coreKtx)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.activityCompose)
    implementation(libs.lifecycle.viewmodel.compose)
    implementation(libs.lifecycle.viewmodel.ktx)

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.material)
    implementation(libs.androidx.compose.toolingPreview)
    debugImplementation(libs.androidx.compose.uiTooling)

    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.hilt.navigation.compose)

    testImplementation(libs.junit)
    androidTestImplementation(libs.junitAndroidExt)
    androidTestImplementation(libs.androidCoreTesting)
}