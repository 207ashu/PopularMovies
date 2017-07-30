package com.example.ashut.popularmoviespart1.Jsons;

import com.example.ashut.popularmoviespart1.Adapters.MovieAdapter;
import com.example.ashut.popularmoviespart1.SingleItems.MovieItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MoviesJson {

    public static void getJsonData(String JsonFile){


        try {
            if(JsonFile!=null) {

            MovieItem item;
            JSONObject root = new JSONObject(JsonFile);
            JSONArray resultsArray = root.optJSONArray("results");
            for (int i = 0; i < resultsArray.length(); i++) {
                JSONObject eachItem = resultsArray.optJSONObject(i);
                String title = eachItem.getString("title");
                String posterPath = eachItem.getString("poster_path");
                String movieId=eachItem.getString("id");

                item = new MovieItem(title, posterPath,movieId);
                item.setTitle(title);
                item.setPosterPath(posterPath);
                item.setMovieId(movieId);
                MovieAdapter.mGridData.add(item);
            }
        }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
