plugins {
    alias(libs.plugins.movieratings.android.library)
    alias(libs.plugins.movieratings.hilt)
}

android {
    defaultConfig {
        testInstrumentationRunner = "org.sco.espresso.HiltTestRunner"
    }

    namespace = "org.sco.espresso"
}

dependencies {
    implementation(libs.coreKtx)
    implementation(libs.androidCoreTesting)
    implementation(libs.espresso)
//    implementation(libs.hilt.android)
//    implementation(libs.hilt.android.testing)
//    kapt(libs.hilt.android.compiler)
}