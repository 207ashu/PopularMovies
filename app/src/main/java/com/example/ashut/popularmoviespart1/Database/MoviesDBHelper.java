package com.example.ashut.popularmoviespart1.Database;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ICRIER on 22-07-2017.
 */

public class MoviesDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME="movies.db";
    private static final int DATABASE_VERSION=1;

    public MoviesDBHelper(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

         final String CREATE_TABLE_MOVIES="CREATE TABLE "+
                MoviesContract.MoviesEntry.TABLE_NAME+"( "+
                MoviesContract.MoviesEntry.MOVIE_ID+" INTEGER PRIMARY KEY, "+
                 MoviesContract.MoviesEntry.MOVIE_NAME+" STRING NOT NULL );";

        sqLiteDatabase.execSQL(CREATE_TABLE_MOVIES);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ MoviesContract.MoviesEntry.TABLE_NAME);

    }
}
