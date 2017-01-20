package org.sco.movieratings.api.models;

import java.io.Serializable;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

public class Preview implements Parcelable, Serializable {

    private String id;
    private String iso_639_1;
    private String iso_3166_1;
    private String key;
    private String name;
    private String site;
    private int size;
    private String type;

    public Preview() {}

    public Preview(@NonNull String id,
                   @NonNull String iso_639_1,
                   @NonNull String iso_3166_1,
                   @NonNull String key,
                   @NonNull String name,
                   @NonNull String site,
                   @NonNull int size,
                   @NonNull String type) {

        this.id = id;
        this.iso_639_1 = iso_639_1;
        this.iso_3166_1 = iso_3166_1;
        this.key = key;
        this.name = name;
        this.site = site;
        this.size =size;
        this.type = type;
    }

    private Preview(Parcel in) {
        this.id = in.readString();
        this.iso_639_1 = in.readString();
        this.iso_3166_1 = in.readString();
        this.key = in.readString();
        this.name = in.readString();
        this.site = in.readString();
        this.size = in.readInt();
        this.type = in.readString();
    }

    @NonNull
    public String getId() {
        return id;
    }

    @NonNull
    public String getKey() {
        return key;
    }

    @NonNull
    public String getName() {
        return name;
    }

    @NonNull
    public String getSite() {
        return site;
    }

    @NonNull
    public int getSize() {
        return size;
    }

    @NonNull
    public String getIso_639_1() {
        return iso_639_1;
    }

    @NonNull
    public String getIso_3166_1() {
        return iso_3166_1;
    }

    @NonNull
    public String getType() {
        return type;
    }

    
    public Preview setId(String id) {
        this.id = id;
        return this;
    }

    public Preview setIso_639_1(String iso_639_1) {
        this.iso_639_1 = iso_639_1;
        return this;
    }

    public Preview setIso_3166_1(String iso_3166_1) {
        this.iso_3166_1 = iso_3166_1;
        return this;
    }

    public Preview setKey(String key) {
        this.key = key;
        return this;
    }

    public Preview setName(String name) {
        this.name = name;
        return this;
    }

    public Preview setSite(String site) {
        this.site = site;
        return this;
    }

    public Preview setSize(int size) {
        this.size = size;
        return this;
    }

    public Preview setType(String type) {
        this.type = type;
        return this;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String toString() { return site + " " + key + " " + name; }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.id);
        parcel.writeString(this.iso_639_1);
        parcel.writeString(this.iso_3166_1);
        parcel.writeString(this.key);
        parcel.writeString(this.name);
        parcel.writeString(this.site);
        parcel.writeInt(this.size);
        parcel.writeString(this.type);
    }

    public final static Creator<Preview> CREATOR = new Creator<Preview>() {
        @Override
        public Preview createFromParcel(Parcel parcel) {
            return new Preview(parcel);
        }

        @Override
        public Preview[] newArray(int i) {
            return new Preview[i];
        }

    };
}
