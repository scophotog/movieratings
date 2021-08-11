package org.sco.movieratings.moviedetails

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import org.sco.movieratings.R
import org.sco.movieratings.api.response.Preview
import org.sco.movieratings.api.response.Review
import org.sco.movieratings.databinding.FragmentMovieBinding
import org.sco.movieratings.db.MovieSchema
import org.sco.movieratings.utility.Utility.updatePreference

@AndroidEntryPoint
class MovieFragment : Fragment(), MoviePreviewAdapter.Callback {

    private lateinit var moviePreviewAdapter: MoviePreviewAdapter
    private lateinit var movieReviewAdapter: MovieReviewAdapter
    private lateinit var moviePresenter: MoviePresenter

    private val viewModel: MovieDetailsViewModel by viewModels()

    private val args: MovieFragmentArgs by navArgs()
    private lateinit var movie: MovieSchema

    private lateinit var binding: FragmentMovieBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMovieBinding.inflate(inflater)
        return binding.root
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

        moviePreviewAdapter = MoviePreviewAdapter(ArrayList(), this)
        movieReviewAdapter = MovieReviewAdapter(ArrayList())

        moviePresenter = MoviePresenter(binding, requireContext())

        binding.previewsContainer.previewRecycler.setHasFixedSize(false)
        binding.previewsContainer.previewRecycler.adapter = moviePreviewAdapter

        binding.reviewsContainer.reviewsRecycler.setHasFixedSize(false)
        binding.reviewsContainer.reviewsRecycler.adapter = movieReviewAdapter

        movie = args.movie
        presentMovie(movie)
    }

    private fun presentMovie(movie: MovieSchema) {
        moviePresenter.present(movie)

        binding.markAsFavorite.setOnClickListener { v ->
            if (v.isSelected) {
                viewModel.removeFromFavorites(movie)
                Snackbar.make(v, "Removed from Favorites", Snackbar.LENGTH_LONG)
                    .setAction("View Favorites", MyViewListener(findNavController(), requireContext())).show()
            } else {
                viewModel.addToFavorite(movie)
                Snackbar.make(v, "Added to Favorites", Snackbar.LENGTH_LONG)
                    .setAction("View Favorites", MyViewListener(findNavController(), requireContext())).show()
            }
        }

        viewModel.checkIfFavorite(movie).observe(viewLifecycleOwner, {
            binding.markAsFavorite.isSelected = it != null
        })

        viewModel.getPreviews(movie.id).observe(viewLifecycleOwner, { previews ->
            previews?.let {
                onPreviewsFetchFinished(it)
            }
        })

        viewModel.getReviews(movie.id).observe(viewLifecycleOwner, { reviews ->
            reviews?.let {
                onReviewsFetchFinished(it)
            }
        })
        viewModel.checkIfFavorite(movie)
    }

    private fun onReviewsFetchFinished(reviews: List<Review>) {
        movieReviewAdapter.add(reviews)
        if (reviews.isNotEmpty()) {
            binding.reviewsContainer.reviewsContainer.visibility = VISIBLE
        }
    }

    private fun onPreviewsFetchFinished(previews: List<Preview>) {
        moviePreviewAdapter.add(previews)
        if (previews.isNotEmpty()) {
            binding.previewsContainer.previewsContainer.visibility = VISIBLE
        }
    }

    class MyViewListener(private val navController: NavController, private val context: Context) : View.OnClickListener {
        override fun onClick(v: View) {
            updatePreference(context, context.getString(R.string.pref_sort_my_favorites))
            navController.navigate(R.id.movieListFragment)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            MovieFragment().apply {
                arguments = Bundle().apply {}
            }
    }
}