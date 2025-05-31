plugins {
    alias(libs.plugins.movieratings.android.library)
}

android {
    namespace = "org.sco.movieratings.shared.fake"
}

dependencies {
    api(project(":feature:shared:api"))
}