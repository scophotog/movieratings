package org.sco.movieratings.usecase

import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock

class GetMoviePreviewsTest {

    @Test
    fun `previews returns with successful result`() = runBlockingTest {
        val mockMoviePreview: org.sco.movieratings.api.response.MoviePreview = mock()
        val mock = mock<org.sco.movieratings.network.TheMovieDBServiceImpl> {
            on { runBlocking { getMoviePreviews(any()) } } doReturn Result.success(
                org.sco.movieratings.api.response.PreviewsResponse(
                    listOf(mockMoviePreview)
                )
            )
        }
        val result = org.sco.movieratings.movielist.usecase.GetMoviePreviews(mock).invoke(1)
        assertTrue(result.isNotEmpty())
    }

    @Test
    fun `no previews returned on failure`() = runBlockingTest {
        val mock = mock<org.sco.movieratings.network.TheMovieDBServiceImpl> {
            on { runBlocking { getMoviePreviews(any()) } } doReturn Result.failure(Exception())
        }
        val result = org.sco.movieratings.movielist.usecase.GetMoviePreviews(mock).invoke(1)
        assertTrue(result.isEmpty())
    }
}