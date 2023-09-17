import org.gradle.kotlin.dsl.dependencies

plugins {
    id("com.google.dagger.hilt.android")
    id("kotlin-kapt")
}

dependencies {
    "implementation"(libs.hilt.android)
    "kapt"(libs.hilt.android.compiler)
}

kapt {
    correctErrorTypes = true
}
