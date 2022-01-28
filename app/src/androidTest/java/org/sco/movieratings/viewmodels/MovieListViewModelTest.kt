package org.sco.movieratings.viewmodels

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.sco.movieratings.movielist.MovieListViewModel
import org.sco.movieratings.repository.MovieRepository
import javax.inject.Inject

@HiltAndroidTest
class MovieListViewModelTest {

    private val hiltAndroidRule = HiltAndroidRule(this)
    private lateinit var viewModel: MovieListViewModel

    @Inject
    lateinit var movieRepository: MovieRepository

    @Before
    fun setup() {
        hiltAndroidRule.inject()
        val savedStateHandle: SavedStateHandle = SavedStateHandle().apply {
            set("MOVIE_LIST_SAVED_STATE_KEY", MovieListViewModel.MovieListType.POPULAR)
        }
        viewModel = MovieListViewModel(movieRepository, savedStateHandle)
    }

}