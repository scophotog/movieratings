package org.sco.movieratings.data.models;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

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

    public String getMovieTitle() {
        return title;
    }

    public String getPosterPath() {
        return poster_path;
    }

    public String getOverview() {
        return overview;
    }

    public String getReleaseDate() {
        return release_date;
    }

    public Double getPopularity() {
        return popularity;
    }

    public Double getVoteAverage() {
        return vote_average;
    }

    public int getMovieId() {
        return movie_id;
    }

    public static Movie fromCursor(Cursor cursor) {
        Movie movie = new Movie();
        return movie;
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
