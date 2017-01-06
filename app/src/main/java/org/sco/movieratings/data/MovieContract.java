package org.sco.movieratings.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class MovieContract {

    public static final String CONTENT_AUTHORITY = "org.sco.movieratings";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_MOVIES = "movies";

    public static final class MovieEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIES;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIES;

        // Table name
        public static final String TABLE_NAME = "movies";

        // ID Returned by the API
        public static final String COLUMN_MOVIE_ID = "movie_id";

        // Movie Title returned by the API
        public static final String COLUMN_MOVIE_TITLE = "movie_title";

        // Movie Poster Path returned by the API
        public static final String COLUMN_POSTER_PATH = "poster_path";

        // Movie Overview Returned By the API
        public static final String COLUMN_OVERVIEW = "overview";

        // Movie Release Date returned by the API
        public static final String COLUMN_RELEASE_DATE = "release_date";

        // Movie Rating returned by the API (vote_average)
        public static final String COLUMN_RATING = "rating";

        // Movie Rating returned by the API (vote_average)
        public static final String COLUMN_POPULARITY = "popularity";

        public static final String COLUMN_IS_FAVORITE = "is_favorite";

        public static Uri buildMovieUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

}
