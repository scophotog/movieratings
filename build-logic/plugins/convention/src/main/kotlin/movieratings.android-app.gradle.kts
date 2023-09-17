import com.android.build.gradle.BaseExtension
import org.sco.movieratings.buildlogic.ApkConfig

plugins {
    id("com.android.application")
    id("kotlin-android")
}

configure<BaseExtension> {
    commonAndroid(project)

    defaultConfig {
        applicationId = ApkConfig.APPLICATION_ID
    }

    buildTypes {
        release {
            isShrinkResources = true
            isMinifyEnabled = true
            consumerProguardFile(
                "proguard-rules.pro"
            )
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
}