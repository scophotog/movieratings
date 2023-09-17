plugins {
    id("movieratings.android-app")
    id("movieratings.hilt")
    id("kotlin-kapt")
}

android {
    namespace = "org.sco.movieratings"

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }
}

dependencies {
    // Movie Details
    implementation(project(":feature:movie_details:wiring"))
    implementation(project(":feature:movie_details:ui"))

    // Movie List
    implementation(project(":feature:movie_list:wiring"))
    implementation(project(":feature:movie_list:ui"))
//    implementation(project(":feature:movie_list:ui2"))

    // App Dependencies
    implementation(libs.coreKtx)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.lifecycle.livedata.ktx)

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.material)
    implementation(libs.androidx.compose.toolingPreview)
    debugImplementation(libs.androidx.compose.uiTooling)

    // Navigation
    implementation(libs.navigationCompose)

    implementation(libs.activityCompose)
    implementation(libs.lifecycle.viewmodel.compose)
    implementation(libs.lifecycle.viewmodel.ktx)

    // Hilt
    implementation(libs.hilt.navigation.compose)

    // Coil
    implementation(libs.coil)
    implementation(libs.coilCompose)
    testImplementation(libs.junit)


    androidTestImplementation(project(":espresso"))
}