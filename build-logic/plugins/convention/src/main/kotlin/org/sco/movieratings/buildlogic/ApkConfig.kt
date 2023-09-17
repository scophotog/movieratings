package org.sco.movieratings.buildlogic

import org.gradle.api.Project

object ApkConfig {
    const val APPLICATION_ID = "org.sco.movieratings"

    const val MIN_SDK_VERSION = 23
    const val TARGET_SDK_VERSION = 34
    const val COMPILE_SDK_VERSION = 34

    private const val DEBUG_VERSION = "DEBUG_VERSION"
    val Project.VERSION_CODE
        get() = prop("version_code", Integer.MAX_VALUE).toInt()
    val Project.VERSION_NAME
        get() = prop("version_name", DEBUG_VERSION)
}

internal fun Project.prop(key: String, default: Any): String {
    return providers.gradleProperty(key).getOrElse(default.toString())
}