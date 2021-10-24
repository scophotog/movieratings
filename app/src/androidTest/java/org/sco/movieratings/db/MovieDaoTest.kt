package org.sco.movieratings.db

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MovieDaoTest {
    private lateinit var database: MovieDatabase
    private lateinit var movieDao: MovieDao

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun createDb() = runBlocking {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        database = Room.inMemoryDatabaseBuilder(context, MovieDatabase::class.java).build()
        movieDao = database.movieDao()
    }

    @After
    fun closeDb() {
        database.close()
    }

    @Test
    fun addFavoriteMovie() = runBlocking {
        val movie = MovieSchema(id = 1, title = "Foo", posterPath = "http://fake.jpg", overview = "Test 123",
        releaseDate = "1/1/2000", popularity = 10.0, voteAverage = 10F)
        movieDao.addFavorite(movie)
        assertThat(movieDao.getFavorites().first().size, equalTo(1))
    }

    @Test
    fun removeFavoriteMovie() = runBlocking {
        val movie = MovieSchema(id = 1, title = "Foo", posterPath = "http://fake.jpg", overview = "Test 123",
            releaseDate = "1/1/2000", popularity = 10.0, voteAverage = 10F)
        movieDao.addFavorite(movie)
        assertThat(movieDao.getFavorites().first().size, equalTo(1))
        movieDao.removeFavorite(movie)
        assertThat(movieDao.getFavorites().first().size, equalTo(0))
    }

    @Test
    fun findFavoriteMovie() = runBlocking {
        val movie = MovieSchema(id = 1, title = "Foo", posterPath = "http://fake.jpg", overview = "Test 123",
            releaseDate = "1/1/2000", popularity = 10.0, voteAverage = 10F)
        movieDao.addFavorite(movie)
        assertThat(movieDao.findFavorite(1).first(), allOf(notNullValue(),instanceOf(MovieSchema::class.java)))
    }
}