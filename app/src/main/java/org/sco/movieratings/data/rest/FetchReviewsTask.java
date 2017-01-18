package org.sco.movieratings.data.rest;

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
import org.sco.movieratings.data.models.Review;

public class FetchReviewsTask extends AsyncTask<Integer, Void, List<Review>> {

    public static String LOG_TAG = FetchReviewsTask.class.getSimpleName();
    private final Listener mListener;

    public interface Listener {
        void onReviewsFetchFinished(List<Review> previews);
    }

    public FetchReviewsTask(Listener listener) {
        mListener = listener;
    }

    @Override
    protected List<Review> doInBackground(Integer... params) {
        if (params.length == 0) {
            return null;
        }

        int movieId = params[0];

        String moviesJsonStr = null;

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        try {
            final String BASE_URL = "http://api.themoviedb.org/3/movie";
            final String API_KEY = "api_key";
            Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                    .appendPath(String.valueOf(movieId))
                    .appendPath("reviews")
                    .appendQueryParameter(API_KEY, BuildConfig.MOVIE_DB_API_KEY)
                    .build();

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
            return getReviewDataFromJson(moviesJsonStr);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(List<Review> reviews) {
        if (reviews != null) {
            mListener.onReviewsFetchFinished(reviews);
        } else {
            mListener.onReviewsFetchFinished(new ArrayList<Review>());
        }
    }

    private List<Review> getReviewDataFromJson(String movieJsonStr)
            throws JSONException {

        final String MDB_RESULTS = "results";
        final String MDB_ID = "id";
        final String MDB_AUTHOR = "author";
        final String MDB_CONTENT = "content";
        final String MDB_URL = "url";

        JSONObject moviesJson = new JSONObject(movieJsonStr);
        JSONArray moviesArray = moviesJson.getJSONArray(MDB_RESULTS);

        List<Review> movieReviewResults = new ArrayList<Review>();

        for(int i = 0; i < moviesArray.length(); i++) {

            String id;
            String author;
            String content;
            String url;

            JSONObject movieReviewResult = moviesArray.getJSONObject(i);

            id = movieReviewResult.getString(MDB_ID);
            author = movieReviewResult.getString(MDB_AUTHOR);
            content = movieReviewResult.getString(MDB_CONTENT);
            url = movieReviewResult.getString(MDB_URL);
            movieReviewResults.add(new Review(id, author, content, url));
        }

        return movieReviewResults;

    }
}
