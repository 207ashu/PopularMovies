package com.example.ashut.popularmoviespart1.Database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.ashut.popularmoviespart1.DetailActivity;

/**
 * Created by ICRIER on 23-07-2017.
 */

public class MoviesContentProvider extends ContentProvider {

    private MoviesDBHelper moviesDBHelper;
    public static final int MOVIES=100;
    public static final int MOVIES_WITH_ID=101;
    public static final UriMatcher sUriMatcher=buildUriMatcher();

    public static UriMatcher buildUriMatcher(){

        UriMatcher uriMatcher=new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(MoviesContract.AUTHORITY,MoviesContract.PATH_MOVIES,MOVIES);
        uriMatcher.addURI(MoviesContract.AUTHORITY,MoviesContract.PATH_MOVIES+"/#",MOVIES_WITH_ID);
        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        Context c=getContext();
        moviesDBHelper=new MoviesDBHelper(c);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String mSelection, @Nullable String[] mSelectionArgs, @Nullable String s1) {
        final SQLiteDatabase db=moviesDBHelper.getWritableDatabase();
        int match=sUriMatcher.match(uri);
        Cursor returnCursor;
        switch (match) {
            case MOVIES_WITH_ID:
                mSelection="movieId = ?";
                mSelectionArgs=new String[]{DetailActivity.movieId};
                returnCursor=db.query(MoviesContract.MoviesEntry.TABLE_NAME,null,mSelection,mSelectionArgs,null,null,null
                        );
                break;
            default:
                throw  new UnsupportedOperationException("Unkown Uri"+uri);


        }
            return returnCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final SQLiteDatabase db=moviesDBHelper.getWritableDatabase();
        int match=sUriMatcher.match(uri);
        Uri returnUri;
        switch (match){

            case MOVIES:
                long id =db.insert(MoviesContract.MoviesEntry.TABLE_NAME,null,contentValues);
                if(id>0){
                    returnUri= ContentUris.withAppendedId(MoviesContract.MoviesEntry.BASE_CONTENT_URI,id);
                }
                else
                    throw new SQLiteException("Failed to Insert"+uri);
                break;
                default:
                    throw  new UnsupportedOperationException("Unkown Uri"+uri);
        }

        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
