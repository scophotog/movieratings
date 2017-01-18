package org.sco.movieratings;

import java.util.ArrayList;
import java.util.List;

import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import net.simonvt.schematic.annotation.PrimaryKey;

import org.sco.movieratings.data.MovieColumns;
import org.sco.movieratings.data.MovieProvider;
import org.sco.movieratings.data.models.Movie;
import org.sco.movieratings.data.models.Preview;
import org.sco.movieratings.data.models.Review;
import org.sco.movieratings.rest.FetchPreviewsTask;
import org.sco.movieratings.rest.FetchReviewsTask;

import com.squareup.picasso.Picasso;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class MovieFragment extends Fragment implements
        FetchPreviewsTask.Listener, FetchReviewsTask.Listener, MoviePreviewAdapter.Callback {

    private static final String LOG_TAG = MovieFragment.class.getSimpleName();

    public static final String MOVIE = "movie";

    public static final String PREVIEWS_EXTRA = "PREVIEWS_EXTRA";
    public static final String REVIEWS_EXTRA = "REVIEWS_EXTRA";

    TextView mMovieTitle;
    ImageView mMoviePoster;
    TextView mMovieDetails;
    TextView mMovieReleaseDate;
    TextView mMovieVoteAverage;
    Button mMarkAsFavorite;
    Button mUnfavorite;
    TextView mMovieTime;
    TextView mPreviewsTitle;
    TextView mReviewsTitle;
    LinearLayoutManager mPreviewLinearLayout;
    LinearLayoutManager mReviewLinearLayout;

    private MoviePreviewAdapter mMoviePreviewAdapter;
    private MovieReviewAdapter mMovieReviewAdapter;
    RecyclerView mRecyclerViewPreviews;
    RecyclerView mRecyclerViewReviews;
    Movie mMovie;

    public MovieFragment() {

    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMoviePreviewAdapter = new MoviePreviewAdapter(new ArrayList<Preview>(), this);
        mMovieReviewAdapter = new MovieReviewAdapter(new ArrayList<Review>());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        if (arguments != null) {
            mMovie = arguments.getParcelable(MovieFragment.MOVIE);
        }

        View rootView = inflater.inflate(R.layout.fragment_movie, container, false);

        mMovieTitle = (TextView) rootView.findViewById(R.id.movie_title);
        mMoviePoster = (ImageView) rootView.findViewById(R.id.poster);
        mMovieDetails = (TextView) rootView.findViewById(R.id.movie_details);
        mMovieReleaseDate = (TextView) rootView.findViewById(R.id.release_date);
        mMovieVoteAverage = (TextView) rootView.findViewById(R.id.vote_average);
        mMarkAsFavorite = (Button) rootView.findViewById(R.id.mark_as_favorite);
        mUnfavorite = (Button) rootView.findViewById(R.id.remove_from_favorite);
        mMovieTime = (TextView) rootView.findViewById(R.id.movie_time);
        mPreviewsTitle = (TextView) rootView.findViewById(R.id.previews_title);
        mReviewsTitle = (TextView) rootView.findViewById(R.id.reviews_title);

        mPreviewLinearLayout = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerViewPreviews = (RecyclerView) rootView.findViewById(R.id.preview_recycler);
        mRecyclerViewPreviews.setHasFixedSize(false);
        mRecyclerViewPreviews.setLayoutManager(mPreviewLinearLayout);
        mRecyclerViewPreviews.setAdapter(mMoviePreviewAdapter);

        mReviewLinearLayout = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerViewReviews = (RecyclerView) rootView.findViewById(R.id.reviews_recycler);
        mRecyclerViewReviews.setHasFixedSize(false);
        mRecyclerViewReviews.setLayoutManager(mReviewLinearLayout);
        mRecyclerViewReviews.setAdapter(mMovieReviewAdapter);

        DividerItemDecoration previewDivider = new DividerItemDecoration(mRecyclerViewPreviews.getContext(),
                mPreviewLinearLayout.getOrientation());
        mRecyclerViewPreviews.addItemDecoration(previewDivider);

        DividerItemDecoration reviewDivider = new DividerItemDecoration(mRecyclerViewReviews.getContext(),
                mReviewLinearLayout.getOrientation());
        mRecyclerViewReviews.addItemDecoration(reviewDivider);



        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (mMovie != null) {
            movieView();
        }

    }

    private void movieView() {
        mMovieTitle.setText(mMovie.getMovieTitle());
        mMovieDetails.setText(mMovie.getOverview());

        Picasso.with(getActivity())
                .load(mMovie.getPosterPath())
                .placeholder(R.drawable.loading)
                .error(R.drawable.image_not_found)
                .into(mMoviePoster);

        mMovieReleaseDate.setText(mMovie.getReleaseDate().split("-")[0]);
        mMovieVoteAverage.setText(mMovie.getVoteAverage() + "/10");
        mMovieTime.setText("120 min");

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
                MovieColumns.MOVIE_ID + " = " + mMovie.getMovieId(),
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
                            MovieColumns.MOVIE_ID + " = " + mMovie.getMovieId(),
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
                    movieValues.put(MovieColumns.MOVIE_ID, mMovie.getMovieId());
                    movieValues.put(MovieColumns.MOVIE_TITLE, mMovie.getMovieTitle());
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

    @Override
    public void onPreviewsFetchFinished(List<Preview> previews) {
        mMoviePreviewAdapter.add(previews);
        if (previews.size() == 0) {
            mReviewsTitle.setVisibility(GONE);
        } else {
            mReviewsTitle.setVisibility(VISIBLE);
        }
    }

    @Override
    public void onReviewsFetchFinished(List<Review> reviews) {
        mMovieReviewAdapter.add(reviews);
        if (reviews.size() == 0) {
            mPreviewsTitle.setVisibility(GONE);
        } else {
            mPreviewsTitle.setVisibility(VISIBLE);
        }
    }


    private void fetchPreviews() {
        FetchPreviewsTask fetchPreviewsTask = new FetchPreviewsTask(this);
        fetchPreviewsTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, mMovie.getMovieId());
    }

    private void fetchReviews() {
        FetchReviewsTask fetchReviewsTask = new FetchReviewsTask(this);
        fetchReviewsTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, mMovie.getMovieId());
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
}
