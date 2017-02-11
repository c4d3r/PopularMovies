package com.github.c4d3r.popularmovies.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Maxim on 11/02/2017.
 */

public class MovieContract {

    // name for the entire content provider, normally package name
    public static final String CONTENT_AUTHORITY = "com.github.c4d3r.popularmovies";

    // base path for the uri
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    /*
     * Possible paths that can be appended to BASE_CONTENT_URI to form valid URI's that Sunshine
     * can handle. For instance,
     *
     *     content://com.github.c4d3r.popularmovies/movie_favourite/
     *     [           BASE_CONTENT_URI         ][ PATH_FAVOURITE ]
     *
     */
    public static final String PATH_FAVOURITE = "movie_favourite";

    public static final class FavouriteEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_FAVOURITE)
                .build(); // builder pattern

        public static final String TABLE_NAME = "favourite_movie";

        public static final String COLUMN_MOVIE_ID = "movie_id";

        public static final String COLUMN_MOVIE_TITLE = "movie_title";

    }

}
