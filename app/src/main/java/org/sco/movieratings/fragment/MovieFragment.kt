package org.sco.movieratings.fragment

import android.content.ActivityNotFoundException
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import org.sco.movieratings.R
import org.sco.movieratings.activity.MainActivity
import org.sco.movieratings.adapter.MoviePreviewAdapter
import org.sco.movieratings.adapter.MovieReviewAdapter
import org.sco.movieratings.api.models.Movie
import org.sco.movieratings.api.models.Preview
import org.sco.movieratings.api.models.Review
import org.sco.movieratings.db.MovieContract
import org.sco.movieratings.interactor.MovieInteractor
import org.sco.movieratings.presenter.MoviePresenter
import org.sco.movieratings.utility.Utility.updatePreference

private const val TAG = "MovieFragment"
private const val PREVIEWS_EXTRA = "PREVIEWS_EXTRA"
private const val REVIEWS_EXTRA = "REVIEWS_EXTRA"


class MovieFragment : Fragment(), MoviePreviewAdapter.Callback {

    lateinit var movie: Movie
    private lateinit var previewLinearLayout: LinearLayout
    private lateinit var reviewLinearLayout: LinearLayout
    private lateinit var markAsFavorite: ImageButton
    private lateinit var previewsTitle: TextView
    private lateinit var reviewsTitle: TextView
    private lateinit var moviePreviewAdapter: MoviePreviewAdapter
    private lateinit var movieReviewAdapter: MovieReviewAdapter
    private lateinit var recyclerviewPreviews: RecyclerView
    private lateinit var recyclerviewReviews: RecyclerView
    private lateinit var movieInteractor: MovieInteractor
    private lateinit var compositeDisposable: CompositeDisposable
    private lateinit var moviePresenter: MoviePresenter

    companion object {
        const val MOVIE = "movie"

        fun newInstance(movie: Movie): MovieFragment {
            val movieFragment = MovieFragment()
            val args = Bundle()
            args.putParcelable(MOVIE, movie)
            movieFragment.arguments = args
            return movieFragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val args = arguments ?: throw IllegalArgumentException("Arguments were null.")
        movie = args.getParcelable(MOVIE) ?: throw IllegalArgumentException("A movie is required for this view")
        moviePreviewAdapter = MoviePreviewAdapter(ArrayList(), this)
        movieReviewAdapter = MovieReviewAdapter(ArrayList())
        movieInteractor = MovieInteractor()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_movie, container, false)
    }

