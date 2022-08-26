package org.sco.movieratings.usecase

import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.sco.movieratings.api.TheMovieDBService
import org.sco.movieratings.api.response.Movie
import org.sco.movieratings.api.response.MoviesResponse
import org.sco.movieratings.repository.MovieMapper

private val movie = Movie(
    title = "Movie",
    id = 1,
    posterPath = "http://poster.png",
    overview = "Overview",
    releaseDate = "2020/01/01",
    popularity = 1.0,
    voteAverage = 1.0,
    voteCount = 1
)
private val movies = listOf(movie)
private val movieResponse = MoviesResponse(
    movies = movies,
    totalMovieCount = movies.size,
    totalPageCount = 1
)

class GetPopularMoviesUseCaseTest {
    private val mapper = MovieMapper("http://img", "")

    @Test
    fun `service returns successful response`() = runBlockingTest {
        val mock = mock<TheMovieDBService> {
            on { runBlocking { getPopularMovies() } } doReturn Result.success(movieResponse)
        }
        val result = GetPopularMoviesUseCase(mock, mapper).invoke()
        assertTrue(result.isNotEmpty())
        assertEquals(movies.size, result.size)
        assertEquals(movies.first().title, result.first().title)
    }

    @Test
    fun `service returns empty list on a failure result response`() = runBlockingTest {
        val mock = mock<TheMovieDBService> {
            onBlocking { getPopularMovies() } doReturn Result.failure(Exception())
        }
        val result = GetPopularMoviesUseCase(mock, mapper).invoke()
        assertTrue(result.isEmpty())
    }
}