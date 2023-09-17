plugins {
    id("movieratings.android-compose")
    id("movieratings.hilt")
    id("kotlin-android")
    id("kotlin-kapt")
}

android.namespace = "org.sco.movieratings.movielist.ui.movielist"

dependencies {
    api(project(":feature:movie_list:api"))
    api(project(":feature:shared:api"))

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.material)
    implementation(libs.androidx.compose.toolingPreview)
    debugImplementation(libs.androidx.compose.uiTooling)

    implementation(libs.coroutinesCore)
    implementation(libs.coroutinesAndroid)
    
    implementation(libs.hilt.navigation.compose)

    implementation(libs.coil)
    implementation(libs.coilCompose)

    // Testing
    debugImplementation(libs.androidx.compose.test.manifest)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.test.junit4)
    androidTestImplementation(libs.junitAndroidExt)
    androidTestImplementation(libs.androidCoreTesting)
    androidTestImplementation(libs.espresso)
    androidTestImplementation(libs.hilt.android.testing)
    kaptAndroidTest(libs.hilt.android.compiler)
    androidTestImplementation(project(":espresso"))
}