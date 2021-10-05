package org.sco.movieratings.movielist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.navigation.NavigationBarView
import dagger.hilt.android.AndroidEntryPoint
import org.sco.movieratings.R
import org.sco.movieratings.databinding.FragmentMovieListBinding
import org.sco.movieratings.db.MovieSchema
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
        viewModel.isLoading.observe(viewLifecycleOwner, {
            binding.loading.isVisible = it
        })
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        val sortType = getPreferredSort(requireContext())
        setViewToSortType(sortType!!)
    }

    private fun setViewToSortType(sortType: String) {
        val topRated = resources.getString(R.string.pref_sort_top_rated)
        val popular = resources.getString(R.string.pref_sort_popular_rated)
        val favorites = resources.getString(R.string.pref_sort_my_favorites)
        when (sortType) {
            topRated -> {
                updateMovieList(0)
                bottomBarPresenter.setSelectedItemById(R.id.bn_top_rated)
            }
            popular -> {
                updateMovieList(1)
                bottomBarPresenter.setSelectedItemById(R.id.bn_most_popular)
            }
            favorites -> {
                updateMovieList(2)
                bottomBarPresenter.setSelectedItemById(R.id.bn_my_favorites)
            }
            else -> {
                throw IllegalArgumentException("Unknown navigation: $sortType")
            }
        }
    }

    private fun updateMovieList(position: Int) {
        movieListPresenter.setNowLoadingView()

        when (position) {
            0 -> {
                viewModel.topRatedMovies.observe(viewLifecycleOwner, { moviesResult ->
                    presentMovies(moviesResult)
                })
                binding.toolbar.toolbar.title = resources.getString(R.string.high_rated_settings)
                updatePreference(
                    requireContext(),
                    resources.getString(R.string.pref_sort_top_rated)
                )
            }
            1 -> {
                viewModel.popularMovies.observe(viewLifecycleOwner, { moviesResult ->
                    presentMovies(moviesResult)
                })
                binding.toolbar.toolbar.title = resources.getString(R.string.most_popular_settings)
                updatePreference(
                    requireContext(),
                    resources.getString(R.string.pref_sort_popular_rated)
                )
            }
            2 -> {
                viewModel.favoriteMovies.observe(viewLifecycleOwner, { moviesResult ->
                    presentMovies(moviesResult)
                })
                binding.toolbar.toolbar.title = resources.getString(R.string.my_favorites_settings)
                updatePreference(
                    requireContext(),
                    resources.getString(R.string.pref_sort_my_favorites)
                )
            }
        }
    }

    private fun presentMovies(moviesResult: List<MovieSchema>) {
        if (moviesResult.isEmpty()) {
            movieListPresenter.setErrorView()
        } else {
            movieListPresenter.present(moviesResult, findNavController())
        }
    }

    private fun setBottomNavListener(): NavigationBarView.OnItemSelectedListener {
        return NavigationBarView.OnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.bn_top_rated -> {
                    updateMovieList(0)
                }
                R.id.bn_most_popular -> {
                    updateMovieList(1)
                }
                R.id.bn_my_favorites -> {
                    updateMovieList(2)
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