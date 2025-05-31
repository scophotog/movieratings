plugins {
    alias(libs.plugins.movieratings.android.library)
    alias(libs.plugins.movieratings.retrofit)
    alias(libs.plugins.movieratings.hilt)

}

android {
    namespace = "org.sco.movieratings.shared.impl"
}

dependencies {
    api(project(":feature:shared:api"))
    api(project(":core:db:api"))
    implementation(project(":core:network:wiring"))
}