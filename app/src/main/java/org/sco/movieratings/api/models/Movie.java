package org.sco.movieratings.api.models;

import java.io.Serializable;
import java.util.List;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import org.sco.movieratings.db.MovieColumns;

import com.google.gson.annotations.SerializedName;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class Movie implements Serializable, Parcelable {

    private String posterPath;
    @SerializedName("adult") private Boolean isAdult;
    private String overview;
    private String releaseDate;
    private List<Integer> genreIds;
    private Integer id;
    private String originalTitle;
    private String originalLanguage;
    private String title;
    private String backdropPath;
    private Double popularity;
    private Integer voteCount;
    @SerializedName("video") private Boolean hasVideo;
    private Double voteAverage;

    public Movie() {}

    public Movie(@Nullable String posterPath,
                 @NonNull Boolean isAdult,
                 @NonNull String overview,
                 @NonNull String releaseDate,
                 @NonNull List<Integer> genreIds,
                 @NonNull Integer id,
                 @NonNull String originalTitle,
                 @NonNull String originalLanguage,
                 @NonNull String title,
                 @Nullable String backdropPath,
                 @NonNull Double popularity,
                 @NonNull Integer voteCount,
                 @NonNull Boolean hasVideo,
                 @NonNull Double voteAverage) {

        this.posterPath = posterPath;//
        this.isAdult = isAdult;
        this.overview = overview;
        this.releaseDate = releaseDate;//
        this.genreIds = genreIds;
        this.id = id; //
        this.originalTitle = originalTitle;
        this.originalLanguage = originalLanguage;
        this.title = title;//
        this.backdropPath = backdropPath;
        this.popularity = popularity;//
        this.voteCount = voteCount;
        this.hasVideo = hasVideo;
        this.voteAverage= voteAverage;//
    }

    // TODO: Implement these items in the db
    private Movie(Parcel in) {
        this.posterPath = in.readString();
//        this.isAdult = in.readByte() != 0;
        this.overview = in.readString();
        this.releaseDate = in.readString();
//        this.genreIds = (ArrayList<Integer>) in.readSerializable();
        this.id = in.readInt();
//        this.originalTitle = in.readString();
//        this.originalLanguage = in.readString();
        this.title = in.readString();
//        this.backdropPath = in.readString();
        this.popularity = in.readDouble();
//        this.voteCount = in.readInt();
//        this.hasVideo = in.readByte() != 0;
        this.voteAverage = in.readDouble();
    }

    public Movie setPosterPath(String posterPath) {
        this.posterPath = posterPath;
        return this;
    }

    public Movie setAdult(Boolean adult) {
        isAdult = adult;
        return this;
    }

    public Movie setOverview(String overview) {
        this.overview = overview;
        return this;
    }

    public Movie setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
        return this;
    }

    public Movie setGenreIds(List<Integer> genreIds) {
        this.genreIds = genreIds;
        return this;
    }

    public Movie setId(Integer id) {
        this.id = id;
        return this;
    }

    public Movie setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
        return this;
    }

    public Movie setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
        return this;
    }

    public Movie setTitle(String title) {
        this.title = title;
        return this;
    }

    public Movie setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
        return this;
    }

    public Movie setPopularity(Double popularity) {
        this.popularity = popularity;
        return this;
    }

    public Movie setVoteCount(Integer voteCount) {
        this.voteCount = voteCount;
        return this;
    }

    public Movie setHasVideo(Boolean hasVideo) {
        this.hasVideo = hasVideo;
        return this;
    }

    public Movie setVoteAverage(Double voteAverage) {
        this.voteAverage = voteAverage;
        return this;
    }

    @NonNull
    public Double getVoteAverage() {
        return voteAverage;
    }

    @Nullable
    public String getPosterPath() {
        return posterPath;
    }

    @NonNull
    public Boolean getAdult() {
        return isAdult;
    }

    @NonNull
    public String getOverview() {
        return overview;
    }

    @NonNull
    public String getReleaseDate() {
        return releaseDate;
    }

    @NonNull
    public List<Integer> getGenreIds() {
        return genreIds;
    }

    @NonNull
    public Integer getId() {
        return id;
    }

    @NonNull
    public String getOriginalTitle() {
        return originalTitle;
    }

    @NonNull
    public String getOriginalLanguage() {
        return originalLanguage;
    }

    @NonNull
    public String getTitle() {
        return title;
    }

    @Nullable
    public String getBackdropPath() {
        return backdropPath;
    }

    @NonNull
    public Double getPopularity() {
        return popularity;
    }

    @NonNull
    public Integer getVoteCount() {
        return voteCount;
    }

    @NonNull
    public Boolean getHasVideo() {
        return hasVideo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.posterPath);
//        dest.writeByte((byte) (this.isAdult ? 1 : 0));
        dest.writeString(this.overview);
        dest.writeString(this.releaseDate);
//        dest.writeSerializable((Serializable) this.genreIds);
        dest.writeInt(this.id);
//        dest.writeString(this.originalTitle);
//        dest.writeString(this.originalLanguage);
        dest.writeString(this.title);
//        dest.writeString(this.backdropPath);
        dest.writeDouble(this.popularity);
//        dest.writeInt(this.voteCount);
//        dest.writeByte((byte) (this.hasVideo ? 1 : 0));
        dest.writeDouble(this.voteAverage);
    }

    public static Movie fromCursor(Cursor c) {
        Movie m = new Movie();
        m.setTitle(c.getString(c.getColumnIndex(MovieColumns.MOVIE_TITLE)));
        m.setId(c.getInt(c.getColumnIndex(MovieColumns.MOVIE_ID)));
        m.setOverview(c.getString(c.getColumnIndex(MovieColumns.OVERVIEW)));
        m.setPopularity(c.getDouble(c.getColumnIndex(MovieColumns.POPULARITY)));
        m.setReleaseDate(c.getString(c.getColumnIndex(MovieColumns.RELEASE_DATE)));
        m.setVoteAverage(c.getDouble(c.getColumnIndex(MovieColumns.RATING)));
        m.setPosterPath(c.getString(c.getColumnIndex(MovieColumns.POSTER_PATH)));
        return m;
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
