package org.sco.movieratings;

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
import org.sco.movieratings.data.models.Preview;

public class FetchPreviewsTask extends AsyncTask<String, Void, List<Preview>> {

    public static String LOG_TAG = FetchPreviewsTask.class.getSimpleName();
    private final Listener mListener;

    interface Listener {
        void onPreviewsFetchFinished(List<Preview> previews);
    }

    public FetchPreviewsTask(Listener listener) {
        mListener = listener;
    }

    @Override
    protected List<Preview> doInBackground(String... params) {
        if (params.length == 0) {
            return null;
        }

        String movieId = params[0];

        String moviesJsonStr = null;

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        try {
            final String BASE_URL = "http://api.themoviedb.org/3/movie";
            final String API_KEY = "api_key";
            Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                    .appendPath(params[0])
                    .appendPath("videos")
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
            return getMovieDataFromJson(moviesJsonStr);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(List<Preview> previews) {
        if (previews != null) {
            mListener.onPreviewsFetchFinished(previews);
        } else {
            mListener.onPreviewsFetchFinished(new ArrayList<Preview>());
        }
    }

    private List<Preview> getMovieDataFromJson(String movieJsonStr)
            throws JSONException {

        final String MDB_RESULTS = "results";
        final String MDB_ID = "id";
        final String MDB_KEY = "key";
        final String MDB_NAME = "name";
        final String MDB_SITE = "site";
        final String MDB_SIZE = "size";
        final String MDB_TYPE = "type";

        JSONObject moviesJson = new JSONObject(movieJsonStr);
        JSONArray moviesArray = moviesJson.getJSONArray(MDB_RESULTS);

        List<Preview> movieResults = new ArrayList<Preview>();

        for(int i = 0; i < moviesArray.length(); i++) {

            String id;
            String key;
            String name;
            String site;
            int size;
            String type;

            JSONObject moviePreviewResult = moviesArray.getJSONObject(i);

            id = moviePreviewResult.getString(MDB_ID);
            key = moviePreviewResult.getString(MDB_KEY);
            name = moviePreviewResult.getString(MDB_NAME);
            site = moviePreviewResult.getString(MDB_SITE);
            size = moviePreviewResult.getInt(MDB_SIZE);
            type = moviePreviewResult.getString(MDB_TYPE);
            movieResults.add(new Preview(id, key, name, site, size, type));

        }

        return movieResults;

    }
}
