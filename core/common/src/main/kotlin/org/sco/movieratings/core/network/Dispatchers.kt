package org.sco.movieratings.core.network

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class Dispatcher(val movieRatingDispatcher: MovieRatingDispatchers)

enum class MovieRatingDispatchers {
    Default,
    IO,
}