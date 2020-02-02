package org.sco.movieratings.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import io.reactivex.subjects.PublishSubject
import org.sco.movieratings.R
import org.sco.movieratings.api.models.Movie

private const val TAG = "MovieListAdapter"
private const val IMAGE_PATH = "http://image.tmdb.org/t/p/w185"

class MovieListAdapter(val movies: List<Movie>, val clickStream: PublishSubject<Movie>) : RecyclerView.Adapter<MovieListAdapter.MoviePosterViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviePosterViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.grid_item_movie, parent, false)
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
                clickStream.onNext(movie)
            }
        }
    }

    class MoviePosterViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var poster: ImageView = view.findViewById(R.id.moviePoster)
    }
}