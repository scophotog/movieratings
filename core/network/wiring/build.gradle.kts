import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-kapt")
}

val apiKey = gradleLocalProperties(rootDir, providers).getProperty("API_KEY")
    ?: System.getenv("API_KEY")
    ?: "NoAPIKeyFound"

android {
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }

    buildTypes {
        getByName("release") {
            buildConfigField("String", "MOVIE_DB_API_KEY", "\"${apiKey}\"")
        }
        getByName("debug") {
            buildConfigField("String", "MOVIE_DB_API_KEY", "\"${apiKey}\"")
        }
    }
    namespace = "org.sco.movieratings.network"
}

dependencies {
    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)

    implementation(libs.retrofit)
    implementation(libs.retrofitMoshi)
    implementation(libs.loggingInterceptor)

    implementation(libs.moshiKotlin)
    kapt(libs.moshiKotlinCodegen)
}
