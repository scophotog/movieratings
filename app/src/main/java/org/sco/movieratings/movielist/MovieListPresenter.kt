package org.sco.movieratings.movielist

import android.view.View
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.sco.movieratings.databinding.FragmentMovieListBinding
import org.sco.movieratings.db.MovieSchema
import org.sco.movieratings.movielist.MovieListAdapter
import org.sco.movieratings.movielist.MovieListFragmentDirections

class MovieListPresenter(private val binding: FragmentMovieListBinding) {
    init {
        binding.movieList.setHasFixedSize(true)
        binding.movieList.layoutManager = GridLayoutManager(binding.root.context, 2, RecyclerView.VERTICAL, false)
    }

    fun present(movies: List<MovieSchema>, navController: NavController) {
        with(binding) {
            loading.visibility = View.GONE
            if (movies.isEmpty()) {
                movieList.visibility = View.GONE
                emptyView.visibility = View.VISIBLE
            } else {
                movieList.visibility = View.VISIBLE
                emptyView.visibility = View.GONE
            }

            movieList.swapAdapter(MovieListAdapter(movies
            ) { movie ->
                val action: NavDirections =
                    MovieListFragmentDirections.actionMovieListFragmentToMovieFragment(movie)
                navController.navigate(action)
            }, true)
        }
    }

    fun setNowLoadingView() {
        with(binding) {
            movieList.visibility = View.GONE
            loading.visibility = View.VISIBLE
        }
    }

    fun setErrorView() {
        with(binding) {
            loading.visibility = View.GONE
            movieList.visibility = View.GONE
            emptyView.visibility = View.VISIBLE
        }
    }


}