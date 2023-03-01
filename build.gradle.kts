// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    extra.apply {
        set("minSdkVersion", libs.versions.minSdk.get().toInt())
        set("targetSdkVersion", libs.versions.targetSdk.get().toInt())
        set("compileSdkVersion", libs.versions.compileSdk.get().toInt())
    }
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath(libs.android.gradle.plugin)
        classpath(libs.kotlin.plugin)
        classpath(libs.hilt.android.gradle)
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}