package org.sco.movieratings.movielist

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import org.sco.movieratings.R
import org.sco.movieratings.databinding.MovieCardBinding
import org.sco.movieratings.db.MovieSchema

class MovieListAdapter() :
    RecyclerView.Adapter<MovieListAdapter.MoviePosterViewHolder>() {

    var movies: List<MovieSchema> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviePosterViewHolder {
        return MoviePosterViewHolder(
            MovieCardBinding.inflate(
                LayoutInflater.from(parent.context),
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
            binding.setClickListener { view ->
                binding.movie?.let { movie ->
                    val action: NavDirections =
                        MovieListFragmentDirections.actionMovieListFragmentToMovieFragment(movie)
                    val navController = Navigation.findNavController(view)
                    navController.navigate(action)
                }
            }
        }

        fun bind(item: MovieSchema) {
            binding.apply {
                movie = item
                executePendingBindings()
            }
        }

        companion object {
            @BindingAdapter("posterUrl")
            @JvmStatic
            fun bindPosterUrl(view: ImageView, imageUrl: String?) {
                Log.d("MovieListAdapter", "bindPosterUrl: $imageUrl")
                Picasso.get()
                    .load(imageUrl)
                    .placeholder(R.drawable.loading)
                    .error(R.drawable.image_not_found)
                    .into(view)
            }
        }
    }
}