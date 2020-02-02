package org.sco.movieratings.presenter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import org.sco.movieratings.R
import org.sco.movieratings.api.models.Movie

private const val IMAGE_PATH = "http://image.tmdb.org/t/p/w185"

class MoviePresenter(val view: View) {

    val movieTitle: TextView by lazy {
        view.findViewById<TextView>(R.id.movie_title)
    }
    val moviePoster: ImageView by lazy {
        view.findViewById<ImageView>(R.id.poster)
    }
    val movieDetails: TextView by lazy {
        view.findViewById<TextView>(R.id.movie_details)
    }

    val movieReleaseDate: TextView by lazy {
        view.findViewById<TextView>(R.id.release_date)
    }

    val movieVoteAverage: TextView by lazy {
        view.findViewById<TextView>(R.id.vote_average)
    }

    val emptyTextView: TextView? by lazy {
        view.rootView.findViewById<TextView>(R.id.empty_detail_view)
    }

    fun present(movie: Movie) {
        if (emptyTextView != null) {
            emptyTextView?.visibility = View.GONE
        }

        movieTitle.text = movie.title
        movieTitle.visibility = View.VISIBLE
        movieDetails.text = movie.overview

        Picasso.get().load(IMAGE_PATH + movie.posterPath)
            .placeholder(R.drawable.loading)
            .error(R.drawable.image_not_found)
            .into(moviePoster)

        movieReleaseDate.text = view.context.getString(R.string.format_release_date, movie.releaseDate.split("-")[0])
        movieVoteAverage.text = view.context.getString(R.string.format_rating, movie.voteAverage)
    }
}