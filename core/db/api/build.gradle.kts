plugins {
    alias(libs.plugins.movieratings.android.library)
    alias(libs.plugins.movieratings.android.room)
    id("kotlin-parcelize")
}

android {
    namespace = "org.sco.movieratings.db.api"
}