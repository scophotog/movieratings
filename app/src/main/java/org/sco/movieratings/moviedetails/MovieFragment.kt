package org.sco.movieratings.moviedetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import org.sco.movieratings.databinding.FragmentMovieBinding
import org.sco.movieratings.db.MovieSchema
import javax.inject.Inject

@AndroidEntryPoint
class MovieFragment : Fragment() {

    private val viewModel: MovieDetailsViewModel by viewModels()

    private val args: MovieFragmentArgs by navArgs()

    private var _binding: FragmentMovieBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var moviePresenter: MoviePresenter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieBinding.inflate(inflater, container, false)

        (activity as AppCompatActivity).apply {
            setSupportActionBar(binding.toolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        return binding.root

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toolbar.setNavigationOnClickListener {
            activity?.onBackPressed()
        }
        presentMovie(args.movie)
    }

    private fun presentMovie(movie: MovieSchema) {
        moviePresenter.binding = binding
        moviePresenter.present(movie, findNavController())

        binding.markAsFavorite.setOnClickListener { v ->
            if (v.isSelected) {
                viewModel.removeFromFavorites(movie)
                moviePresenter.onRemoveFavorite()

            } else {
                viewModel.addToFavorite(movie)
                moviePresenter.onAddFavorite()
            }
        }

        viewModel.checkIfFavorite(movie).observe(viewLifecycleOwner) {
            binding.markAsFavorite.isSelected = it
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.isFavorite.collect {
                binding.markAsFavorite.isSelected = it
            }
        }

        viewModel.getPreviews(movie.id).observe(viewLifecycleOwner) { previews ->
            previews?.let {
                moviePresenter.setPreviews(it)
            }
        }

        viewModel.getReviews(movie.id).observe(viewLifecycleOwner) { reviews ->
            reviews?.let {
                moviePresenter.setReviews(it)
            }
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