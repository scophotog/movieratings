package org.sco.movieratings.presenter

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import org.sco.movieratings.R
import org.sco.movieratings.adapter.MovieListAdapter
import org.sco.movieratings.api.models.Movie

class MovieListPresenter(view: View) {
    private val loadingView: View by lazy {
        view.findViewById<View>(R.id.loading)
    }
    private val emptyView: TextView by lazy {
        view.findViewById<TextView>(R.id.empty_view)
    }
    private val recycler: RecyclerView by lazy {
        view.findViewById<RecyclerView>(R.id.movie_list)
    }
    private val clickStream = PublishSubject.create<Movie>()
    val movies: ArrayList<Movie> = ArrayList()
        get() = field

    fun present(movies: List<Movie>) {
        loadingView.visibility = View.GONE
        if (movies.isEmpty()) {
            recycler.visibility = View.GONE
            emptyView.visibility = View.VISIBLE
        } else {
            recycler.visibility = View.VISIBLE
            emptyView.visibility = View.GONE
        }
        this.movies.clear()
        this.movies.addAll(movies)
        recycler.swapAdapter(MovieListAdapter(movies, clickStream), true)
    }

    val movieClickStream: Observable<Movie>
        get() = clickStream.hide()


    fun setNowLoadingView() {
        recycler.visibility = View.GONE
        loadingView.visibility = View.VISIBLE
    }

    fun setErrorView() {
        loadingView.visibility = View.GONE
        recycler.visibility = View.GONE
        emptyView.visibility = View.VISIBLE
    }

    init {
        recycler.setHasFixedSize(true)
        recycler.layoutManager = GridLayoutManager(view.context, 2, RecyclerView.VERTICAL, false)
        recycler.adapter = MovieListAdapter(ArrayList(), clickStream)
    }
}