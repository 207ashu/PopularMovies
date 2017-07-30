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

/**
 * Created by ICRIER on 22-07-2017.
 */

public class TrailersJson {
    static String trailersArrayLength="";


    public static String getTrailersData(String JsonFile){

        try {
            if(JsonFile!=null) {

                JSONObject root = new JSONObject(JsonFile);

                JSONArray resultsArray = root.optJSONArray("results");
                TrailersItem trailersItem;

                 trailersArrayLength=""+resultsArray.length();



                //Retrieve data for Trailers
                for (int i = 0; i < resultsArray.length(); i++) {
                    JSONObject eachItem = resultsArray.optJSONObject(i);
                    String type = eachItem.getString("type");
                    String key = eachItem.getString("key");
                    String url="http://img.youtube.com/vi/"+key +"/hqdefault.jpg";

                    if(type.equals("Trailer")){
                        trailersItem = new TrailersItem(key,url);

                        trailersItem.setKey(key);
                        trailersItem.setImageURL(url);
                        TrailersAdapter.TrailersData.add(trailersItem);}
                }
            }
            else
                DetailActivity.showErrorMessage();

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return trailersArrayLength;}
}
