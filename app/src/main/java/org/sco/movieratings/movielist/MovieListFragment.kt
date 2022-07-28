package org.sco.movieratings.movielist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material.MaterialTheme
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import org.sco.movieratings.databinding.FragmentMovieListBinding
import org.sco.movieratings.movielist.compose.MovieList
import javax.inject.Inject

@AndroidEntryPoint
class MovieListFragment : Fragment() {

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
        setMovieListType()
        binding.apply {
            composeView.setContent {
                MaterialTheme {
                    MovieList()
                }
            }
        }
        return binding.root
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