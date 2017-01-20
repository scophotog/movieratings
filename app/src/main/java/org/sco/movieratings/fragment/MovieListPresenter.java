package org.sco.movieratings.fragment;

import java.util.ArrayList;
import java.util.List;

import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import org.sco.movieratings.R;
import org.sco.movieratings.adapter.MovieListAdapter;
import org.sco.movieratings.api.models.Movie;

import rx.Observable;
import rx.subjects.PublishSubject;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Created by sargenzi on 1/20/17.
 */

public class MovieListPresenter {

    private final TextView mEmptyView;
    private final RecyclerView mRecycler;
    private final PublishSubject<Movie> clickStream = PublishSubject.create();

    private List<Movie> mMovies = new ArrayList<>();

    public MovieListPresenter(@NonNull View view) {
        mEmptyView = (TextView) view.findViewById(R.id.empty_view);
        mRecycler = (RecyclerView) view.findViewById(R.id.movie_list);
        mRecycler.setHasFixedSize(true);
        mRecycler.setLayoutManager(
                new GridLayoutManager(view.getContext(), 2, GridLayoutManager.VERTICAL, false)
        );
        mRecycler.setAdapter(new MovieListAdapter(clickStream));
    }

    public void present(@NonNull List<Movie> movies) {
        if (movies.isEmpty()) {
            mRecycler.setVisibility(GONE);
            mEmptyView.setVisibility(VISIBLE);
        } else {
            mRecycler.setVisibility(VISIBLE);
            mEmptyView.setVisibility(GONE);
        }
        mMovies.clear();
        mMovies.addAll(movies);
        this.mRecycler.swapAdapter(new MovieListAdapter(movies, clickStream), true);
    }

    public Observable<Movie> getMovieClickStream() {
        return clickStream.asObservable();
    }

    public List<Movie> getMovies() {
        return mMovies;
    }

}
