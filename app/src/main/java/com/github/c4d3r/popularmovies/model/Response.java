package com.github.c4d3r.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Maxim on 01/02/2017.
 */

public class Response<T> implements Parcelable {

    private int id;

    private int page;

    private List<T> results;

    @SerializedName("total_results")
    private int totalResults;

    @SerializedName("total_pages")
    private int totalPages;

    public Response(Parcel in) {
        page = in.readInt();
        totalResults = in.readInt();
        totalPages = in.readInt();
        in.readList(results, List.class.getClassLoader());
    }

    public int getPage() {
        return page;
    }

    public List<T> getResults() {
        return results;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public int getTotalPages() {
        return totalPages;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(page);
        parcel.writeInt(totalResults);
        parcel.writeInt(totalPages);
        parcel.writeList(results);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public final Parcelable.Creator<Response> CREATOR = new Creator<Response>() {
        @Override
        public Response createFromParcel(Parcel source) {
            return new Response(source);
        }

        @Override
        public Response[] newArray(int size) {
            return new Response[size];
        }
    };
}
