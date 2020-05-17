package org.sco.movieratings.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.bottomnavigation.BottomNavigationView
import io.reactivex.disposables.CompositeDisposable
import org.sco.movieratings.R
import org.sco.movieratings.activity.MainActivity
import org.sco.movieratings.api.models.Movie
import org.sco.movieratings.interactor.MoviesInteractor
import org.sco.movieratings.presenter.BottomBarPresenter
import org.sco.movieratings.presenter.MovieListPresenter
import org.sco.movieratings.utility.MovieListRouter
import org.sco.movieratings.utility.Utility.getPreferredSort
import org.sco.movieratings.utility.Utility.updatePreference
import org.sco.movieratings.viewModel.MovieListViewModel

private const val LOG = "MovieListFragment"
private const val SAVED_MOVIES = "movies"

class MovieListFragment : Fragment() {

    private lateinit var moviesInteractor: MoviesInteractor
    private lateinit var movieListPresenter: MovieListPresenter
    private lateinit var bottomBarPresenter: BottomBarPresenter
    private lateinit var compositeDisposable: CompositeDisposable

    private lateinit var movieListViewModel: MovieListViewModel
    private lateinit var movies: ArrayList<Movie>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        moviesInteractor = MoviesInteractor(MovieListRouter(parentFragmentManager))
        movieListViewModel = ViewModelProvider(this).get(MovieListViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_movie_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bottomBarPresenter = BottomBarPresenter(view)
        bottomBarPresenter.setOnNavigationItemSelectedListener(setBottomNavListener())
        movieListPresenter = MovieListPresenter(view)

        if (savedInstanceState != null) {
            movies =
                savedInstanceState.getParcelableArrayList<Movie>(SAVED_MOVIES) as ArrayList<Movie>
            movieListPresenter.present(movies)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(SAVED_MOVIES, movieListPresenter.movies)
    }

    override fun onResume() {
        super.onResume()
        val sortType = getPreferredSort(requireContext())
        setViewToSortType(sortType!!)
        compositeDisposable = CompositeDisposable()
        setMovieClickInteractor(compositeDisposable)
    }

    override fun onPause() {
        compositeDisposable.clear()
        super.onPause()
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

    private fun setMovieClickInteractor(compositeDisposable: CompositeDisposable) {
        compositeDisposable.add(
            movieListPresenter.movieClickStream
                .subscribe { movie ->
                    moviesInteractor.onMovieClicked(
                        movie,
                        requireContext()
                    )
                }
        )
    }

    private fun updateMovieList(position: Int) {
        movieListPresenter.setNowLoadingView()

        when (position) {
            0 -> {
                movieListViewModel.refreshTopRatedMovies()
                (activity as MainActivity?)!!.setTitle(resources.getString(R.string.high_rated_settings))
                updatePreference(
                    requireContext(),
                    resources.getString(R.string.pref_sort_top_rated)
                )
            }
            1 -> {
                movieListViewModel.refreshPopularMovies()
                (activity as MainActivity?)!!.setTitle(resources.getString(R.string.most_popular_settings))
                updatePreference(
                    requireContext(),
                    resources.getString(R.string.pref_sort_popular_rated)
                )
            }
            2 -> {
                movieListViewModel.refreshFavoriteMovies(requireContext())
                (activity as MainActivity?)!!.setTitle(resources.getString(R.string.my_favorites_settings))
                updatePreference(
                    requireContext(),
                    resources.getString(R.string.pref_sort_my_favorites)
                )
            }
        }

        movieListViewModel.movies.observe(viewLifecycleOwner, Observer { movies ->
            movies?.let {
                if (it.isNotEmpty()) {
                    movieListPresenter.present(it)
                } else {
                    movieListPresenter.setErrorView()
                }
            }
        })
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

}