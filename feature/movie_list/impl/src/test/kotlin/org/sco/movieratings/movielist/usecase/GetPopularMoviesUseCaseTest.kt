package org.sco.movieratings.movielist.usecase

import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.sco.movieratings.movielist.api.MovieListItem

@OptIn(ExperimentalCoroutinesApi::class)
class GetPopularMoviesUseCaseTest {

    @Test
    fun `service returns successful response`() = runTest {
        val mock = mock<org.sco.movieratings.shared.api.MovieListRepository> {
            on { runBlocking { getPopularMovies() } } doReturn movies
        }
        val result = GetPopularMoviesUseCase(mock).invoke()
        assertTrue(result.isNotEmpty())
        assertEquals(movies.size, result.size)
        assertEquals(movies.first().title, result.first().title)
    }

    @Test
    fun `service returns empty list on a failure result response`() = runTest {
        val mock = mock<org.sco.movieratings.shared.api.MovieListRepository> {
            on { runBlocking { getPopularMovies() } } doReturn listOf()
        }
        val result = GetPopularMoviesUseCase(mock).invoke()
        assertTrue(result.isEmpty())
    }

    companion object {
        private val movie = MovieListItem(
            title = "Movie",
            id = 1,
            posterPath = "http://poster.png"
        )
        private val movies = listOf(movie)
    }
}