package com.example.ashut.popularmoviespart1.Jsons;

import com.example.ashut.popularmoviespart1.Adapters.ReviewsAdapter;
import com.example.ashut.popularmoviespart1.DetailActivity;
import com.example.ashut.popularmoviespart1.SingleItems.ReviewsItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ICRIER on 22-07-2017.
 */

public class ReviewsJson {
    static String reviewsArrayLength="";


    public static String getReviewsData(String JsonFile){

        try {
            if(JsonFile!=null) {

                JSONObject root = new JSONObject(JsonFile);

                JSONArray resultsArray = root.optJSONArray("results");
                ReviewsItem reviewsItem;

                reviewsArrayLength=""+resultsArray.length();



                //Retrieve data for Trailers
                for (int i = 0; i < resultsArray.length(); i++) {
                    JSONObject eachItem = resultsArray.optJSONObject(i);
                    String author = eachItem.getString("author");
                    String content = eachItem.getString("content");

                        reviewsItem = new ReviewsItem(author,content);

                        reviewsItem.setAuthor(author);
                        reviewsItem.setContent(content);
                        ReviewsAdapter.ReviewsData.add(reviewsItem);}

            }
            else
                DetailActivity.showErrorMessage();

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return reviewsArrayLength;}
}
