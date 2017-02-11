package com.github.c4d3r.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Maxim on 10/02/2017.
 */

public class MovieTrailer implements Parcelable {

    private String id;

    private String key;

    private String name;

    private String site;

    private int size;

    private String type;

    public MovieTrailer(Parcel in) {
        id = in.readString();
        key = in.readString();
        name = in.readString();
        site = in.readString();
        size = in.readInt();
        type = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(key);
        parcel.writeString(name);
        parcel.writeString(site);
        parcel.writeInt(size);
        parcel.writeString(type);
    }

    public static final Parcelable.Creator<MovieTrailer> CREATOR = new Creator<MovieTrailer>() {
        @Override
        public MovieTrailer createFromParcel(Parcel source) {
            return new MovieTrailer(source);
        }

        @Override
        public MovieTrailer[] newArray(int size) {
            return new MovieTrailer[size];
        }
    };

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

    public String getUrl() {
        switch(this.getSite().toUpperCase()) {
            case "YOUTUBE":
                return "http://www.youtube.com/watch?v=" + this.getKey();
            default: return "http://www.google.com";
        }
    }
}
