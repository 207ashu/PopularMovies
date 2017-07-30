package com.example.ashut.popularmoviespart1.SingleItems;

/**
 * Created by ICRIER on 20-07-2017.
 */

public class TrailersItem {
    private String key;
    private String imageURL;

    public TrailersItem(String key,String imageURL) {
        this.key = key;
        this.imageURL=imageURL;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public  String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
