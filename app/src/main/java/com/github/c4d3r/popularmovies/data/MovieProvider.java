package com.github.c4d3r.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by Maxim on 11/02/2017.
 */

public class MovieProvider extends ContentProvider {

    // constants to match URI with data
    public static final int CODE_FAVOURITE = 100;
    public static final int CODE_FAVOURITE_WITH_ID = 101;

    private MovieDbHelper _dbHelper;

    private static final UriMatcher sUriMatcher = buildUriMatcher(); // s in front means static

    // urimatcher does the matching for you, no regex needed
    public static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MovieContract.CONTENT_AUTHORITY;

        // content://com.github.c4d3r.popularmovies/movie/favourite/*/
        matcher.addURI(authority, MovieContract.PATH_FAVOURITE, CODE_FAVOURITE);

        // content://com.github.c4d3r.popularmovies/movie_favourite/12/
        matcher.addURI(authority, MovieContract.PATH_FAVOURITE + "/#", CODE_FAVOURITE_WITH_ID);

        return matcher;
    }

    /**
     * Initialize content provider, run on main thread, keep it short!
     * @return
     */
    @Override
    public boolean onCreate() {
        _dbHelper = new MovieDbHelper(getContext());
        return true;
    }


    /**
     * Handles query requests
     * @param uri
     * @param projection
     * @param selection
     * @param selectionArgs
     * @param sortOrder
     * @return
     */
    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                            String[] selectionArgs, String sortOrder) {
        Cursor cursor;

        switch(sUriMatcher.match(uri)) {
            case CODE_FAVOURITE_WITH_ID: {
                // get id
                String id = uri.getLastPathSegment();

                // may be more than ? in the selection statement
                String[] selectionArguments = new String[]{id};

                cursor = _dbHelper.getReadableDatabase().query(
                        MovieContract.FavouriteEntry.TABLE_NAME,
                        projection,
                        MovieContract.FavouriteEntry.COLUMN_MOVIE_ID + " = ? ",
                        selectionArguments,
                        null,
                        null,
                        sortOrder
                );
                break;
            }

            case CODE_FAVOURITE:
                cursor = _dbHelper.getReadableDatabase().query(
                        MovieContract.FavouriteEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            default:
                throw new UnsupportedOperationException("Unknown URI: " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues value) {

        final SQLiteDatabase db = _dbHelper.getWritableDatabase();

        switch(sUriMatcher.match(uri)) {
            case CODE_FAVOURITE:
                db.beginTransaction();
                long _id = -1;
                try {
                    _id = db.insert(MovieContract.FavouriteEntry.TABLE_NAME, null, value);
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }

                if(_id != -1) getContext().getContentResolver().notifyChange(uri, null);
        }

        return uri;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {

        int numRowsDeleted;

        /**
         * if we pass null, entire table is deleted,
         * passing 1 will delete all rows and return the number of deleted rows
         */
        if(null == selection) selection = "1";

        switch(sUriMatcher.match(uri)) {
            case CODE_FAVOURITE_WITH_ID:
                numRowsDeleted = _dbHelper.getWritableDatabase().delete(
                        MovieContract.FavouriteEntry.TABLE_NAME,
                        selection,
                        selectionArgs
                );
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // notify that change has occurred to this URI
        if(numRowsDeleted != 0) getContext().getContentResolver().notifyChange(uri, null);

        return numRowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }
}
