package org.sco.movieratings.viewModel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import org.sco.movieratings.api.DaggerApiComponent
import org.sco.movieratings.api.MoviesService
import org.sco.movieratings.api.models.Movie
import org.sco.movieratings.fragment.MovieType
import javax.inject.Inject

class MovieListViewModel() : ViewModel() {

    @Inject
    lateinit var moviesService: MoviesService

    init {
        DaggerApiComponent.create().inject(this)
    }

    val movies = MutableLiveData<List<Movie>>()
    val loadingState = MutableLiveData<Boolean>()
    val moviesLoadError = MutableLiveData<Boolean>()

    private val disposable = CompositeDisposable()

    fun refreshPopularMovies() {
        fetchMovies(MovieType.POPULAR, null)
    }

    fun refreshTopRatedMovies() {
        fetchMovies(MovieType.TOP_RATED, null)
    }

    fun refreshFavoriteMovies(context: Context) {
        fetchMovies(MovieType.FAVORITE, context)
    }

    private fun fetchMovies(movieListType: MovieType, context: Context?) {
        loadingState.value = true
        disposable.add(
            moviesService.getMovies(movieListType, context)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<List<Movie>>() {
                    override fun onSuccess(movieList: List<Movie>) {
                        movies.value = movieList
                        moviesLoadError.value = false
                        loadingState.value = false
                    }

                    override fun onError(e: Throwable) {
                        moviesLoadError.value = true
                        loadingState.value = false
                    }
                })
        )
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}