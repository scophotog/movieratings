package org.sco.movieratings.data;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;

/**
 * Created by sargenzi on 1/16/17.
 */

public interface MovieColumns {

    @DataType(DataType.Type.INTEGER) @PrimaryKey
    @AutoIncrement
    String _ID = "_id";

    @DataType(DataType.Type.INTEGER) @NotNull
    String MOVIE_ID = "movie_id";

    @DataType(DataType.Type.TEXT) @NotNull
    String MOVIE_TITLE = "movie_title";

    @DataType(DataType.Type.TEXT) @NotNull
    String OVERVIEW = "overview";

    @DataType(DataType.Type.TEXT) @NotNull
    String POSTER_PATH = "poster_path";

    @DataType(DataType.Type.TEXT) @NotNull
    String RELEASE_DATE = "release_date";

    @DataType(DataType.Type.REAL) @NotNull
    String RATING = "vote_average";

    @DataType(DataType.Type.REAL) @NotNull
    String POPULARITY = "popularity";

    @DataType(DataType.Type.INTEGER) @NotNull
    String IS_FAVORITE = "is_favorite";

}
