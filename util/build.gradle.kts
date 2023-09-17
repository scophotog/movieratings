plugins {
    id("movieratings.android-compose")
    id("kotlin-android")
}

android.namespace = "org.sco.movieratings.util"

dependencies {

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.foundation)
}