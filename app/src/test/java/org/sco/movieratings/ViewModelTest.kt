package org.sco.movieratings

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.reactivex.Single
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.sco.movieratings.api.MoviesService
import org.sco.movieratings.api.models.Movie
import org.sco.movieratings.fragment.MovieType
import org.sco.movieratings.viewModel.MovieListViewModel

class ViewModelTest {

    @get:Rule
    var rule = InstantTaskExecutorRule()

    @Mock
    lateinit var moviesService: MoviesService

    @Mock
    lateinit var mockContext: Context

    @InjectMocks
    var movieListViewModel = MovieListViewModel()

    private var testSingle: Single<List<Movie>>? = null
    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)

        RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
        RxJavaPlugins.setComputationSchedulerHandler { Schedulers.trampoline() }
        RxJavaPlugins.setNewThreadSchedulerHandler { Schedulers.trampoline() }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
    }

    @Test
    fun getPopularMovies() {
        testSingle = Single.just(listOf(getTestMovie()))

        `when`(moviesService.getMovies(MovieType.POPULAR, null)).thenReturn(testSingle)
        movieListViewModel.refreshPopularMovies()

        assertEquals(1, movieListViewModel.movies.value?.size)
        assertEquals(false, movieListViewModel.moviesLoadError.value)
        assertEquals(false, movieListViewModel.loadingState.value)
    }

    @Test
    fun getTopRatedMovies() {
        testSingle = Single.just(listOf(getTestMovie()))

        `when`(moviesService.getMovies(MovieType.TOP_RATED, null)).thenReturn(testSingle)
        movieListViewModel.refreshTopRatedMovies()

        assertEquals(1, movieListViewModel.movies.value?.size)
        assertEquals(false, movieListViewModel.moviesLoadError.value)
        assertEquals(false, movieListViewModel.loadingState.value)
    }

    @Test
    fun getFavoriteMovies() {
        testSingle = Single.just(listOf(getTestMovie()))

        `when`(moviesService.getMovies(MovieType.FAVORITE, mockContext)).thenReturn(testSingle)
        movieListViewModel.refreshFavoriteMovies(mockContext)

        assertEquals(1, movieListViewModel.movies.value?.size)
        assertEquals(false, movieListViewModel.moviesLoadError.value)
        assertEquals(false, movieListViewModel.loadingState.value)
    }

    @Test
    fun noMoviesFound() {
        testSingle = Single.error(Throwable())
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

