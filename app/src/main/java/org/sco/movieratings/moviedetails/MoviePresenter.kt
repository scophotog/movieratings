package org.sco.movieratings.moviedetails

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.view.View
import androidx.navigation.NavController
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import org.sco.movieratings.R
import org.sco.movieratings.api.response.Preview
import org.sco.movieratings.api.response.Review
import org.sco.movieratings.databinding.FragmentMovieBinding
import org.sco.movieratings.db.MovieSchema
import org.sco.movieratings.utility.Utility

class MoviePresenter(
    private val previewsAdapter: MoviePreviewAdapter,
    private val reviewsAdapter: MovieReviewAdapter
) {

    lateinit var binding: FragmentMovieBinding
    private lateinit var navController: NavController

    fun present(movie: MovieSchema, navController: NavController) {
        this.navController = navController
        with(binding) {
            binding.movie = movie
            Picasso.get().load(movie.posterPath)
                .placeholder(R.drawable.loading)
                .error(R.drawable.image_not_found)
                .into(poster)
            setMarkAsFavoriteAction()
        }
    }

    fun setPreviews(previews: List<Preview>) {
        with(binding) {
            if(previews.isNotEmpty()) {
                previewsContainer.root.visibility = View.VISIBLE
                previewsContainer.previewRecycler.setHasFixedSize(false)
                previewsAdapter.previews = previews
                previewsContainer.previewRecycler.swapAdapter(previewsAdapter, true)
                previewsAdapter.previewClickListener = { preview ->
                    startYouTube(preview)
                }
            }
        }
    }

    fun setReviews(reviews: List<Review>) {
        with(binding) {
            if (reviews.isNotEmpty()) {
                reviewsContainer.root.visibility = View.VISIBLE
                reviewsContainer.reviewsRecycler.setHasFixedSize(false)
                reviewsAdapter.reviews = reviews
                reviewsContainer.reviewsRecycler.swapAdapter(reviewsAdapter, true)
            }
        }
    }

    fun setMarkAsFavoriteAction() {
        binding.markAsFavorite.setOnClickListener {
            if (it.isSelected) {
                onRemoveFavorite()
            } else {
                onAddFavorite()
            }
        }
    }

    fun onRemoveFavorite() {
        Snackbar.make(binding.root, "Removed from Favorites", Snackbar.LENGTH_LONG)
            .setAction("View Favorites"
            ) { snackBarAction() }.show()
    }

    fun onAddFavorite() {
        Snackbar.make(binding.root, "Added to Favorites", Snackbar.LENGTH_LONG)
            .setAction("View Favorites") { snackBarAction() }.show()
    }

    private fun snackBarAction() {
        Utility.updatePreference(binding.root.context, binding.root.context.getString(R.string.pref_sort_my_favorites))
        navController.navigate(R.id.movieListFragment)
    }

    private fun startYouTube(preview: Preview) {
        val youTubeIntent = Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:${preview.key}"))
        val youTubeWebIntent = Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=${preview.key}"))
        try {
            binding.root.context.startActivity(youTubeIntent)
        } catch (e: ActivityNotFoundException) {
            binding.root.context.startActivity(youTubeWebIntent)
        }
    }
}