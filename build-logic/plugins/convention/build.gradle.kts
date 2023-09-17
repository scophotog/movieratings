plugins {
    `kotlin-dsl`
}

dependencies {
    implementation(libs.android.gradle.plugin)
    implementation(libs.kotlin.plugin)
    implementation(libs.kotlin.ksp.gradle)
    implementation(libs.square.anvil.gradle)
    implementation(libs.hilt.android.gradle)

    implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
}