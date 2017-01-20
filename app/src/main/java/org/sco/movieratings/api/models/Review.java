package org.sco.movieratings.api.models;

import java.io.Serializable;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

public class Review implements Serializable, Parcelable {

    private String id;
    private String author;
    private String content;
    private String url;

    public Review() {}

    public Review(@NonNull String id,
                  @NonNull String author,
                  @NonNull String content,
                  @NonNull String url) {
        this.id = id;
        this.author = author;
        this.content = content;
        this.url = url;
    }

    private Review(Parcel in) {
        this.id = in.readString();
        this.author = in.readString();
        this.content = in.readString();
        this.url = in.readString();
    }

    @NonNull
    public String getId() {
        return id;
    }

    @NonNull
    public String getAuthor() {
        return author;
    }

    @NonNull
    public String getContent() {
        return content;
    }

    @NonNull
    public String getUrl() {
        return url;
    }

    public Review setId(String id) {
        this.id = id;
        return this;
    }

    public Review setAuthor(String author) {
        this.author = author;
        return this;
    }

    public Review setContent(String content) {
        this.content = content;
        return this;
    }

    public Review setUrl(String url) {
        this.url = url;
        return this;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String toString() { return url; }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.id);
        parcel.writeString(this.author);
        parcel.writeString(this.content);
        parcel.writeString(this.url);
    }

    public final static Creator<Review> CREATOR = new Creator<Review>() {
        @Override
        public Review createFromParcel(Parcel parcel) {
            return new Review(parcel);
        }

        @Override
        public Review[] newArray(int i) {
            return new Review[i];
        }

    };
}
