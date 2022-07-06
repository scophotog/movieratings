package org.sco.movieratings.movielist

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import coil.load
import org.sco.movieratings.MovieViewPagerFragmentDirections
import org.sco.movieratings.R
import org.sco.movieratings.databinding.MovieCardBinding
import org.sco.movieratings.db.MovieSchema

class MovieListAdapter :
    RecyclerView.Adapter<MovieListAdapter.MoviePosterViewHolder>() {

    var movies: List<MovieSchema> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviePosterViewHolder {
        return MoviePosterViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.movie_card,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MoviePosterViewHolder, position: Int) {
        if (movies.isNotEmpty()) {
            holder.bind(movies[position])
        }
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    class MoviePosterViewHolder(
        private val binding: MovieCardBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            with(binding) {
                setClickListener { view ->
                    movie?.let { movie ->
                        Navigation.findNavController(view).run {
                            navigate(
                                MovieViewPagerFragmentDirections.actionViewPagerFragmentToMovieDetailFragment(movie)
                            )
                        }
                    }
                }
            }
        }

        fun bind(item: MovieSchema) {
            binding.apply {
                movie = item
                executePendingBindings()
            }
        }
    }
}

@BindingAdapter("imageUrl")
fun ImageView.bindPosterUrl(imageUrl: String?) {
    if (imageUrl.isNullOrBlank()) {
        load(R.drawable.image_not_found)
    } else {
        load(imageUrl) {
            placeholder(R.drawable.loading)
            error(R.drawable.image_not_found)
        }
    }
}