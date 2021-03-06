package org.sco.movieratings.movielist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import org.sco.movieratings.R
import org.sco.movieratings.db.MovieSchema

private const val TAG = "MovieListAdapter"
private const val IMAGE_PATH = "https://image.tmdb.org/t/p/w500"

class MovieListAdapter(
    val movies: List<MovieSchema>,
    val listener: (MovieSchema) -> Unit
) : RecyclerView.Adapter<MovieListAdapter.MoviePosterViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviePosterViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.movie_card, parent, false)
        return MoviePosterViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    override fun onBindViewHolder(holder: MoviePosterViewHolder, position: Int) {
        if (movies.isNotEmpty()) {
            val movie = movies[position]
            Picasso.get()
                .load(IMAGE_PATH + movie.posterPath)
                .placeholder(R.drawable.loading)
                .error(R.drawable.image_not_found)
                .into(holder.poster)

            holder.itemView.setOnClickListener() {
                listener(movie)
            }
            holder.title.text = movie.title
            holder.rating.rating = movie.voteAverage.toFloat() / 2
        }
    }

    class MoviePosterViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var poster: ImageView = view.findViewById(R.id.moviePoster)
        var title: TextView = view.findViewById(R.id.movieTitle)
        var rating: RatingBar = view.findViewById(R.id.ratingBar)
    }
}