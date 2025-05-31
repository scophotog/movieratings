plugins {
    alias(libs.plugins.movieratings.android.application)
    alias(libs.plugins.movieratings.android.application.compose)
    alias(libs.plugins.movieratings.hilt)
}

android {
    namespace = "org.sco.movieratings.movielist.democarousel"

    defaultConfig {
        applicationId = "org.sco.movieratings.movielist.democarousel"
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

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}
val fakeImplementation by configurations
val realImplementation by configurations
dependencies {
    // Demo Dependencies
    implementation(project(":feature:movie_list:ui2"))
    fakeImplementation(project(":feature:movie_list:fake-wiring"))
    realImplementation(project(":feature:movie_list:wiring"))

    implementation(project(":core:designsystem"))

    implementation(libs.androidx.activity.compose)

    testImplementation(libs.junit)
    androidTestImplementation(libs.junitAndroidExt)
    androidTestImplementation(libs.androidCoreTesting)
    androidTestImplementation(libs.androidx.test.core)
    androidTestImplementation(libs.androidx.test.core.ktx)
}