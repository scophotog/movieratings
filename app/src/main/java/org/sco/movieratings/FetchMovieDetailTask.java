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

public class FetchMovieDetailTask extends AsyncTask<String, Void, List<Preview>> {

    private final String LOG_TAG = FetchMovieDetailTask.class.getSimpleName();

    private final String BASE_URL = "http://api.themoviedb.org/3/movie";

    MoviePreviewAdapter mMoviewPreviewAdapter;

    FetchMovieDetailTask(MoviePreviewAdapter mpa) { this.mMoviewPreviewAdapter = mpa; }

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

    @Override
    protected List<Preview> doInBackground(String... params) {

        if (params.length == 0) { return null; }

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String moviesJsonStr = null;

        try {
            final String API_KEY = "api_key";

            // http://api.themoviedb.org/3/movie/278/videos?api_key=f7eff3ac535ebd1b6d1b1baf2939cfc2

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
    protected void onPostExecute(List<Preview> result) {
        if (result != null) {
            mMoviewPreviewAdapter.clear();
            for(Preview item : result) {
                mMoviewPreviewAdapter.add(item);
            }
        }
    }

    public MoviePreviewAdapter getResults() {
        return mMoviewPreviewAdapter;
    }
}
