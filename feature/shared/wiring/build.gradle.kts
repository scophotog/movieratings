plugins {
    alias(libs.plugins.movieratings.android.library)
    alias(libs.plugins.movieratings.retrofit)
    alias(libs.plugins.movieratings.android.room)
    alias(libs.plugins.movieratings.hilt)

}

android {
    namespace = "org.sco.movieratings.shared.wiring"
}

dependencies {
    implementation(project(":core:db:impl"))
    implementation(project(":core:db:wiring"))
    implementation(project(":core:network:wiring"))
    implementation(project(":feature:shared:impl"))
}