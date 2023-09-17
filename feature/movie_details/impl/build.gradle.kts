plugins {
    id("movieratings.android-lib")
    id("movieratings.hilt")
    id("kotlin-android")
    id("kotlin-kapt")
}

android.namespace = "org.sco.movieratings.moviedetails"

dependencies {
    api(project(":feature:movie_details:api"))
    api(project(":feature:shared:api"))
    implementation(project(":core:db:impl"))

    testImplementation(libs.junit)
    testImplementation("androidx.test:core:1.4.0")
    testImplementation(libs.mockk)
    testImplementation(project(":feature:shared:fake"))
    testImplementation("androidx.arch.core:core-testing:2.1.0")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.5.2")
    testImplementation("androidx.room:room-testing:2.4.1")
}