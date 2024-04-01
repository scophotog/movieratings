plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-kapt")
}

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
    namespace = "org.sco.movieratings.movielist.fake.di"
}

dependencies {
    api(project(":feature:movie_details:api"))
    api(project(":feature:movie_details:fake"))

    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)
}