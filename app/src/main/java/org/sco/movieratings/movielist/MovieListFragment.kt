package org.sco.movieratings.movielist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.navigation.NavigationBarView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import org.sco.movieratings.R
import org.sco.movieratings.databinding.FragmentMovieListBinding
import org.sco.movieratings.utility.Utility.getPreferredSort
import org.sco.movieratings.utility.Utility.updatePreference
import javax.inject.Inject

@AndroidEntryPoint
class MovieListFragment : Fragment() {

    private lateinit var movieListPresenter: MovieListPresenter
    private lateinit var bottomBarPresenter: BottomBarPresenter

    private val viewModel: MovieListViewModel by viewModels()

    private lateinit var binding: FragmentMovieListBinding

    @Inject
    lateinit var movieListAdapter: MovieListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMovieListBinding.inflate(inflater)

        bottomBarPresenter = BottomBarPresenter(binding)
        bottomBarPresenter.setOnItemSelectedListener(setBottomNavListener())
        movieListPresenter = MovieListPresenter(binding, movieListAdapter)
        subscribeUi()
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        val sortType = getPreferredSort(requireContext())
        if (sortType != null) {
            bottomBarPresenter.setViewToSortType(sortType)
        }
    }

    private fun subscribeUi() {
        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            viewModel.viewState.collect {
                presentMovies(it)
            }
            viewModel.movieListType.collect {
                setTitleBar(it)
            }
        }
    }

    private fun presentMovies(moviesResult: MovieListViewModel.MovieViewState) {
        binding.loading.isVisible = moviesResult.loading
        if (moviesResult.movies.isEmpty()) {
            movieListPresenter.setErrorView()
        } else {
            movieListPresenter.present(moviesResult.movies)
        }
    }

    private fun setTitleBar(value: MovieListViewModel.MovieListType) {
        when(value) {
            MovieListViewModel.MovieListType.POPULAR -> {
                binding.toolbar.toolbar.setTitle(R.string.most_popular_settings)
                updatePreference(
                    requireContext(),
                    resources.getString(R.string.pref_sort_popular_rated)
                )
            }
            MovieListViewModel.MovieListType.TOP -> {
                binding.toolbar.toolbar.setTitle(R.string.high_rated_settings)
                updatePreference(
                    requireContext(),
                    resources.getString(R.string.pref_sort_top_rated)
                )
            }
            MovieListViewModel.MovieListType.FAVORITE -> {
                binding.toolbar.toolbar.setTitle(R.string.my_favorites_settings)
                updatePreference(
                    requireContext(),
                    resources.getString(R.string.pref_sort_my_favorites)
                )
            }
        }
    }

    private fun setBottomNavListener(): NavigationBarView.OnItemSelectedListener {
        return NavigationBarView.OnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.bn_top_rated -> {
                    viewModel.setMovieListType(MovieListViewModel.MovieListType.TOP)
                }
                R.id.bn_most_popular -> {
                    viewModel.setMovieListType(MovieListViewModel.MovieListType.POPULAR)
                }
                R.id.bn_my_favorites -> {
                    viewModel.setMovieListType(MovieListViewModel.MovieListType.FAVORITE)
                }
                else -> {
                    throw IllegalArgumentException("Unknown navigation")
                }
            }
            true
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            MovieListFragment().apply {
                arguments = Bundle().apply {}
            }
    }
}