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
    namespace = "org.sco.movieratings.shared.impl"
}

dependencies {
    api(project(":feature:shared:api"))
    api(project(":core:db:api"))
    implementation(project(":core:network:wiring"))


    implementation(libs.retrofit)
    implementation(libs.retrofitMoshi)
    kapt(libs.moshiKotlinCodegen)
    implementation(libs.moshiKotlin)

    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    kapt(libs.room.compiler)

    implementation(libs.coroutinesCore)
    implementation(libs.coroutinesAndroid)
    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)
}