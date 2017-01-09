package org.sco.movieratings;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Vector;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.sco.movieratings.data.MovieContract.MovieEntry;

public class FetchMovieTask extends AsyncTask<String, Void, Void> {

    private final String LOG_TAG = FetchMovieTask.class.getSimpleName();

    private final Context mContext;

    FetchMovieTask(Context context) {
        mContext = context;
    }

    private void getMovieDataFromJson(String movieJsonStr)
            throws JSONException {

        final String MDB_RESULTS = "results";
        final String MDB_POSTER_PATH = "poster_path";
        final String MDB_OVERVIEW = "overview";
        final String MDB_RELEASE_DATE = "release_date";
        final String MDB_TITLE = "title";
        final String MDB_POPULARITY = "popularity";
        final String MDB_VOTE_AVERAGE = "vote_average";
        final String MDB_MOVIE_ID = "id";

        final String IMAGE_PATH = "http://image.tmdb.org/t/p/w185";

        try {
            JSONObject moviesJson = new JSONObject(movieJsonStr);
            JSONArray moviesArray = moviesJson.getJSONArray(MDB_RESULTS);

            Vector<ContentValues> cVVector = new Vector<ContentValues>(moviesArray.length());

            for (int i = 0; i < moviesArray.length(); i++) {

                String posterPath;
                String overview;
                String releaseDate;
                String title;
                String popularity;
                String averageRating;
                String id;

                JSONObject singleMovie = moviesArray.getJSONObject(i);

                posterPath = IMAGE_PATH + singleMovie.getString(MDB_POSTER_PATH);
                overview = singleMovie.getString(MDB_OVERVIEW);
                releaseDate = singleMovie.getString(MDB_RELEASE_DATE);
                title = singleMovie.getString(MDB_TITLE);
                popularity = singleMovie.getString(MDB_POPULARITY);
                averageRating = singleMovie.getString(MDB_VOTE_AVERAGE);
                id = singleMovie.getString(MDB_MOVIE_ID);

                ContentValues movieValues = new ContentValues();
                movieValues.put(MovieEntry.COLUMN_POSTER_PATH, posterPath);
                movieValues.put(MovieEntry.COLUMN_OVERVIEW, overview);
                movieValues.put(MovieEntry.COLUMN_RELEASE_DATE, releaseDate);
                movieValues.put(MovieEntry.COLUMN_MOVIE_TITLE, title);
                movieValues.put(MovieEntry.COLUMN_RATING, averageRating);
                movieValues.put(MovieEntry.COLUMN_POPULARITY, popularity);
                movieValues.put(MovieEntry.COLUMN_MOVIE_ID, id);
                movieValues.put(MovieEntry.COLUMN_IS_FAVORITE, "N"); // Default to N

                cVVector.add(movieValues);
            }

            int inserted = 0;

            if (cVVector.size() > 0) {
                ContentValues[] cvArray = new ContentValues[cVVector.size()];
                cVVector.toArray(cvArray);
                inserted = mContext.getContentResolver().bulkInsert(MovieEntry.CONTENT_URI, cvArray);
            }

            Log.d(LOG_TAG, "FetchMovieTask Complete. " + inserted + " inserted.");
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
    }

    @Override
    protected Void doInBackground(String... params) {

        if (params.length == 0) {
            return null;
        }

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String moviesJsonStr = null;

        try {
            final String BASE_URL = "http://api.themoviedb.org/3/movie";
            final String POPULAR_MOVIE_BASE_URL = BASE_URL + "/popular?";
            final String TOP_RATED_MOVIE_BASE_URL = BASE_URL + "/top_rated?";
            final String API_KEY = "api_key";

            Uri builtUri;

            if (params[0].equals("most_popular")) {
                builtUri = Uri.parse(POPULAR_MOVIE_BASE_URL).buildUpon()
                        .appendQueryParameter(API_KEY, BuildConfig.MOVIE_DB_API_KEY)
                        .build();
            } else if (params[0].equals("top_rated")) {
                builtUri = Uri.parse(TOP_RATED_MOVIE_BASE_URL).buildUpon()
                        .appendQueryParameter(API_KEY, BuildConfig.MOVIE_DB_API_KEY)
                        .build();
            } else {
                Log.e(LOG_TAG, "Unknown param: " + params[0]);
                return null;
            }

            URL url = new URL(builtUri.toString());

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                return null;
            }

            moviesJsonStr = buffer.toString();
            getMovieDataFromJson(moviesJsonStr);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }
        return null;
    }
}
