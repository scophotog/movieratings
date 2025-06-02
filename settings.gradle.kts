pluginManagement {
    includeBuild("build-logic")

    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode = RepositoriesMode.FAIL_ON_PROJECT_REPOS
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
    }
}

rootProject.name = "movieratings"

include(":app")
include(":espresso")
include(":core:db")
include(":core:network")
include(":core:designsystem")
include(":core:testing")
include(":core:screenshot-testing")
include(":core:common")
include(":core:model")
include(":core:data")
include(":core:domain")

//include(":feature:shared:fake")

include(":feature:movie_details:ui")


include(":feature:movie_list:demo")
include(":feature:movie_list:demo-carousel")
include(":feature:movie_list:ui")
include(":feature:movie_list:ui2")

include(":util")


check(JavaVersion.current().isCompatibleWith(JavaVersion.VERSION_17)) {
    """
    Now in Android requires JDK 17+ but it is currently using JDK ${JavaVersion.current()}.
    Java Home: [${System.getProperty("java.home")}]
    https://developer.android.com/build/jdks#jdk-config-in-studio
    """.trimIndent()
}


