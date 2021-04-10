package org.sco.movieratings

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.sco.movieratings.api.MoviesService
import org.sco.movieratings.viewmodel.MovieListViewModel

class ViewModelTest {

    @get:Rule
    var rule = InstantTaskExecutorRule()

    @Mock
    lateinit var moviesService: MoviesService

    @Mock
    lateinit var mockContext: Context

    @InjectMocks
    var movieListViewModel = MovieListViewModel()

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun getPopularMovies() {
        `when`(moviesService.getMovies(MovieType.POPULAR, null)).thenReturn(testSingle)
        movieListViewModel.refreshPopularMovies()

        assertEquals(1, movieListViewModel.movies.value?.size)
        assertEquals(false, movieListViewModel.moviesLoadError.value)
        assertEquals(false, movieListViewModel.loadingState.value)
    }

    @Test
    fun getTopRatedMovies() {
        testSingle = Observable.just(listOf(getTestMovie()))

        `when`(moviesService.getMovies(MovieType.TOP_RATED, null)).thenReturn(testSingle)
        movieListViewModel.refreshTopRatedMovies()

        assertEquals(1, movieListViewModel.movies.value?.size)
        assertEquals(false, movieListViewModel.moviesLoadError.value)
        assertEquals(false, movieListViewModel.loadingState.value)
    }

    @Test
    fun getFavoriteMovies() {
        testSingle = Observable.just(listOf(getTestMovie()))

        `when`(moviesService.getMovies(MovieType.FAVORITE, mockContext)).thenReturn(testSingle)
        movieListViewModel.refreshFavoriteMovies(mockContext)

        assertEquals(1, movieListViewModel.movies.value?.size)
        assertEquals(false, movieListViewModel.moviesLoadError.value)
        assertEquals(false, movieListViewModel.loadingState.value)
    }

    @Test
    fun noMoviesFound() {
        testSingle = Observable.error(Throwable())
        `when`(moviesService.getMovies(MovieType.FAVORITE, mockContext)).thenReturn(testSingle)
        movieListViewModel.refreshFavoriteMovies(mockContext)
        assertEquals(null, movieListViewModel.movies.value?.size)
        assertEquals(true, movieListViewModel.moviesLoadError.value)
        assertEquals(false, movieListViewModel.loadingState.value)
    }

    private fun getTestMovie(): Movie {
        return Movie(
            "Fake Movie",
            1,
            "url",
            "Fake Overview",
            "01/01/2020",
            10.0,
            10.0
        )
    }
}

