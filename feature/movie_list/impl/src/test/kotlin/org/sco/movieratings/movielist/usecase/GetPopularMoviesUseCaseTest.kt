package org.sco.movieratings.movielist.usecase

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.sco.movieratings.shared.api.MovieListRepository
import org.sco.movieratings.shared.fake.repository.FakeRepository

class GetPopularMoviesUseCaseTest {

    @Test
    fun `service returns successful response - fake`() = runTest {
        val fakeRepository = FakeRepository()
        val result = GetPopularMoviesUseCase(fakeRepository).invoke()
        assertTrue(result.isNotEmpty())
        assertEquals(3, result.size)
        assertEquals("Fake", result.first().title)
    }

    @Test
    fun `service returns empty list - mockk`() = runTest {
        val mock = mockk<MovieListRepository> {
            coEvery { getPopularMovies() } returns listOf()
        }
        val result = GetPopularMoviesUseCase(mock).invoke()
        assertTrue(result.isEmpty())
    }
}