package com.example.ashut.popularmoviespart1.Database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.Toast;

import com.example.ashut.popularmoviespart1.DetailActivity;

import java.util.HashMap;

/**
 * Created by ICRIER on 23-07-2017.
 */

public class MoviesContentProvider extends ContentProvider {

    static final String AUTHORITY = "com.example.ashut.popularmoviespart1";
    static final String URL = "content://" + AUTHORITY + "/movies";
    public static final Uri CONTENT_URI = Uri.parse(URL);

    public static String movieId = "movieId";
    public static String movieName = "movieName";
    public static String posterPath ="posterPath";


    static final int MOVIES = 1;
    static final int MOVIES_WITH_ID = 2;

    static final UriMatcher uriMatcher;
    static{
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, "movies", MOVIES);
        uriMatcher.addURI(AUTHORITY, "movies/#", MOVIES_WITH_ID);
    }

    /**
     * Database specific constant declarations
     */

    private SQLiteDatabase db;
    static final String DATABASE_NAME = "movies.db";
    static final String MOVIES_TABLE_NAME = "movies";
    static final int DATABASE_VERSION = 1;
    static final String CREATE_DB_TABLE =
            " CREATE TABLE " + MOVIES_TABLE_NAME +
                    " ( "+ movieId + " INTEGER PRIMARY KEY , "
                    + posterPath +" TEXT NOT NULL , "
                    +  movieName+ " TEXT NOT NULL ); " ;


    /**
     * Helper class that actually creates and manages
     * the provider's underlying data repository.
     */

    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context){
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_DB_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " +  MOVIES_TABLE_NAME);
            onCreate(db);
        }
    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        DatabaseHelper dbHelper = new DatabaseHelper(context);

        /**
         * Create a write able database which will trigger its
         * creation if it doesn't already exist.
         */

        db = dbHelper.getWritableDatabase();
        return (db == null)? false:true;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        /**
         * Add a new student record
         */
        long rowID = db.insert(	MOVIES_TABLE_NAME, null, values);
        Uri _uri=null;
        /**
         * If record is added successfully
         */
        try{
            if (rowID > 0) {
                _uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
                getContext().getContentResolver().notifyChange(_uri, null);}

        }catch (SQLiteConstraintException e){
            Toast.makeText(getContext(),"Existing Record",Toast.LENGTH_LONG).show();
            _uri=null;
        }
        return _uri;
    }

    @Override
    public Cursor query(Uri uri, String[] projection,
                        String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(MOVIES_TABLE_NAME);

        switch (uriMatcher.match(uri)) {
            case MOVIES:
                qb.setProjectionMap(null);
                break;

            case MOVIES_WITH_ID :
                qb.appendWhere( movieId + " = " + uri.getPathSegments().get(1));
                break;

            default:
        }

        Cursor c = qb.query(db,	projection,	selection,
                selectionArgs,null, null, null);
        /**
         * register to watch a content URI for changes
         */
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int count = 0;
        switch (uriMatcher.match(uri)){
            case MOVIES:
                count = db.delete(MOVIES_TABLE_NAME, selection, selectionArgs);
                break;

            case MOVIES_WITH_ID:
                String id = uri.getPathSegments().get(1);
                count = db.delete( MOVIES_TABLE_NAME, movieId +  " = " + id +
                                (!TextUtils.isEmpty(selection) ? "  AND (" + selection + ')' : ""), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values,
                      String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)){
            /**
             * Get all student records
             */
            case MOVIES:
                return "vnd.android.cursor.dir/vnd.example.students";
            /**
             * Get a particular student
             */
            case MOVIES_WITH_ID:
                return "vnd.android.cursor.item/vnd.example.students";
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

}
