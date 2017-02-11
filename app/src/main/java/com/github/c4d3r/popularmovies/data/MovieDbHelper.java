package com.github.c4d3r.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.github.c4d3r.popularmovies.data.MovieContract.*;

/**
 * Created by Maxim on 11/02/2017.
 */

public class MovieDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "movies.db";

    private static final int DATABASE_VERSION = 1;

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // create tables

        final String SQL_CREATE_MOVIE_FAVOURITE_TABLE =

            "CREATE TABLE " + FavouriteEntry.TABLE_NAME + " (" +

            FavouriteEntry._ID                  + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            FavouriteEntry.COLUMN_MOVIE_ID      + " INTEGER, " +
            FavouriteEntry.COLUMN_MOVIE_TITLE   + " TEXT " + ");";


        sqLiteDatabase.execSQL(SQL_CREATE_MOVIE_FAVOURITE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //for later upgrades
    }
}
