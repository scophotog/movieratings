plugins {
    id("com.android.application")
    id("kotlin-android")
    id("dagger.hilt.android.plugin")
    id("kotlin-kapt")
}

android {
    namespace = "org.sco.movieratings.movielist.demo"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "org.sco.movieratings.movielist.demo"
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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

    flavorDimensions.add("version")

    // Wow can use fake or real data implementations
    productFlavors {
        create("fake") {
            dimension = "version"
            applicationIdSuffix = ".fake"
            versionNameSuffix = "-demo"
        }
        create("real") {
            dimension = "version"
            applicationIdSuffix = ".real"
            versionNameSuffix = "-real"
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }

    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}
val fakeImplementation by configurations
val realImplementation by configurations
dependencies {
    implementation(project(":feature:movie_list:ui"))
    fakeImplementation(project(":feature:movie_list:fake"))
    fakeImplementation(project(":feature:movie_list:fake-wiring"))
    realImplementation(project(":feature:movie_list:impl"))
    realImplementation(project(":feature:movie_list:wiring"))

    implementation(libs.coreKtx)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.activityCompose)
    implementation(libs.viewmodelCompose)
    implementation(libs.viewmodelKtx)

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.material)
    implementation(libs.androidx.compose.toolingPreview)
    debugImplementation(libs.androidx.compose.uiTooling)

    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.hilt.navigation.compose)
    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)

    testImplementation(libs.junit)
    androidTestImplementation(libs.junitAndroidExt)
    androidTestImplementation(libs.androidCoreTesting)
}