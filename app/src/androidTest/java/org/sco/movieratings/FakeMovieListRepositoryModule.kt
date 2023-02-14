package org.sco.movieratings

import dagger.Module
import dagger.Provides
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.testing.TestInstallIn
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.sco.movieratings.api.response.MoviePreview
import org.sco.movieratings.api.response.Review
import org.sco.movieratings.db.MovieSchema
import org.sco.movieratings.di.MovieModule
import org.sco.movieratings.movielist.MovieListType
import org.sco.movieratings.repository.IMovieRepository
import javax.inject.Singleton

@TestInstallIn(components = [ViewModelComponent::class], replaces = [MovieModule::class])
@Module
object FakeMovieListRepositoryModule {

    val fakeMovies = listOf(MovieSchema.mock(),MovieSchema.mock(),MovieSchema.mock(),MovieSchema.mock())

    @Singleton
    @Provides
    fun provideFakeMovieListRepository() = object : IMovieRepository {
        override fun getMovieList(listType: MovieListType): Flow<List<MovieSchema>> {
            return flowOf(fakeMovies)
        }

        override fun getMovie(movieId: Int): Flow<MovieSchema?> {
            return flowOf(MovieSchema.mock())
        }

        override fun getPopularMovies(refresh: Boolean): Flow<List<MovieSchema>> {
            return flowOf(fakeMovies)
        }

        override fun getTopRatedMovies(refresh: Boolean): Flow<List<MovieSchema>> {
            return flowOf(fakeMovies)
        }

        override fun getFavoriteMovies(): Flow<List<MovieSchema>> {
            return flowOf(fakeMovies)
        }

        override fun getMovieReviews(movieId: Int): Flow<List<Review>> {
            TODO("Not yet implemented")
        }

        override fun getMoviePreviews(movieId: Int): Flow<List<MoviePreview>> {
            TODO("Not yet implemented")
        }

        override suspend fun isFavorite(movieSchema: MovieSchema): Boolean {
            TODO("Not yet implemented")
        }

        override suspend fun addToFavorites(movieSchema: MovieSchema) {
            TODO("Not yet implemented")
        }

        override suspend fun removeFromFavorites(movie: MovieSchema) {
            TODO("Not yet implemented")
        }

    }
}