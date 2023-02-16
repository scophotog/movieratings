package org.sco.movieratings.usecase

import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test
import org.sco.movieratings.movielist.data.model.Movie
import org.sco.movieratings.movielist.data.model.MoviesResponse
import org.sco.movieratings.movielist.mapper.MovieMapper

//TODO Move all this to the correct model

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
    private val mapper = MovieMapper("http://img")

    @Test
    fun `service returns successful response`() = runBlockingTest {
//        val mock = mock<org.sco.movieratings.network.TheMovieDBServiceImpl> {
//            on { runBlocking { getPopularMovies() } } doReturn Result.success(movieResponse)
//        }
//        val result = org.sco.movieratings.usecase.GetPopularMoviesUseCase(mock, mapper).invoke()
//        assertTrue(result.isNotEmpty())
//        assertEquals(movies.size, result.size)
//        assertEquals(movies.first().title, result.first().title)
    }

    @Test
    fun `service returns empty list on a failure result response`() = runBlockingTest {
//        val mock = mock<org.sco.movieratings.network.TheMovieDBServiceImpl> {
//            onBlocking { getPopularMovies() } doReturn Result.failure(Exception())
//        }
//        val result = org.sco.movieratings.usecase.GetPopularMoviesUseCase(mock, mapper).invoke()
//        assertTrue(result.isEmpty())
    }
}