    override fun view(preview: Preview) {
        val youTubeIntent = Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:${preview.key}"))
        val youTubeWebIntent = Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=${preview.key}"))
        try {
            startActivity(youTubeIntent)
        } catch (e: ActivityNotFoundException) {
            startActivity(youTubeWebIntent)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        moviePresenter = MoviePresenter(view)
        markAsFavorite = view.findViewById(R.id.mark_as_favorite)
        previewsTitle = view.findViewById(R.id.previews_title)
        reviewsTitle = view.findViewById(R.id.reviews_title)

        previewLinearLayout = view.findViewById(R.id.previews_container)
        recyclerviewPreviews = view.findViewById(R.id.preview_recycler)
        recyclerviewPreviews.setHasFixedSize(false)
        recyclerviewPreviews.adapter = moviePreviewAdapter

        reviewLinearLayout = view.findViewById(R.id.reviews_container)
        recyclerviewReviews = view.findViewById(R.id.reviews_recycler)
        recyclerviewReviews.setHasFixedSize(false)
        recyclerviewReviews.adapter = movieReviewAdapter

        movieView()
    }

    override fun onPause() {
        compositeDisposable.clear()
        super.onPause()
    }

    private fun movieView() {
        moviePresenter.present(movie)
        updateFavoriteButton()
        fetchPreviews()
        fetchReviews()
    }

    private fun isFavorite():Boolean {
        val cursor = context?.contentResolver?.query(
            MovieContract.CONTENT_URI,
            arrayOf("movie_id", "is_favorite"),
            "movie_id = ${movie.id}",
            null,
            null)

        return if (cursor != null && cursor.moveToFirst()) {
            cursor.close()
            true
        } else {
            false
        }
    }

    private fun markAsFavorite() {
        if(!isFavorite()) {
            val movieValues = ContentValues()
            movieValues.put(MovieContract.MovieEntry.IS_FAVORITE, 1)
            movieValues.put(MovieContract.MovieEntry.MOVIE_ID, movie.id)
            movieValues.put(MovieContract.MovieEntry.MOVIE_TITLE, movie.title)
            movieValues.put(MovieContract.MovieEntry.OVERVIEW, movie.overview)
            movieValues.put(MovieContract.MovieEntry.POPULARITY, movie.popularity)
            movieValues.put(MovieContract.MovieEntry.POSTER_PATH, movie.posterPath)
            movieValues.put(MovieContract.MovieEntry.RELEASE_DATE, movie.releaseDate)
            movieValues.put(MovieContract.MovieEntry.RATING, movie.voteAverage)
            context?.contentResolver?.insert(
                MovieContract.CONTENT_URI,
                movieValues
            )
            updateFavoriteButton()
        }

    }

    private fun updateFavoriteButton() {
        markAsFavorite.isSelected = isFavorite()
        markAsFavorite.setOnClickListener {view ->
            if (isFavorite()) {
                markAsFavorite.isSelected = false
                removeFromFavorite()
                Snackbar.make(view, "Removed from Favorites", Snackbar.LENGTH_LONG)
                    .setAction("View Favorites", MyViewListener()).show()
            } else {
                markAsFavorite.isSelected = true
                markAsFavorite()
                Snackbar.make(view, "Added to Favorites", Snackbar.LENGTH_LONG)
                    .setAction("View Favorites", MyViewListener()).show()
            }
        }
    }

    private fun removeFromFavorite() {
        if(isFavorite()) {
            context?.contentResolver?.delete(
                MovieContract.CONTENT_URI,
                "${MovieContract.MovieEntry.MOVIE_ID} = ${movie.id}",
                null
            )
            updateFavoriteButton()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val previews = moviePreviewAdapter.previews
        if (previews.isNotEmpty()) {
            outState.putParcelableArrayList(PREVIEWS_EXTRA, previews)
        }
        val reviews = movieReviewAdapter.reviews
        if (reviews.isNotEmpty()) {
            outState.putParcelableArrayList(REVIEWS_EXTRA, reviews)
        }
    }

    private fun onReviewsFetchFinished(reviews: List<Review>) {
        movieReviewAdapter.add(reviews)
        if (reviews.isNotEmpty()) {
            reviewLinearLayout.visibility = VISIBLE
        }
    }

    private fun onPreviewsFetchFinished(previews: List<Preview>) {
        moviePreviewAdapter.add(previews)
        if (previews.isNotEmpty()) {
            previewLinearLayout.visibility = VISIBLE
        }
    }

    private fun fetchReviews() {
        compositeDisposable = CompositeDisposable()
        compositeDisposable.add(movieInteractor.getReviews(movie)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({reviews -> onReviewsFetchFinished(reviews) },
                {
                    Toast.makeText(context, "Failed to fetch reviews", Toast.LENGTH_SHORT).show()
                    Log.e(TAG, it.message.toString())
                }
            ))
    }

    private fun fetchPreviews() {
        compositeDisposable = CompositeDisposable()
        compositeDisposable.add(movieInteractor.getPreviews(movie)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ previews -> onPreviewsFetchFinished(previews) },
                {
                    Toast.makeText(context, "Failed to fetch previews", Toast.LENGTH_SHORT).show()
                    Log.e(TAG, it.message.toString())
                }
            ))
    }

    class MyViewListener : View.OnClickListener {
        override fun onClick(v: View) {
            val intent = Intent(v.context, MainActivity::class.java)
            updatePreference(v.context, v.context.getString(R.string.pref_sort_my_favorites))
            v.context.startActivity(intent)
        }
    }
}