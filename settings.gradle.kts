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
include(":core:network:wiring")
include(":core:db:impl")
include(":core:coroutine:wiring")

include(":feature:shared:api")
include(":feature:shared:impl")
include(":feature:shared:wiring")
include(":feature:shared:fake")

include(":feature:movie_details:api")
include(":feature:movie_details:impl")
include(":feature:movie_details:wiring")
include(":feature:movie_details:fake")
include(":feature:movie_details:fake-wiring")
include(":feature:movie_details:ui")

include(":feature:movie_list:api")
include(":feature:movie_list:impl")
include(":feature:movie_list:wiring")
include(":feature:movie_list:fake")
include(":feature:movie_list:fake-wiring")
include(":feature:movie_list:demo")
include(":feature:movie_list:demo-carousel")
include(":feature:movie_list:ui")
include(":feature:movie_list:ui2")
include(":core:db:api")
include(":core:db:wiring")
include(":core:designsystem")
include(":util")


check(JavaVersion.current().isCompatibleWith(JavaVersion.VERSION_17)) {
    """
    Now in Android requires JDK 17+ but it is currently using JDK ${JavaVersion.current()}.
    Java Home: [${System.getProperty("java.home")}]
    https://developer.android.com/build/jdks#jdk-config-in-studio
    """.trimIndent()
}