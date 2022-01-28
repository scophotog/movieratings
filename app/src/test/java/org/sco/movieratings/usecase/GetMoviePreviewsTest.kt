package org.sco.movieratings.usecase

import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.sco.movieratings.api.TheMovieDBService
import org.sco.movieratings.api.response.Preview
import org.sco.movieratings.api.response.PreviewsResponse

class GetMoviePreviewsTest {

    @Test
    fun `previews returns with successful result`() = runBlockingTest {
        val mockPreview: Preview = mock()
        val mock = mock<TheMovieDBService> {
            on { runBlocking { getMoviePreviews(any()) } } doReturn Result.success(
                PreviewsResponse(
                    listOf(mockPreview)
                )
            )
        }
        val result = GetMoviePreviews(mock).invoke(1)
        assertTrue(result.isNotEmpty())
    }

    @Test
    fun `no previews returned on failure`() = runBlockingTest {
        val mock = mock<TheMovieDBService> {
            on { runBlocking { getMoviePreviews(any()) } } doReturn Result.failure(Exception())
        }
        val result = GetMoviePreviews(mock).invoke(1)
        assertTrue(result.isEmpty())
    }
}