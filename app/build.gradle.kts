import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-parcelize")
    id("dagger.hilt.android.plugin")
    id("kotlin-kapt")
}

var apiKey = gradleLocalProperties(rootDir).getProperty("API_KEY")
    ?: System.getenv("API_KEY")
    ?: "NoAPIKeyFound"

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

    buildTypes {
        getByName("release") {
            buildConfigField("String", "MOVIE_DB_API_KEY", "\"${apiKey}\"")
        }
        getByName("debug") {
            buildConfigField("String", "MOVIE_DB_API_KEY", "\"${apiKey}\"")
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
        freeCompilerArgs = listOf(
            "-Xallow-result-return-type",
            "-Xopt-in=kotlin.RequiresOptIn",
            "-Xopt-in=kotlinx.coroutines.ExperimentalCoroutinesApi"
        )
    }

}

dependencies {
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

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.retrofitMoshi)
    implementation(libs.loggingInterceptor)
    kapt(libs.moshiKotlinCodegen)
    implementation(libs.moshiKotlin)

    // Room
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    kapt(libs.room.compiler)

    // Hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)
    implementation(libs.hilt.navigation.compose)

    // Coil
    implementation(libs.coil)
    implementation(libs.coilCompose)
    testImplementation(libs.junit)

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