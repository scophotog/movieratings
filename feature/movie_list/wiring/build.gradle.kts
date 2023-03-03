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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(project(":core:db:impl"))
    implementation(project(":core:network:wiring"))
    implementation(project(":feature:movie_list:impl"))
    implementation(project(":feature:shared:wiring"))

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