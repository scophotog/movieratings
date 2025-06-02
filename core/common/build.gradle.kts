plugins {
    alias(libs.plugins.movieratings.jvm.library)
    alias(libs.plugins.movieratings.hilt)
}

dependencies {
    implementation(libs.coroutinesCore)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.turbine)
}