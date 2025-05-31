plugins {
    alias(libs.plugins.movieratings.android.feature)
    alias(libs.plugins.movieratings.android.library)
    alias(libs.plugins.movieratings.android.library.compose)
    alias(libs.plugins.movieratings.hilt)

}

android {
    defaultConfig {
        testInstrumentationRunner = "org.sco.espresso.HiltTestRunner"
    }

    namespace = "org.sco.movieratings.movielist.ui.movielist"
}

dependencies {
    api(project(":feature:movie_list:api"))
    api(project(":feature:shared:api"))
    implementation(project(":util"))

    implementation(project(":core:designsystem"))

    implementation(libs.coil)
    implementation(libs.coilCompose)

    // Testing
    testImplementation(libs.junit)

    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.junitAndroidExt)
    androidTestImplementation(libs.androidCoreTesting)
    androidTestImplementation(libs.espresso)
    androidTestImplementation(libs.hilt.android.testing)
    androidTestImplementation(project(":espresso"))
}