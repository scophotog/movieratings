package org.sco.movieratings;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.sco.movieratings.data.MovieContract;
import org.sco.movieratings.data.models.Preview;

import com.squareup.picasso.Picasso;

public class MovieFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>,
        FetchPreviewsTask.Listener, MoviePreviewAdapter.Callback {

    private static final String LOG_TAG = MovieFragment.class.getSimpleName();

    static final String MOVIE_URI = "URI";
    private Uri mUri;

    private Cursor mData;

    public static final String PREVIEWS_EXTRA = "PREVIEWS_EXTRA";

    private static final int MOVIE_LOADER = 0;

    private static final String[] MOVIE_COLUMNS = {
            MovieContract.MovieEntry.TABLE_NAME + "." + MovieContract.MovieEntry._ID,
            MovieContract.MovieEntry.COLUMN_MOVIE_ID,
            MovieContract.MovieEntry.COLUMN_MOVIE_TITLE,
            MovieContract.MovieEntry.COLUMN_POSTER_PATH,
            MovieContract.MovieEntry.COLUMN_RELEASE_DATE,
            MovieContract.MovieEntry.COLUMN_OVERVIEW,
            MovieContract.MovieEntry.COLUMN_RATING,
            MovieContract.MovieEntry.COLUMN_POPULARITY,
            MovieContract.MovieEntry.COLUMN_IS_FAVORITE
    };

    static final int COL_MOVIE_ID = 0;
    static final int COL_MOVIE_API_ID = 1;
    static final int COL_MOVIE_TITLE = 2;
    static final int COL_POSTER_PATH = 3;
    static final int COL_RELEASE_DATE = 4;
    static final int COL_OVERVIEW = 5;
    static final int COL_RATING = 6;
    static final int COL_POPULARITY = 7;
    static final int COL_IS_FAVORITE = 8;


    TextView mMovieTitle;
    ImageView mMoviePoster;
    TextView mMovieDetails;
    TextView mMovieReleaseDate;
    TextView mMovieVoteAverage;
    Button mMarkAsFavorite;
    Button mUnfavorite;

    private MoviePreviewAdapter mMoviePreviewAdapter;
    RecyclerView mRecyclerViewPreviews;

    public MovieFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        if (arguments != null) {
            mUri = arguments.getParcelable(MovieFragment.MOVIE_URI);
        }

        View rootView = inflater.inflate(R.layout.fragment_movie, container, false);
        mMovieTitle = (TextView) rootView.findViewById(R.id.movie_title);
        mMoviePoster = (ImageView) rootView.findViewById(R.id.poster);
        mMovieDetails = (TextView) rootView.findViewById(R.id.movie_details);
        mMovieReleaseDate = (TextView) rootView.findViewById(R.id.release_date);
        mMovieVoteAverage = (TextView) rootView.findViewById(R.id.vote_average);
        mMarkAsFavorite = (Button) rootView.findViewById(R.id.mark_as_favorite);
        mUnfavorite = (Button) rootView.findViewById(R.id.remove_from_favorite);
        mRecyclerViewPreviews = (RecyclerView) rootView.findViewById(R.id.preview_recycler);
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

    }

    public void movieView() {
        mMovieTitle.setText(mData.getString(COL_MOVIE_TITLE));
        mMovieDetails.setText(mData.getString(COL_OVERVIEW));

        Picasso.with(getActivity())
                .load(mData.getString(COL_POSTER_PATH))
                .placeholder(R.drawable.loading)
                .error(R.drawable.image_not_found)
                .into(mMoviePoster);

        mMovieReleaseDate.setText(mData.getString(COL_RELEASE_DATE).split("-")[0]);
        mMovieVoteAverage.setText(mData.getString(COL_RATING) + "/10");

        updateFavoriteButton();

        // Previews
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

        mRecyclerViewPreviews.setLayoutManager(layoutManager);
        mMoviePreviewAdapter = new MoviePreviewAdapter(new ArrayList<Preview>(), this);
        mRecyclerViewPreviews.setAdapter(mMoviePreviewAdapter);
        mRecyclerViewPreviews.setNestedScrollingEnabled(false);

        fetchPreviews();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(MOVIE_LOADER, null, this);
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
                MovieContract.MovieEntry.CONTENT_URI,
                new String[]{MovieContract.MovieEntry.COLUMN_MOVIE_ID,MovieContract.MovieEntry.COLUMN_IS_FAVORITE},
                MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = " + mData.getString(COL_MOVIE_API_ID),
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
                    args.put(MovieContract.MovieEntry.COLUMN_IS_FAVORITE, "N");
                    getContext().getContentResolver().update(
                            MovieContract.MovieEntry.CONTENT_URI,
                            args,
                            MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = " + mData.getString(COL_MOVIE_API_ID),
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
                    args.put(MovieContract.MovieEntry.COLUMN_IS_FAVORITE, "Y");
                    getContext().getContentResolver().update(
                            MovieContract.MovieEntry.CONTENT_URI,
                            args,
                            MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = " + mData.getString(COL_MOVIE_API_ID),
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
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.v(LOG_TAG, "In onCreateLoader");
        if (null != mUri) {
            return new CursorLoader(
                    getActivity(),
                    mUri,
                    MOVIE_COLUMNS,
                    null,
                    null,
                    null
            );
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.v(LOG_TAG, "In onLoadFinished");
        if (data != null && data.moveToFirst()) {
            mData = data;
            movieView();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.v(LOG_TAG, "In onLoaderReset");
    }

    @Override
    public void onPreviewsFetchFinished(List<Preview> previews) {
        mMoviePreviewAdapter.add(previews);
    }

    private void fetchPreviews() {
        FetchPreviewsTask fetchPreviewsTask = new FetchPreviewsTask(this);
        fetchPreviewsTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, mData.getString(COL_MOVIE_API_ID));
    }

    @Override
    public void view(Preview preview, int position) {

    }
}
