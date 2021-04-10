package org.sco.movieratings.movielist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import org.sco.movieratings.R
import org.sco.movieratings.databinding.FragmentMovieListBinding
import org.sco.movieratings.utility.Utility.getPreferredSort
import org.sco.movieratings.utility.Utility.updatePreference
import org.sco.movieratings.viewmodel.MovieListViewModel
import org.sco.movieratings.viewmodel.MovieListViewModelFactory
import org.sco.movieratings.db.MovieSchema
import javax.inject.Inject

private const val LOG = "MovieListFragment"
private const val SORT_MODE = "sortMode"

@AndroidEntryPoint
class MovieListFragment : Fragment() {

    private lateinit var movieListPresenter: MovieListPresenter
    private lateinit var bottomBarPresenter: BottomBarPresenter

    @Inject
    lateinit var viewModelFactory: MovieListViewModelFactory
    private lateinit var viewModel: MovieListViewModel

    private lateinit var binding: FragmentMovieListBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMovieListBinding.inflate(inflater)
        viewModel = ViewModelProvider(this, viewModelFactory).get(MovieListViewModel::class.java)

        bottomBarPresenter = BottomBarPresenter(binding)
        bottomBarPresenter.setOnNavigationItemSelectedListener(setBottomNavListener())
        movieListPresenter = MovieListPresenter(binding)

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
                    if (moviesResult.isSuccess) {
                        val movieList = moviesResult.getOrNull()!!.map { movie -> MovieSchema(movie.id, movie.title , movie.posterPath, movie.overview, movie.releaseDate, movie.popularity, movie.voteAverage) }
                        movieListPresenter.present(movieList, findNavController())
                    } else {
                        movieListPresenter.setErrorView()
                    }
                })
                binding.toolbar.toolbar.title = resources.getString(R.string.high_rated_settings)
                updatePreference(
                    requireContext(),
                    resources.getString(R.string.pref_sort_top_rated)
                )
            }
            1 -> {
                viewModel.popularMovies.observe(viewLifecycleOwner, { moviesResult ->
                    if (moviesResult.isSuccess) {
                        val movieList = moviesResult.getOrNull()!!.map { movie -> MovieSchema(movie.id, movie.title, movie.posterPath, movie.overview, movie.releaseDate, movie.popularity, movie.voteAverage) }
                        movieListPresenter.present(movieList, findNavController())
                    } else {
                        movieListPresenter.setErrorView()
                    }
                })
                binding.toolbar.toolbar.title = resources.getString(R.string.most_popular_settings)
                updatePreference(
                    requireContext(),
                    resources.getString(R.string.pref_sort_popular_rated)
                )
            }
            2 -> {
                viewModel.favoriteMovies.observe(viewLifecycleOwner, { moviesResult ->
                    if (moviesResult.isNotEmpty()) {
                        movieListPresenter.present(moviesResult, findNavController())
                    } else {
                        movieListPresenter.setErrorView()
                    }
                })
                binding.toolbar.toolbar.title = resources.getString(R.string.my_favorites_settings)
                updatePreference(
                    requireContext(),
                    resources.getString(R.string.pref_sort_my_favorites)
                )
            }
        }
    }

    private fun setBottomNavListener(): BottomNavigationView.OnNavigationItemSelectedListener {
        return BottomNavigationView.OnNavigationItemSelectedListener { item ->
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