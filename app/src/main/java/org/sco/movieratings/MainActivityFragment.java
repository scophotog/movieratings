package org.sco.movieratings;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * The activity for displaying all movie posters.
 */

public class MainActivityFragment extends Fragment {

    private MovieAdapter mMovieAdapter;

    private ArrayList<Movie> movieList;

    public MainActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState == null || !savedInstanceState.containsKey("movies")) {
            movieList = new ArrayList<Movie>();
        } else {
            movieList = savedInstanceState.getParcelableArrayList("movies");
        }
        setHasOptionsMenu(true);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("movies", movieList);
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mMovieAdapter = new MovieAdapter(getActivity(), movieList);

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        GridView gridView = (GridView) rootView.findViewById(R.id.movielist_view);
        gridView.setAdapter(mMovieAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Movie movie = mMovieAdapter.getItem(i);

                Intent intent = new Intent(getActivity(),DetailActivity.class);
                intent.putExtra("movie", movie);

                startActivity(intent);
            }

        });

        return rootView;
    }

    private void updateMovies() {
        FetchMovieTask movieTask = new FetchMovieTask();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sortType = prefs.getString(getString(R.string.pref_sort_key),
                getString(R.string.pref_sort_top_rated));
        movieTask.execute(sortType);

        if (sortType.equals("top_rated")) {
            ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.high_rated_settings));
        } else if (sortType.equals("most_popular")) {
            ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.most_popular_settings));
        } else {
            ((MainActivity) getActivity()).setActionBarTitle("");
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        updateMovies();
    }

    public class FetchMovieTask extends AsyncTask<String, Void, Movie[]> {

        private final String LOG_TAG = FetchMovieTask.class.getSimpleName();

        private final String BASE_URL = "http://api.themoviedb.org/3/movie";
        private final String IMAGE_PATH = "http://image.tmdb.org/t/p/w185";

        private Movie[] getMovieDataFromJson(String movieJsonStr)
            throws JSONException {

            final String MDB_RESULTS = "results";
            final String MDB_POSTER_PATH = "poster_path";
            final String MDB_OVERVIEW = "overview";
            final String MDB_RELEASE_DATE = "release_date";
            final String MDB_TITLE = "title";
            final String MDB_POPULARITY = "popularity";
            final String MDB_VOTE_AVERAGE = "vote_average";

            JSONObject moviesJson = new JSONObject(movieJsonStr);
            JSONArray moviesArray = moviesJson.getJSONArray(MDB_RESULTS);

            Movie[] movieResults = new Movie[20];

            for(int i = 0; i < moviesArray.length(); i++) {

                String posterPath;
                String overview;
                String releaseDate;
                String title;
                String popularity;
                String averageRating;

                JSONObject singleMovie = moviesArray.getJSONObject(i);

                posterPath = IMAGE_PATH + singleMovie.getString(MDB_POSTER_PATH);
                overview = singleMovie.getString(MDB_OVERVIEW);
                releaseDate = singleMovie.getString(MDB_RELEASE_DATE);
                title = singleMovie.getString(MDB_TITLE);
                popularity = singleMovie.getString(MDB_POPULARITY);
                averageRating = singleMovie.getString(MDB_VOTE_AVERAGE);
                movieResults[i] = new Movie(title, posterPath, overview,
                                            releaseDate, popularity, averageRating);

            }

            return movieResults;

        }

        @Override
        protected Movie[] doInBackground(String... params) {

            if (params.length == 0) { return null; }

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String moviesJsonStr = null;

            try {
                final String POPULAR_MOVIE_BASE_URL = BASE_URL + "/popular?";
                final String TOP_RATED_MOVIE_BASE_URL = BASE_URL + "/top_rated?";
                final String API_KEY = "api_key";

                Uri builtUri;

                if (params[0].equals("top_rated")) {
                    builtUri = Uri.parse(POPULAR_MOVIE_BASE_URL).buildUpon()
                            .appendQueryParameter(API_KEY, BuildConfig.MOVIE_DB_API_KEY)
                            .build();
                } else if (params[0].equals("most_popular")) {
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
        protected void onPostExecute(Movie[] result) {
            if (result != null) {
                mMovieAdapter.clear();
                for(Movie movieItem : result) {
                    mMovieAdapter.add(movieItem);
                }
            }
        }
    }
}
