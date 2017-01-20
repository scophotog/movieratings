package org.sco.movieratings.fragment;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;

import org.sco.movieratings.R;
import org.sco.movieratings.api.models.Movie;

/**
 * Created by sargenzi on 1/20/17.
 */

public class MovieListRouter {

    private final NavigationManager navigationManager;

    public MovieListRouter(@NonNull FragmentManager fragmentManager) {
        this.navigationManager = new NavigationManager(fragmentManager);
    }

    public void navigateTo(@NonNull Movie movie) {
        final MovieFragment movieFragment = MovieFragment.newInstance(movie);
        this.navigationManager.navigateTo(movieFragment, R.id.movie_detail_container);
    }

}
