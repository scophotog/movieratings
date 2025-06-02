plugins {
    alias(libs.plugins.movieratings.android.library)
    alias(libs.plugins.movieratings.kover)
    id("com.google.devtools.ksp")
}

android {
    namespace = "org.sco.movieratings.core.domain"
}

dependencies {
    api(project(":core:data"))
    api(project(":core:model"))

    implementation(libs.coroutinesCore)
    implementation(libs.javax.inject)

    testImplementation(project(":core:testing"))
}