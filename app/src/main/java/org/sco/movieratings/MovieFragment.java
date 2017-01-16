package org.sco.movieratings;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.sco.movieratings.data.MovieColumns;
import org.sco.movieratings.data.MovieProvider;
import org.sco.movieratings.data.models.Movie;
import org.sco.movieratings.data.models.Preview;
import org.sco.movieratings.rest.FetchPreviewsTask;

import com.squareup.picasso.Picasso;

public class MovieFragment extends Fragment implements
        FetchPreviewsTask.Listener, MoviePreviewAdapter.Callback {

    private static final String LOG_TAG = MovieFragment.class.getSimpleName();

    public static final String MOVIE = "movie";

    public static final String PREVIEWS_EXTRA = "PREVIEWS_EXTRA";

    TextView mMovieTitle;
    ImageView mMoviePoster;
    TextView mMovieDetails;
    TextView mMovieReleaseDate;
    TextView mMovieVoteAverage;
    Button mMarkAsFavorite;
    Button mUnfavorite;
    TextView mMovieTime;
    LinearLayoutManager mLinearLayout;

    private MoviePreviewAdapter mMoviePreviewAdapter;
    RecyclerView mRecyclerViewPreviews;
    Movie mMovie;

    public MovieFragment() {

    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMoviePreviewAdapter = new MoviePreviewAdapter(new ArrayList<Preview>(), this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Intent intent = getActivity().getIntent();

        if (intent != null) {
            mMovie = intent.getParcelableExtra(MOVIE);
        }

        View rootView = inflater.inflate(R.layout.activity_movie, container, false);
        mMovieTitle = (TextView) rootView.findViewById(R.id.movie_title);
        mMoviePoster = (ImageView) rootView.findViewById(R.id.poster);
        mMovieDetails = (TextView) rootView.findViewById(R.id.movie_details);
        mMovieReleaseDate = (TextView) rootView.findViewById(R.id.release_date);
        mMovieVoteAverage = (TextView) rootView.findViewById(R.id.vote_average);
        mMarkAsFavorite = (Button) rootView.findViewById(R.id.mark_as_favorite);
        mUnfavorite = (Button) rootView.findViewById(R.id.remove_from_favorite);
        mMovieTime = (TextView) rootView.findViewById(R.id.movie_time);

        mLinearLayout = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        mRecyclerViewPreviews = (RecyclerView) rootView.findViewById(R.id.preview_recycler);
        mRecyclerViewPreviews.setHasFixedSize(false);
        mRecyclerViewPreviews.setLayoutManager(mLinearLayout);
        mRecyclerViewPreviews.setAdapter(mMoviePreviewAdapter);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getActivity() instanceof MovieActivity) {
            MovieActivity activity = ((MovieActivity) getActivity());

            final Toolbar toolbar = (Toolbar) activity.findViewById(R.id.toolbar);
            activity.setSupportActionBar(toolbar);
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        movieView();

    }

    public void movieView() {
        mMovieTitle.setText(mMovie.getMovieTitle());
        mMovieTitle.setBackgroundResource(R.color.movieTitleBackground);
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
            if (cursor.getString(1).equals("Y")) {
                cursor.close();
                return true;
            } else {
                cursor.close();
                return false;
            }
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
                    mUnfavorite.setVisibility(View.VISIBLE);
                    mMarkAsFavorite.setVisibility(View.GONE);
                    // Set remove from favorites
                } else {
                    mMarkAsFavorite.setVisibility(View.VISIBLE);
                    mUnfavorite.setVisibility(View.GONE);
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
                    ContentValues args = new ContentValues();
                    args.put(MovieColumns.IS_FAVORITE, 0);
                    getContext().getContentResolver().update(
                            MovieProvider.Movies.CONTENT_URI,
                            args,
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
                    ContentValues args = new ContentValues();
                    args.put(MovieColumns.IS_FAVORITE, 1);
                    getContext().getContentResolver().update(
                            MovieProvider.Movies.CONTENT_URI,
                            args,
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

    @Override
    public void onPreviewsFetchFinished(List<Preview> previews) {
        mMoviePreviewAdapter.add(previews);
    }

    private void fetchPreviews() {
        FetchPreviewsTask fetchPreviewsTask = new FetchPreviewsTask(this);
        fetchPreviewsTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, mMovie.getMovieId());
    }

    @Override
    public void view(Preview preview, int position) {

    }
}
