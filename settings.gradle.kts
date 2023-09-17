enableFeaturePreview("STABLE_CONFIGURATION_CACHE")

pluginManagement {
    includeBuild("build-logic")

    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "MovieRatingsApp"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

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
include(":util")
include(":plugins:convention")
