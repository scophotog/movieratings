package org.sco.movieratings.utility;

import android.content.Context;

import org.sco.movieratings.R;
import org.sco.movieratings.activity.MovieActivity;
import org.sco.movieratings.api.models.Movie;
import org.sco.movieratings.fragment.MovieFragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

/**
 * Created by sargenzi on 1/20/17.
 */

public class MovieListRouter {

    private final NavigationManager navigationManager;

    public MovieListRouter(@NonNull FragmentManager fragmentManager) {
        this.navigationManager = new NavigationManager(fragmentManager);
    }

    public void startFragment(@NonNull Movie movie) {
        final MovieFragment movieFragment = MovieFragment.newInstance(movie);
        this.navigationManager.navigateTo(movieFragment, R.id.movie_detail_container);
    }

    public void startActivity(@NonNull Movie movie, @NonNull Context context) {
        final MovieActivity movieActivity = MovieActivity.newInstance(movie, context);
        this.navigationManager.navigateToActivity(movieActivity, context);
    }

}
