plugins {
    id("movieratings.android-lib")
    id("movieratings.hilt")
    id("kotlin-android")
    id("kotlin-kapt")
}

android {
    namespace = "org.sco.espresso"

    defaultConfig {
        testInstrumentationRunner = "org.sco.espresso.HiltTestRunner"
    }
}

dependencies {
    implementation(libs.coreKtx)
    implementation(libs.androidCoreTesting)
    implementation(libs.espresso)
    implementation(libs.hilt.android.testing)
}