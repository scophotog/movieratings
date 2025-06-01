plugins {
    alias(libs.plugins.movieratings.android.library)
    alias(libs.plugins.movieratings.hilt)
    alias(libs.plugins.movieratings.kover)
}

android {
    namespace = "org.sco.movieratings.movielist"
}

dependencies {
    api(project(":feature:shared:api"))
    api(project(":feature:movie_list:api"))
    implementation(project(":core:db:impl"))

    testImplementation(project(":core:testing"))
    testImplementation(libs.mockk)
    testImplementation(project(":feature:shared:fake"))
}