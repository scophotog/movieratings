plugins {
    id("movieratings.android-app")
    id("kotlin-android")
}

android {

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

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }
}

val fakeImplementation by configurations
val realImplementation by configurations
