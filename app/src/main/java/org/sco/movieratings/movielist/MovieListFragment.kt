package org.sco.movieratings.movielist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import org.sco.movieratings.databinding.FragmentMovieListBinding
import org.sco.movieratings.db.MovieSchema
import org.sco.movieratings.utility.Result
import javax.inject.Inject

@AndroidEntryPoint
class MovieListFragment : Fragment() {

    private lateinit var movieListPresenter: MovieListPresenter

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
        movieListPresenter = MovieListPresenter(binding, movieListAdapter)
        subscribeUi()
        return binding.root
    }

    private fun subscribeUi() {
        setMovieListType()
        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            viewModel.viewState.collect {
                when (it) {
                    Result.Empty,
                    is Result.Error -> movieListPresenter.setErrorView()
                    Result.InProgress -> movieListPresenter.setNowLoadingView()
                    is Result.Success -> presentMovies(it.extractData ?: emptyList())
                }

            }
        }
    }

    private fun presentMovies(moviesResult: List<MovieSchema>) {
        if (moviesResult.isEmpty()) {
            movieListPresenter.setErrorView()
        } else {
            movieListPresenter.present(moviesResult)
        }
    }

    private fun setMovieListType() {
        requireArguments().getString(MOVIE_LIST_TYPE_KEY)?.let {
            viewModel.setMovieListType(MovieListType.valueOf(it))
        } ?: throw IllegalArgumentException("Unknown ListType")
    }

    companion object {
        const val MOVIE_LIST_TYPE_KEY = "ListType"

        fun fragmentListInstance(listType: MovieListType): () -> Fragment {
            return {
                MovieListFragment().apply {
                    arguments = Bundle().apply {
                        putString(MOVIE_LIST_TYPE_KEY, listType.name)
                    }
                }
            }
        }
    }
}