package org.sco.movieratings;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by sargenzi on 12/2/16.
 */

public class Movie implements Parcelable {
    private static final String IMAGE_PATH = "http://image.tmdb.org/t/p/w185";

    String title;
    String poster_path;
    String overview;
    String release_date;
    String popularity;
    String vote_average;

    public Movie(String title,
                 String poster_path,
                 String overview,
                 String release_date,
                 String popularity,
                 String vote_average) {

        this.title = title;
        this.poster_path = IMAGE_PATH + poster_path;
        this.overview = overview;
        this.release_date = release_date;
        this.popularity = popularity;
        this.vote_average = vote_average;
    }

    private Movie(Parcel in) {
        vote_average = in.readString();
        popularity = in.readString();
        poster_path = in.readString();
        title = in.readString();
        overview = in.readString();
        release_date = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String toString() { return title + " " + vote_average + " " + popularity; }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(vote_average);
        parcel.writeString(popularity);
        parcel.writeString(poster_path);
        parcel.writeString(title);
        parcel.writeString(overview);
        parcel.writeString(release_date);
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
