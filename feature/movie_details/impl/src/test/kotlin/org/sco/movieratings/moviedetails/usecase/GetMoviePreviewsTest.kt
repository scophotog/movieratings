package org.sco.movieratings.moviedetails.usecase

import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Test
import org.sco.movieratings.shared.api.MovieListItem
import org.sco.movieratings.shared.api.MovieListRepository
import org.sco.movieratings.shared.api.MoviePreviewItem
import org.sco.movieratings.shared.fake.repository.FakeRepository
import org.sco.movieratings.shared.fake.repository.FakeRepository.Companion.MOVIE_WITH_NO_PREVIEWS
import org.sco.movieratings.shared.fake.repository.FakeRepository.Companion.MOVIE_WITH_PREVIEWS

@OptIn(ExperimentalCoroutinesApi::class)
class GetMoviePreviewsTest {

    @Test
    fun `previews returns with successful result - with fake`() = runTest {
        val fakeRepository = FakeRepository()
        val result: MovieListItem = GetMovieUseCase(fakeRepository).invoke(MOVIE_WITH_PREVIEWS)
        assertTrue(result.previewList.isNotEmpty())
    }

    @Test
    fun `no previews returned - with fake`() = runTest {
        val fakeRepository = FakeRepository()
        val result: MovieListItem = GetMovieUseCase(fakeRepository).invoke(MOVIE_WITH_NO_PREVIEWS)
        assertTrue(result.previewList.isEmpty())
    }

    @Test
    fun `previews returns with successful result - with mock`() = runTest {
        val mockMoviePreview = mockk<MoviePreviewItem>()
        val moviePreviews = listOf(mockMoviePreview)
        val mock = mockk<MovieListRepository> {
            every { runBlocking { getMoviePreviews(any()) } } returns moviePreviews
            every { runBlocking { getMovie(any()) } } returns MovieListItem(
                id = 0,
                previewList = moviePreviews.toMutableList(),
                posterPath = null,
                title = "mock"
            )
        }
        val result: MovieListItem = GetMovieUseCase(mock).invoke(0)
        assertTrue(result.previewList.isNotEmpty())
    }

    @Test
    fun `no previews returned - with mock`() = runTest {
        val moviePreviews = emptyList<MoviePreviewItem>()
        val mock = mockk<MovieListRepository> {
            every { runBlocking { getMoviePreviews(any()) } } returns moviePreviews
            every { runBlocking { getMovie(any()) } } returns MovieListItem(
                id = 0,
                previewList = moviePreviews.toMutableList(),
                posterPath = null,
                title = "mock"
            )
        }
        val result: MovieListItem = GetMovieUseCase(mock).invoke(0)
        assertTrue(result.previewList.isEmpty())
    }
}