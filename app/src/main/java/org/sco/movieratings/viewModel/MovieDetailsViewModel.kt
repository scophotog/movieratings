package org.sco.movieratings.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import org.sco.movieratings.api.DaggerApiComponent
import org.sco.movieratings.api.MoviesService
import org.sco.movieratings.api.models.Movie
import org.sco.movieratings.api.models.Preview
import org.sco.movieratings.api.models.Review
import org.sco.movieratings.api.response.PreviewsResponse
import org.sco.movieratings.api.response.ReviewsResponse
import javax.inject.Inject

class MovieDetailsViewModel() : ViewModel() {

    @Inject
    lateinit var moviesService: MoviesService

    private val disposable = CompositeDisposable()

    val reviewList = MutableLiveData<List<Review>>()
    val previewList = MutableLiveData<List<Preview>>()
    val reviewError = MutableLiveData<Boolean>()
    val previewError = MutableLiveData<Boolean>()

    init {
        DaggerApiComponent.create().inject(this)
    }

    fun refreshReviews(movie: Movie) {
        disposable.add(getReviews(moviesService.getMovieReviews(movie.id)))
    }

    fun refreshPreviews(movie: Movie) {
        disposable.add(getPreviews(moviesService.getMoviePreviews(movie.id)))
    }

    private fun getReviews(observable: Observable<ReviewsResponse>): DisposableObserver<Review> {
        val reviews = ArrayList<Review>()
        return observable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .flatMap {
                Observable.fromIterable(it.reviews)
            }
            .subscribeWith(object : DisposableObserver<Review>() {
                override fun onComplete() {
                    reviewList.value = reviews
                    reviewError.value = false
                }

                override fun onNext(review: Review) {
                    reviews.add(review)
                    reviewError.value = false
                }

                override fun onError(e: Throwable) {
                    reviewError.value = true
                }
            })
    }

    private fun getPreviews(observable: Observable<PreviewsResponse>): DisposableObserver<Preview> {
        val previews = ArrayList<Preview>()
        return observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap { Observable.fromIterable(it.previews) }
                .subscribeWith(object : DisposableObserver<Preview>() {
                    override fun onComplete() {
                        previewList.value = previews
                        previewError.value = false
                    }

                    override fun onNext(preview: Preview) {
                        previews.add(preview)
                        previewError.value = false
                    }

                    override fun onError(e: Throwable) {
                        previewError.value = true
                    }
                })
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }

}