package org.sco.movieratings.viewModel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Function
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import org.sco.movieratings.api.DaggerApiComponent
import org.sco.movieratings.api.MoviesService
import org.sco.movieratings.api.models.Movie
import org.sco.movieratings.api.response.MoviesResponse
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
        disposable.add(getMovies(moviesService.getPopularMovies()))
    }

    fun refreshTopRatedMovies() {
        disposable.add(getMovies(moviesService.getTopRatedMovies()))
    }

    fun refreshFavoriteMovies(context: Context) {
        disposable.add(getFavorites(context))
    }

    private fun getMovies(observable: Observable<MoviesResponse>): DisposableObserver<Movie> {
        val movieList = ArrayList<Movie>()
        return observable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .flatMap {
                Observable.fromIterable(it.movies)
            }
            .subscribeWith(object : DisposableObserver<Movie>() {
                override fun onComplete() {
                    loadingState.value = false
                    movies.value = movieList
                }

                override fun onNext(movie: Movie) {
                    movieList.add(movie)
                    moviesLoadError.value = false
                }

                override fun onError(e: Throwable) {
                    moviesLoadError.value = true
                    loadingState.value = false
                }
            })
    }

    private fun getFavorites(context: Context): DisposableObserver<Movie> {
        val movieList = ArrayList<Movie>()
        return moviesService.getFavoriteMovies(context)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .flatMap(Function<List<Movie>, Observable<Movie>>() {
                Observable.fromIterable(it)
            })
            .subscribeWith(object : DisposableObserver<Movie>() {
                override fun onComplete() {
                    loadingState.value = false
                    movies.value = movieList
                }

                override fun onNext(movie: Movie) {
                    movieList.add(movie)
                    moviesLoadError.value = false
                }

                override fun onError(e: Throwable) {
                    moviesLoadError.value = true
                    loadingState.value = false
                }
            })
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}

