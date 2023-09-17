import com.android.build.gradle.BaseExtension

plugins {
    id("movieratings.android-lib")
}

configure<BaseExtension> {
    buildFeatures.compose = true

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }
}