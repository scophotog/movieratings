plugins {
    alias(libs.plugins.movieratings.android.application)
    alias(libs.plugins.movieratings.android.application.compose)
    alias(libs.plugins.movieratings.hilt)
}

android {
    namespace = "org.sco.movieratings"

    defaultConfig {
        applicationId = "org.sco.movieratings"
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "org.sco.espresso.HiltTestRunner"
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
    packaging {
        resources {
            excludes.add("/META-INF/{AL2.0,LGPL2.1}")
        }
    }

}

dependencies {
    // Movie Details
//    implementation(project(":feature:movie_details:wiring"))
    implementation(project(":feature:movie_details:ui"))

    // Movie List
//    implementation(project(":feature:movie_list:wiring"))
    implementation(project(":feature:movie_list:ui"))
//    implementation(project(":feature:movie_list:ui2"))
    implementation(project(":core:designsystem"))

    // App Dependencies
    implementation(libs.androidx.activity.compose)

    // Hilt
    implementation(libs.hilt.navigation.compose)

    androidTestImplementation(project(":espresso"))
}