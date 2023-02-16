package org.sco.movieratings.moviedetails.usecase

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.sco.movieratings.moviedetails.api.MovieDetailItem
import org.sco.movieratings.moviedetails.api.MovieDetailsRepository
import org.sco.movieratings.moviedetails.api.MoviePreviewItem

@OptIn(ExperimentalCoroutinesApi::class)
class GetMoviePreviewsTest {

    @Test
    fun `previews returns with successful result`() = runTest {
        val mockMoviePreview: MoviePreviewItem = mock()
        val moviePreviews = listOf(mockMoviePreview)
        val mock = mock<MovieDetailsRepository> {
            on { runBlocking { getMoviePreviews(any()) } } doReturn moviePreviews
            on { runBlocking { getMovie(any()) } } doReturn MovieDetailItem(
                id = 0,
                previewList = moviePreviews
            )
        }
        val result: MovieDetailItem = GetMovieUseCase(mock).invoke(0)
        assertTrue(result.previewList.isNotEmpty())
    }

    @Test
    fun `no previews returned on failure`() = runTest {
        val moviePreviews = emptyList<MoviePreviewItem>()
        val mock = mock<MovieDetailsRepository> {
            on { runBlocking { getMoviePreviews(any()) } } doReturn moviePreviews
            on { runBlocking { getMovie(any()) } } doReturn MovieDetailItem(
                id = 0,
                previewList = moviePreviews
            )
        }
        val result: MovieDetailItem = GetMovieUseCase(mock).invoke(0)
        assertTrue(result.previewList.isEmpty())
    }
}