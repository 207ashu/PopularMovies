package com.example.ashut.popularmoviespart1.Database;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by ICRIER on 22-07-2017.
 */

public class MoviesContract {

    private  MoviesContract(){}

    public static final String AUTHORITY="com.example.ashut.popularmoviespart1";

    public static final Uri BASE_URI = Uri.parse("content://"+AUTHORITY);

    public static final String PATH_MOVIES="movies";

    public static class MoviesEntry implements BaseColumns{

        public static final Uri BASE_CONTENT_URI=BASE_URI.buildUpon().appendPath(PATH_MOVIES).build();

        public static final String TABLE_NAME="movies";
        public static final String MOVIE_ID="movieId";
        public static final String MOVIE_NAME="movieName";

    }
}
