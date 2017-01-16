package org.sco.movieratings.rest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.sco.movieratings.BuildConfig;
import org.sco.movieratings.data.models.Movie;
import org.sco.movieratings.ui.MovieListAdapter;

public class FetchMovieTask extends AsyncTask<String, Void, List<Movie>> {

    private final String LOG_TAG = FetchMovieTask.class.getSimpleName();

    private final String BASE_URL = "http://api.themoviedb.org/3/movie";
    private final String IMAGE_PATH = "http://image.tmdb.org/t/p/w185";

    MovieListAdapter mMovieListAdapter;

    public FetchMovieTask(MovieListAdapter myAdapter) {
        this.mMovieListAdapter = myAdapter;
    }

    private List<Movie> getMovieDataFromJson(String movieJsonStr)
            throws JSONException {

        final String MDB_RESULTS = "results";
        final String MDB_POSTER_PATH = "poster_path";
        final String MDB_OVERVIEW = "overview";
        final String MDB_RELEASE_DATE = "release_date";
        final String MDB_TITLE = "title";
        final String MDB_POPULARITY = "popularity";
        final String MDB_VOTE_AVERAGE = "vote_average";
        final String MDB_API_ID = "id";

        JSONObject moviesJson = new JSONObject(movieJsonStr);
        JSONArray moviesArray = moviesJson.getJSONArray(MDB_RESULTS);

        List<Movie> movieResults = new ArrayList<Movie>();

        for(int i = 0; i < moviesArray.length(); i++) {

            String posterPath;
            String overview;
            String releaseDate;
            String title;
            double popularity;
            double averageRating;
            int api_id;

            JSONObject singleMovie = moviesArray.getJSONObject(i);

            posterPath = IMAGE_PATH + singleMovie.getString(MDB_POSTER_PATH);
            overview = singleMovie.getString(MDB_OVERVIEW);
            releaseDate = singleMovie.getString(MDB_RELEASE_DATE);
            title = singleMovie.getString(MDB_TITLE);
            popularity = singleMovie.getDouble(MDB_POPULARITY);
            averageRating = singleMovie.getDouble(MDB_VOTE_AVERAGE);
            api_id = singleMovie.getInt(MDB_API_ID);
            movieResults.add(new Movie(title, posterPath, overview,
                    releaseDate, popularity, averageRating, api_id, false));
        }
        return movieResults;
    }

    @Override
    protected List<Movie> doInBackground(String... params) {

        if (params.length == 0) { return null; }

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String moviesJsonStr = null;

        try {
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
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            return null;
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

        try {
            return getMovieDataFromJson(moviesJsonStr);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(List<Movie> results) {
        if (results != null || results.size() > 0) {
            mMovieListAdapter.add(results);
        }
    }

    public MovieListAdapter getResults() {
        return mMovieListAdapter;
    }
}
