import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    alias(libs.plugins.movieratings.android.library)
    alias(libs.plugins.movieratings.retrofit)
    alias(libs.plugins.movieratings.hilt)
}

val apiKey = gradleLocalProperties(rootDir, providers).getProperty("API_KEY")
    ?: System.getenv("API_KEY")
    ?: "NoAPIKeyFound"

android {
    buildTypes {
        getByName("release") {
            buildConfigField("String", "MOVIE_DB_API_KEY", "\"${apiKey}\"")
        }
        getByName("debug") {
            buildConfigField("String", "MOVIE_DB_API_KEY", "\"${apiKey}\"")
        }
    }
    namespace = "org.sco.movieratings.network"
    buildFeatures {
        buildConfig = true
    }
}
