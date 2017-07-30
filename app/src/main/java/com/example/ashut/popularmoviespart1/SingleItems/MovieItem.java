package com.example.ashut.popularmoviespart1.SingleItems;

/**
 * Created by ICRIER on 07-07-2017.
 */

public class MovieItem {
    private String title;
    private String posterPath;
    private String movieId;

    public MovieItem(String title, String posterPath,String movieId) {
        this.title = title;
        this.posterPath = posterPath;
        this.movieId=movieId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }
}
