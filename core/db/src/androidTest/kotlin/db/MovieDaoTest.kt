package db

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.AllOf.allOf
import org.hamcrest.core.IsEqual.equalTo
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.sco.movieratings.db.MovieDatabase
import org.sco.movieratings.db.dao.MovieDao
import org.sco.movieratings.db.model.MovieSchema

class MovieDaoTest {
    private lateinit var database: MovieDatabase
    private lateinit var movieDao: MovieDao

    @Before
    fun setup() {
        database = run {
            val context = ApplicationProvider.getApplicationContext<Context>()
            Room.inMemoryDatabaseBuilder(context, MovieDatabase::class.java).build()
        }
        movieDao = database.movieDao()
    }

    @After
    fun closeDb() {
        database.close()
    }

    @Test
    fun addFavoriteMovie() = runTest {
        val movie = MovieSchema(
            id = 1, title = "Foo", posterPath = "http://fake.jpg", overview = "Test 123",
            releaseDate = "1/1/2000", popularity = 10.0, voteAverage = 10F
        )
        movieDao.addFavorite(movie)
        assertThat(movieDao.getFavorites().first(), equalTo(1))
    }

    @Test
    fun removeFavoriteMovie() = runTest {
        val movie = MovieSchema(
            id = 1, title = "Foo", posterPath = "http://fake.jpg", overview = "Test 123",
            releaseDate = "1/1/2000", popularity = 10.0, voteAverage = 10F
        )
        movieDao.addFavorite(movie)
        assertThat(movieDao.getFavorites().first(), equalTo(1))
        movieDao.removeFavorite(movie)
        assertThat(movieDao.getFavorites().first(), equalTo(0))
    }

    @Test
    fun findFavoriteMovie() = runTest {
        val movie = MovieSchema(
            id = 1, title = "Foo", posterPath = "http://fake.jpg", overview = "Test 123",
            releaseDate = "1/1/2000", popularity = 10.0, voteAverage = 10F
        )
        movieDao.addFavorite(movie)
        assertThat(movieDao.findFavorite(1), allOf(notNullValue(),instanceOf(MovieSchema::class.java)))
    }
}