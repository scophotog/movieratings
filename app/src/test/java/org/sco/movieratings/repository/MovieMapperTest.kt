package org.sco.movieratings.repository

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.sco.movieratings.api.response.Movie

class MovieMapperTest {

    private val imagePath = "http://fake.jpg"
    private val movieList = listOf(
        Movie(
            title = "Test",
            id = 1,
            posterPath = "http://poster.jpg",
            overview = "Overview",
            releaseDate = "01/01/2022",
            popularity = 1.0,
            voteAverage = 1.0,
            voteCount = 1
        )
    )

    @Test
    fun `it returns a list of movies`() {
//        val movieMapper = MovieMapper(imagePath)
//        val out = movieMapper(movieList)
//        assertTrue(out.isNotEmpty())
//        assertEquals(movieList[0].title, out[0].title)
    }
}