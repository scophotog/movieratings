package org.sco.movieratings.data.models;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import org.sco.movieratings.data.MovieColumns;

public final class Movie implements Parcelable {

    String title;
    String poster_path;
    String overview;
    String release_date;
    Double popularity;
    Double vote_average;
    int movie_id;
    Boolean is_favorite;

    public Movie() {}

    public Movie(String title,
                 String poster_path,
                 String overview,
                 String release_date,
                 Double popularity,
                 Double vote_average,
                 int movie_id,
                 Boolean is_favorite) {

        this.title = title;
        this.poster_path = poster_path;
        this.overview = overview;
        this.release_date = release_date;
        this.popularity = popularity;
        this.vote_average = vote_average;
        this.movie_id = movie_id;
        this.is_favorite = is_favorite;
    }

    protected Movie(Parcel in) {
        vote_average = in.readDouble();
        popularity = in.readDouble();
        poster_path = in.readString();
        title = in.readString();
        overview = in.readString();
        release_date = in.readString();
        movie_id = in.readInt();
    }

    public Movie setMovieTitle(String title) {
        this.title = title;
        return this;
    }

    public String getMovieTitle() {
        return title;
    }

    public Movie setPosterPath(String path) {
        this.poster_path = path;
        return this;
    }

    public String getPosterPath() {
        return poster_path;
    }

    public Movie setOverview(String overview) {
        this.overview = overview;
        return this;
    }

    public String getOverview() {
        return overview;
    }

    public Movie setReleaseDate(String release_date) {
        this.release_date = release_date;
        return this;
    }

    public String getReleaseDate() {
        return release_date;
    }

    public Movie setPopularity(Double popularity) {
        this.popularity = popularity;
        return this;
    }

    public Double getPopularity() {
        return popularity;
    }

    public Movie setVoteAverage(Double vote_average) {
        this.vote_average = vote_average;
        return this;
    }

    public Double getVoteAverage() {
        return vote_average;
    }

    public Movie setMovieId(int movie_id) {
        this.movie_id = movie_id;
        return this;
    }

    public int getMovieId() {
        return movie_id;
    }

    public Movie setFavorite(int favorite) {
        this.is_favorite = favorite == 1 ? true : false;
        return this;
    }

    public int getFavorite() {
        if(this.is_favorite) {
            return 1;
        } else {
            return 0;
        }
    }

    public static Movie fromCursor(Cursor c) {
        Movie m = new Movie();
        m.setMovieTitle(c.getString(c.getColumnIndex(MovieColumns.MOVIE_TITLE)));
        m.setMovieId(c.getInt(c.getColumnIndex(MovieColumns.MOVIE_ID)));
        m.setOverview(c.getString(c.getColumnIndex(MovieColumns.OVERVIEW)));
        m.setPopularity(c.getDouble(c.getColumnIndex(MovieColumns.POPULARITY)));
        m.setReleaseDate(c.getString(c.getColumnIndex(MovieColumns.RELEASE_DATE)));
        m.setVoteAverage(c.getDouble(c.getColumnIndex(MovieColumns.RATING)));
        m.setPosterPath(c.getString(c.getColumnIndex(MovieColumns.POSTER_PATH)));
        m.setFavorite(c.getInt(c.getColumnIndex(MovieColumns.IS_FAVORITE)));
        return m;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String toString() { return title + " " + vote_average + " " + popularity; }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeDouble(vote_average);
        parcel.writeDouble(popularity);
        parcel.writeString(poster_path);
        parcel.writeString(title);
        parcel.writeString(overview);
        parcel.writeString(release_date);
        parcel.writeInt(movie_id);
    }

    public final static Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel parcel) {
            return new Movie(parcel);
        }

        @Override
        public Movie[] newArray(int i) {
            return new Movie[i];
        }

    };
}
