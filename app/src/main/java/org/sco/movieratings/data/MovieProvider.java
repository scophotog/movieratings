package org.sco.movieratings.data;

import android.net.Uri;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.InexactContentUri;
import net.simonvt.schematic.annotation.TableEndpoint;

/**
 * Created by sargenzi on 1/16/17.
 */

@ContentProvider(authority = MovieProvider.AUTHORITY, database = MovieDatabase.class)
public final class MovieProvider {

    public static final String AUTHORITY =
            "org.sco.movieratings.data.MovieProvider";

    static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    interface Path{
        String MOVIES = "movies";
    }

    private static Uri buildUri(String... paths) {
        Uri.Builder builder = BASE_CONTENT_URI.buildUpon();
        for (String path : paths) {
            builder.appendPath(path);
        }
        return builder.build();
    }

    @TableEndpoint(table = MovieDatabase.MOVIES)
    public static class Movies {
        @ContentUri(
                path = Path.MOVIES,
                type = "vnc.android.cursor.dir/movies")
        public static final Uri CONTENT_URI = buildUri(Path.MOVIES);

        @InexactContentUri(
                name = "MOVIE_ID",
                path = Path.MOVIES + "/#",
                type = "vnc.android.cursor.dir/movies",
                whereColumn = MovieColumns.MOVIE_ID,
                pathSegment = 1)
        public static Uri withId(int id) {
            return buildUri(Path.MOVIES, String.valueOf(id));
        }

        @InexactContentUri(
                name = "MOVIE_FAVORITES",
                path = Path.MOVIES + "/favorites",
                type = "vnc.android.cursor.dir/movies",
                whereColumn = MovieColumns.IS_FAVORITE + 1,
                pathSegment = 1)
        public static Uri favorites() {
            return buildUri(Path.MOVIES);
        }
    }

}
