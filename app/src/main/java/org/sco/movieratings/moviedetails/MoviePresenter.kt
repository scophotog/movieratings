package org.sco.movieratings.moviedetails

import android.content.Context
import android.view.View
import com.squareup.picasso.Picasso
import org.sco.movieratings.R
import org.sco.movieratings.databinding.FragmentMovieBinding
import org.sco.movieratings.db.MovieSchema

private const val IMAGE_PATH = "https://image.tmdb.org/t/p/w185"

class MoviePresenter(private val binding: FragmentMovieBinding, private val context: Context) {

    fun present(movie: MovieSchema) {
        with(binding) {
            movieTitle.text = movie.title
            movieTitle.visibility = View.VISIBLE
            movieDetails.text = movie.overview
            Picasso.get().load(IMAGE_PATH + movie.posterPath)
                .placeholder(R.drawable.loading)
                .error(R.drawable.image_not_found)
                .into(poster)
            binding.releaseDate.text = context.getString(R.string.format_release_date, movie.releaseDate.split("-")[0])
            binding.voteAverage.text = context.getString(R.string.format_rating, movie.voteAverage)
        }
    }
}