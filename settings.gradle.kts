pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }

    resolutionStrategy {
        eachPlugin {
            if (requested.id.id.startsWith("androidx.navigation.safeargs")) {
                useModule("androidx.navigation:navigation-safe-args-gradle-plugin:2.3.5")
            }
        }
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

include(":app")
include(":e2etests")
include(":espresso")
include(":core:network:impl")
include(":core:db:impl")

include(":feature:movie_details:api")
include(":feature:movie_details:impl")
include(":feature:movie_details:wiring")
include(":feature:movie_details:fake")
include(":feature:movie_details:fake-wiring")

include(":feature:movie_list:api")
include(":feature:movie_list:impl")
include(":feature:movie_list:wiring")
include(":feature:movie_list:fake")
include(":feature:movie_list:fake-wiring")
include(":feature:movie_list:demo")
include(":feature:movie_list:ui")
