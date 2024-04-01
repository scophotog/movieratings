plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-parcelize")
    id("dagger.hilt.android.plugin")
    id("kotlin-kapt")
}

android {
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "org.sco.movieratings"
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "org.sco.espresso.HiltTestRunner"

        kapt {
            arguments {
                arg("room.schemaLocation", "$projectDir/schemas")
            }
        }
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
        freeCompilerArgs = listOf(
            "-Xallow-result-return-type",
            "-Xopt-in=kotlin.RequiresOptIn",
            "-Xopt-in=kotlinx.coroutines.ExperimentalCoroutinesApi"
        )
    }
    namespace = "org.sco.movieratings"

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
    implementation(libs.lifecycle.extensions)
    implementation(libs.lifecycle.livedata.ktx)

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.material)
    implementation(libs.androidx.compose.toolingPreview)
    debugImplementation(libs.androidx.compose.uiTooling)

    // Navigation
    implementation(libs.navigationCompose)

    implementation(libs.activityCompose)
    implementation(libs.viewmodelCompose)
    implementation(libs.viewmodelKtx)

    // Hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)
    implementation(libs.hilt.navigation.compose)

    // Coil
    implementation(libs.coil)
    implementation(libs.coilCompose)
    testImplementation(libs.junit)


    androidTestImplementation(project(":espresso"))
}