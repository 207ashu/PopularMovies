package com.example.ashut.popularmoviespart1.Jsons;

import com.example.ashut.popularmoviespart1.Adapters.ReviewsAdapter;
import com.example.ashut.popularmoviespart1.Adapters.TrailersAdapter;
import com.example.ashut.popularmoviespart1.DetailActivity;
import com.example.ashut.popularmoviespart1.SingleItems.ReviewsItem;
import com.example.ashut.popularmoviespart1.SingleItems.TrailersItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static android.R.attr.key;

/**
 * Created by ICRIER on 08-07-2017.
 */

public class DeatilJson {
   static HashMap<String,String>data=new HashMap<>();


    public static HashMap<String,String> getDetailJsonData(String JsonFile){


        try {
            if(JsonFile!=null) {

                JSONObject root = new JSONObject(JsonFile);

                String movieId = root.getString("id");
                String title = root.getString("title");
                String releaseDate = root.getString("release_date");
                String posterPath = root.getString("poster_path");
                String backdropPath = root.getString("backdrop_path");
                String voteAverage = root.getString("vote_average");
                String overview = root.getString("overview");

                data.put("movieId", movieId);
                data.put("title", title);
                data.put("releaseDate", releaseDate);
                data.put("posterPath", posterPath);
                data.put("backdropPath", backdropPath);
                data.put("voteAverage", voteAverage);
                data.put("overView", overview);


            }
            else
                DetailActivity.showErrorMessage();

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return data;}
}

