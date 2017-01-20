package org.sco.movieratings.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.sco.movieratings.R;
import org.sco.movieratings.adapter.MoviePreviewAdapter;
import org.sco.movieratings.adapter.MovieReviewAdapter;
import org.sco.movieratings.api.models.Movie;
import org.sco.movieratings.db.MovieColumns;
import org.sco.movieratings.db.MovieProvider;
import org.sco.movieratings.api.models.Preview;
import org.sco.movieratings.api.models.Review;
import org.sco.movieratings.interactor.MovieInteractor;
import org.sco.movieratings.presenter.MoviePresenter;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class MovieFragment extends Fragment implements MoviePreviewAdapter.Callback {

    private static final String LOG_TAG = MovieFragment.class.getSimpleName();

    public static final String MOVIE = "movie";

    public static final String PREVIEWS_EXTRA = "PREVIEWS_EXTRA";
    public static final String REVIEWS_EXTRA = "REVIEWS_EXTRA";


    LinearLayoutManager mPreviewLinearLayout;
    LinearLayoutManager mReviewLinearLayout;

    Button mMarkAsFavorite;
    Button mUnfavorite;
    TextView mPreviewsTitle;
    TextView mReviewsTitle;

    private MoviePreviewAdapter mMoviePreviewAdapter;
    private MovieReviewAdapter mMovieReviewAdapter;
    RecyclerView mRecyclerViewPreviews;
    RecyclerView mRecyclerViewReviews;
    Movie mMovie;

    private MovieInteractor mMovieInteractor;
    private CompositeSubscription mCompositeSubscription;
    private MoviePresenter mMoviePresenter;

    public MovieFragment() {

    }

    public static MovieFragment newInstance(@NonNull Movie movie) {
        final MovieFragment movieFragment = new MovieFragment();
        final Bundle args = new Bundle();
        args.putParcelable(MOVIE, movie);
        movieFragment.setArguments(args);
        return movieFragment;
    }

    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMovie = getArguments().getParcelable(MOVIE);
        if (mMovie == null) {
//            throw new IllegalArgumentException("A movie is required for this view");
        }
        mMoviePreviewAdapter = new MoviePreviewAdapter(new ArrayList<Preview>(), this);
        mMovieReviewAdapter = new MovieReviewAdapter(new ArrayList<Review>());
        mMovieInteractor = new MovieInteractor();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_movie, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mMoviePresenter = new MoviePresenter(view);

        mMarkAsFavorite = (Button) view.findViewById(R.id.mark_as_favorite);
        mUnfavorite = (Button) view.findViewById(R.id.remove_from_favorite);
        mPreviewsTitle = (TextView) view.findViewById(R.id.previews_title);
        mReviewsTitle = (TextView) view.findViewById(R.id.reviews_title);

        mPreviewLinearLayout = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerViewPreviews = (RecyclerView) view.findViewById(R.id.preview_recycler);
        mRecyclerViewPreviews.setHasFixedSize(false);
        mRecyclerViewPreviews.setLayoutManager(mPreviewLinearLayout);
        mRecyclerViewPreviews.setAdapter(mMoviePreviewAdapter);

        mReviewLinearLayout = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerViewReviews = (RecyclerView) view.findViewById(R.id.reviews_recycler);
        mRecyclerViewReviews.setHasFixedSize(false);
        mRecyclerViewReviews.setLayoutManager(mReviewLinearLayout);
        mRecyclerViewReviews.setAdapter(mMovieReviewAdapter);

        DividerItemDecoration previewDivider = new DividerItemDecoration(mRecyclerViewPreviews.getContext(),
                mPreviewLinearLayout.getOrientation());
        mRecyclerViewPreviews.addItemDecoration(previewDivider);

        DividerItemDecoration reviewDivider = new DividerItemDecoration(mRecyclerViewReviews.getContext(),
                mReviewLinearLayout.getOrientation());
        mRecyclerViewReviews.addItemDecoration(reviewDivider);

        movieView();

    }

    private void movieView() {

        mMoviePresenter.present(mMovie);

        updateFavoriteButton();

        // Previews
        fetchPreviews();

        // Reviews
        fetchReviews();

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        ArrayList<Preview> previews = mMoviePreviewAdapter.getPreviews();
        if (previews != null && !previews.isEmpty()) {
            savedInstanceState.putParcelableArrayList(PREVIEWS_EXTRA, previews);
        }
        ArrayList<Review> reviews = mMovieReviewAdapter.getReviews();
        if (reviews != null && !reviews.isEmpty()) {
            savedInstanceState.putParcelableArrayList(REVIEWS_EXTRA, reviews);
        }
    }

    private boolean isFavorite() {
        Cursor cursor = getContext().getContentResolver().query(
                MovieProvider.Movies.CONTENT_URI,
                new String[]{MovieColumns.MOVIE_ID,MovieColumns.IS_FAVORITE},
                MovieColumns.MOVIE_ID + " = " + mMovie.getId(),
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            cursor.close();
            return true;
        } else {
            return false;
        }
    }

    private void updateFavoriteButton() {

        new AsyncTask<Void, Void, Boolean>() {

            @Override
            protected Boolean doInBackground(Void... params) {
                return isFavorite();
            }

            @Override
            protected void onPostExecute(Boolean favorite) {
                if (favorite) {
                    mUnfavorite.setVisibility(VISIBLE);
                    mMarkAsFavorite.setVisibility(GONE);
                    // Set remove from favorites
                } else {
                    mMarkAsFavorite.setVisibility(VISIBLE);
                    mUnfavorite.setVisibility(GONE);
                    // Set add to favorite
                }
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        mUnfavorite.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        removeFromFavorite();
                        Snackbar.make(v,"Removed from Favorites", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                });

        mMarkAsFavorite.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        markAsFavorite();
                        Snackbar.make(v,"Added to Favorites", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                });
    }

    private void removeFromFavorite() {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                if (isFavorite()) {
                    getContext().getContentResolver().delete(
                            MovieProvider.Movies.CONTENT_URI,
                            MovieColumns.MOVIE_ID + " = " + mMovie.getId(),
                            null);
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void v) {
                updateFavoriteButton();

            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void markAsFavorite() {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                if (!isFavorite()) {
                    ContentValues movieValues = new ContentValues();
                    movieValues.put(MovieColumns.IS_FAVORITE, 1);
                    movieValues.put(MovieColumns.MOVIE_ID, mMovie.getId());
                    movieValues.put(MovieColumns.MOVIE_TITLE, mMovie.getTitle());
                    movieValues.put(MovieColumns.OVERVIEW, mMovie.getOverview());
                    movieValues.put(MovieColumns.POPULARITY, mMovie.getPopularity());
                    movieValues.put(MovieColumns.POSTER_PATH, mMovie.getPosterPath());
                    movieValues.put(MovieColumns.RELEASE_DATE, mMovie.getReleaseDate());
                    movieValues.put(MovieColumns.RATING, mMovie.getVoteAverage());

                    getContext().getContentResolver().insert(
                            MovieProvider.Movies.CONTENT_URI,
                            movieValues
                    );
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void v) {
                updateFavoriteButton();

            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void onPreviewsFetchFinished(List<Preview> previews) {
        mMoviePreviewAdapter.add(previews);
        if (previews.size() == 0) {
            mPreviewsTitle.setVisibility(GONE);
        } else {
            mPreviewsTitle.setVisibility(VISIBLE);
        }
    }

    private void onReviewsFetchFinished(List<Review> reviews) {
        mMovieReviewAdapter.add(reviews);
        if (reviews.size() == 0) {
            mReviewsTitle.setVisibility(GONE);
        } else {
            mReviewsTitle.setVisibility(VISIBLE);
        }
    }

    private void fetchPreviews() {
        mCompositeSubscription = new CompositeSubscription();
        mCompositeSubscription.add(mMovieInteractor.getPreviews(mMovie)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Preview>>() {
                    @Override
                    public void call(final List<Preview> previews) {
                        onPreviewsFetchFinished(previews);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(final Throwable throwable) {
                        Toast.makeText(getContext(), "Failed to fetch reviews", Toast.LENGTH_SHORT).show();
                    }
                }
                ));
    }

    private void fetchReviews() {
        mCompositeSubscription = new CompositeSubscription();
        mCompositeSubscription.add(mMovieInteractor.getReviews(mMovie)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Review>>() {
                               @Override
                               public void call(final List<Review> reviews) {
                                   onReviewsFetchFinished(reviews);
                               }
                           }, new Action1<Throwable>() {
                               @Override
                               public void call(final Throwable throwable) {
                                   Toast.makeText(getContext(), "Failed to fetch reviews", Toast.LENGTH_SHORT).show();
                               }
                           }
                ));
    }

    @Override
    public void view(Preview preview) {
        Intent youTubeIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + preview.getKey()));
        Intent youTubeWebIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + preview.getKey()));
        try {
            startActivity(youTubeIntent);
        } catch (ActivityNotFoundException e) {
            startActivity(youTubeWebIntent);
        }
    }

    @Override
    public void onPause() {
        mCompositeSubscription.unsubscribe();
        super.onPause();
    }
}
