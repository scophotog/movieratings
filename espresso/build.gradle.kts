plugins {
    id("com.android.library")
    id("kotlin-android")
    id("dagger.hilt.android.plugin")
    id("kotlin-kapt")
}

android {
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()

        testInstrumentationRunner = "org.sco.espresso.HiltTestRunner"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    namespace = "org.sco.espresso"
}

dependencies {
    implementation(libs.coreKtx)
    implementation(libs.androidCoreTesting)
    implementation(libs.espresso)
    implementation(libs.hilt.android)
    implementation(libs.hilt.android.testing)
    kapt(libs.hilt.android.compiler)
}