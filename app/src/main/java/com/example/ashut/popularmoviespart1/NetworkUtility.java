package com.example.ashut.popularmoviespart1;

import android.net.ConnectivityManager;
import android.net.Uri;
import android.support.v4.content.Loader;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;


public class NetworkUtility {

    final static String BASE_URL = "https://api.themoviedb.org/3/movie?api_key=81710de5042d48ea00effa78ae855b78&language=en-US&page=1";

    //Creating URL for MainDisplay
    public static URL buildUrl() {
        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendPath("now_playing")
                .build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    //Creating URL for Details
    public static URL buildDetailUrl(String movieId) {

        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(movieId)
                .build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }
    //Creating URL for Details
    public static URL buildDetailUrl(String movieId,String VideosOrReviews) {

        Uri builtUri=Uri.parse(BASE_URL).buildUpon()
                .appendPath(movieId)
                .build();
        if (VideosOrReviews=="videos"){
             builtUri=Uri.parse(BASE_URL).buildUpon()
                    .appendPath(movieId)
                     .appendPath("videos")
                    .build();
        } else if (VideosOrReviews=="reviews"){
            builtUri=Uri.parse(BASE_URL).buildUpon()
                    .appendPath(movieId)
                    .appendPath("reviews")
                    .build();
        }


        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }


        // Creating URL For Sorting via Menu
    public static URL buildUrl(int itemSelected) {
        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .build();
        if (itemSelected == R.id.popular_view) {
            builtUri = Uri.parse(BASE_URL).buildUpon()
                    .appendPath("popular")
                    .build();
        } else if (itemSelected == R.id.top_rated_view) {
            builtUri = Uri.parse(BASE_URL).buildUpon()
                    .appendPath("top_rated")
                    .build();
        }

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }


    public static String getResponseFromHttpUrl(URL url) throws Exception {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();



        String JSONFILE = null;
        try {


            InputStream in = urlConnection.getInputStream();
            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");
            boolean hasInput = scanner.hasNext();
            if(hasInput)
                JSONFILE = scanner.next();
            else
                JSONFILE=null;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return JSONFILE;

    }
}
