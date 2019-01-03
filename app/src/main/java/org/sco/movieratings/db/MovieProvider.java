package org.sco.movieratings.db;

import android.net.Uri;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.TableEndpoint;

@ContentProvider(authority = MovieProvider.AUTHORITY, database = MovieDatabase.class)
public final class MovieProvider {

    public static final String AUTHORITY =
            "org.sco.movieratings.db.MovieProvider";

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
    }

}
