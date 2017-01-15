package org.sco.movieratings.data.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Preview implements Parcelable {

    String id;
    String key;
    String name;
    String site;
    int size;
    String type;

    public Preview(String id,
                   String key,
                   String name,
                   String site,
                   int size,
                   String type) {

        this.id = id;
        this.key = key;
        this.name = name;
        this.site = site;
        this.size =size;
        this.type = type;
    }

    private Preview(Parcel in) {
        id = in.readString();
        key = in.readString();
        name = in.readString();
        site = in.readString();
        size = in.readInt();
        type = in.readString();
    }

    public String getId() {
        return id;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public String getSite() {
        return site;
    }

    public int getSize() {
        return size;
    }

    public String getType() {
        return type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String toString() { return site + " " + key + " " + name; }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(key);
        parcel.writeString(name);
        parcel.writeString(site);
        parcel.writeInt(size);
        parcel.writeString(type);
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
