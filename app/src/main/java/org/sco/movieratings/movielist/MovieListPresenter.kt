package org.sco.movieratings.movielist

import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.sco.movieratings.databinding.FragmentMovieListBinding
import org.sco.movieratings.db.MovieSchema

class MovieListPresenter(private val binding: FragmentMovieListBinding, private val adapter: MovieListAdapter) {
    init {
        binding.movieList.setHasFixedSize(true)
        binding.movieList.layoutManager = GridLayoutManager(binding.root.context, 2, RecyclerView.VERTICAL, false)
    }

    fun present(movies: List<MovieSchema>) {
        with(binding) {
            loading.visibility = View.GONE
            if (movies.isEmpty()) {
                movieList.visibility = View.GONE
                emptyView.visibility = View.VISIBLE
            } else {
                movieList.visibility = View.VISIBLE
                emptyView.visibility = View.GONE
            }

            adapter.movies = movies

            movieList.swapAdapter(adapter, true)
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