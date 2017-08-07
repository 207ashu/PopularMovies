package com.example.ashut.popularmoviespart1;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.ashut.popularmoviespart1.Adapters.ReviewsAdapter;
import com.example.ashut.popularmoviespart1.Adapters.TrailersAdapter;
import com.example.ashut.popularmoviespart1.Database.MoviesContentProvider;
import com.example.ashut.popularmoviespart1.Jsons.DeatilJson;
import com.example.ashut.popularmoviespart1.Jsons.ReviewsJson;
import com.example.ashut.popularmoviespart1.Jsons.TrailersJson;
import com.example.ashut.popularmoviespart1.SingleItems.ReviewsItem;
import com.example.ashut.popularmoviespart1.SingleItems.TrailersItem;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;

public class DetailActivity extends AppCompatActivity{

    public static RecyclerView.LayoutManager mLayoutManager, mReviewsLayoutManager;
    private  TextView dTitle, dReleaseDate, dAverageVote, dOverview, dTrailers, dReviews;
    private static TextView dErrorMessageDisplay;
    public Toast mToast;
    private static ScrollView dScrollView;
    private static RecyclerView mRecyclerView, mReviewsRecyclerView;
    private static ArrayList<TrailersItem> mTrailersData;
    private static ArrayList<ReviewsItem> mReviewsData;
    HashMap<String, String> mdata;
    public static String movieId,movieTitle,posterPath;
    boolean Internet;
    private ImageView dBackground, dPoster;
    private ProgressBar dLoadingIndicator;
    private TrailersAdapter mTrailersAdapter;
    private ReviewsAdapter mReviewsAdapter;
    private static ToggleButton toggle;

    public static void showErrorMessage() {
        dScrollView.setVisibility(View.INVISIBLE);
        dErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        dLoadingIndicator = (ProgressBar) findViewById(R.id.detail_loader);
        dErrorMessageDisplay = (TextView) findViewById(R.id.tv_detail_error_message);
        dScrollView = (ScrollView) findViewById(R.id.scrollView);

        dTitle = (TextView) findViewById(R.id.tv_title);
        dReleaseDate = (TextView) findViewById(R.id.tv_release_date);
        dAverageVote = (TextView) findViewById(R.id.tv_vote_avg);
        dOverview = (TextView) findViewById(R.id.tv_overview);
        dBackground = (ImageView) findViewById(R.id.background_poster);
        dPoster = (ImageView) findViewById(R.id.poster);
        dTrailers = (TextView) findViewById(R.id.tv_trailers);
        dReviews = (TextView) findViewById(R.id.tv_reviews);

        movieId = "" + getIntent().getStringExtra("MOVIE_ID");
        movieTitle=""+getIntent().getStringExtra("MOVIE_TITLE");
        posterPath=""+getIntent().getStringExtra("POSTER_PATH");

        final URL DetailSearchUrl = NetworkUtility.buildDetailUrl(movieId);
        new DetailQueryTask().execute(DetailSearchUrl);


        URL VideosSearchUrl = NetworkUtility.buildDetailUrl(movieId, "videos");
        new VideosQueryTask().execute(VideosSearchUrl);
        mTrailersData = new ArrayList<>();
        mRecyclerView = (RecyclerView) findViewById(R.id.TrailerView);
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mTrailersAdapter = new TrailersAdapter(mTrailersData, DetailActivity.this);
        mRecyclerView.setAdapter(mTrailersAdapter);


        URL ReviewsSearchUrl = NetworkUtility.buildDetailUrl(movieId, "reviews");
        new ReviewsQueryTask().execute(ReviewsSearchUrl);
        mReviewsData = new ArrayList<>();
        mReviewsRecyclerView = (RecyclerView) findViewById(R.id.ReviewsView);
        mReviewsLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mReviewsRecyclerView.setLayoutManager(mReviewsLayoutManager);
        mReviewsAdapter = new ReviewsAdapter(mReviewsData, DetailActivity.this);
        mReviewsRecyclerView.setAdapter(mReviewsAdapter);

        new FavouriteQueryTask().execute();


        toggle = (ToggleButton) findViewById(R.id.toggleButton);
        toggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (toggle.isChecked()) {

                    ContentValues cv = new ContentValues();
                    cv.put(MoviesContentProvider.movieId,movieId);
                    cv.put(MoviesContentProvider.movieName,movieTitle);
                    cv.put(MoviesContentProvider.posterPath,posterPath);

                    getContentResolver().insert(
                            MoviesContentProvider.CONTENT_URI, cv);


                    if(mToast!=null)
                        mToast.cancel();
                    mToast=Toast.makeText(DetailActivity.this,"Marked as Favourite",Toast.LENGTH_LONG);
                    mToast.show();

                } else {
                    String[] selectionArgs ={movieId};
                   getContentResolver().delete(MoviesContentProvider.CONTENT_URI,""+MoviesContentProvider.movieId+" = ? ",selectionArgs);



                    if(mToast!=null)
                        mToast.cancel();
                    mToast=Toast.makeText(DetailActivity.this,"Removed from Favourites",Toast.LENGTH_LONG);
                    mToast.show();

                }
            }

        });


    }

    public boolean checkInternet() {
        boolean internetStatus = false;
        ConnectivityManager connec = (ConnectivityManager) getSystemService(getBaseContext().CONNECTIVITY_SERVICE);
        if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED || connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED
                || connec.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTING || connec.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTING)
            internetStatus = true;
        else if (connec.getNetworkInfo(0).getState() == NetworkInfo.State.DISCONNECTED || connec.getNetworkInfo(1).getState() == NetworkInfo.State.DISCONNECTED)
            internetStatus = false;

        return internetStatus;
    }

    public void showDetails(HashMap<String, String> data) {

        dErrorMessageDisplay.setVisibility(View.INVISIBLE);
        dScrollView.setVisibility(View.VISIBLE);

        dTitle.setText(data.get("title"));
        dOverview.append(data.get("overView"));
        dReleaseDate.append(" " + data.get("releaseDate"));
        dAverageVote.append(" " + data.get("voteAverage"));

        Picasso.with(this)
                .load("" + "https://image.tmdb.org/t/p/w500" + data.get("posterPath"))
                .into(dPoster);
        Picasso.with(this)
                .load("" + "https://image.tmdb.org/t/p/w500" + data.get("backdropPath"))
                .into(dBackground);


    }


    private class DetailQueryTask extends AsyncTask<URL, Void, HashMap<String, String>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected HashMap<String, String> doInBackground(URL... params) {
            URL searchUrl = params[0];

            try {
                Internet = checkInternet();
                if (Internet == true) {
                    String DetailSearchResults = NetworkUtility.getResponseFromHttpUrl(searchUrl);
                    mdata = DeatilJson.getDetailJsonData(DetailSearchResults);
                } else
                    mdata = new HashMap<>();
            } catch (UnknownHostException e) {
                mdata = new HashMap<>();
            } catch (Exception e) {
                e.printStackTrace();
                mdata = new HashMap<>();
            }
            return mdata;

        }

        @Override
        protected void onPostExecute(HashMap<String, String> data) {
            dLoadingIndicator.setVisibility(View.INVISIBLE);

            if (data.isEmpty())
                showErrorMessage();
            else {
                showDetails(data);

            }
        }
    }


    private class VideosQueryTask extends AsyncTask<URL, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(URL... params) {
            URL searchUrl = params[0];
            String videosSearchResults = null;
            try {
                boolean InternetStatus = checkInternet();
                if (InternetStatus == true)
                    videosSearchResults = NetworkUtility.getResponseFromHttpUrl(searchUrl);
                else videosSearchResults = null;
            } catch (Exception e) {
                e.printStackTrace();
                videosSearchResults = null;
            }
            videosSearchResults = TrailersJson.getTrailersData(videosSearchResults);
            return videosSearchResults;
        }

        @Override
        protected void onPostExecute(String videosSearchResults) {
            if (videosSearchResults.equals("0")) {
                dTrailers.setText("No Trailers Found");
            } else {
                mTrailersAdapter = new TrailersAdapter(mTrailersData, DetailActivity.this);
                mRecyclerView.setAdapter(mTrailersAdapter);
            }
        }
    }

    private class FavouriteQueryTask extends AsyncTask<Void, Void, Cursor> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Cursor doInBackground(Void... voids) {
            String[] selectionArgs={movieId};
            Cursor c = getContentResolver().query(MoviesContentProvider.CONTENT_URI,null,MoviesContentProvider.movieId+" = ? ",selectionArgs,null);
            return c;
        }

        @Override
        protected void onPostExecute(Cursor c) {
            if(c.getCount()==0)
                toggle.setChecked(false);
            else
                toggle.setChecked(true);
        }
    }

    private class ReviewsQueryTask extends AsyncTask<URL, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(URL... params) {
            URL searchUrl = params[0];
            String reviewsSearchResults = null;
            try {
                boolean InternetStatus = checkInternet();
                if (InternetStatus == true)
                    reviewsSearchResults = NetworkUtility.getResponseFromHttpUrl(searchUrl);
                else reviewsSearchResults = null;
            } catch (Exception e) {
                e.printStackTrace();
                reviewsSearchResults = null;
            }
            reviewsSearchResults = ReviewsJson.getReviewsData(reviewsSearchResults);
            return reviewsSearchResults;
        }

        @Override
        protected void onPostExecute(String reviewsSearchResults) {
            if (reviewsSearchResults.equals("0")) {
                dReviews.setText("No Reviews by Users");

            } else {
                mReviewsAdapter = new ReviewsAdapter(mReviewsData, DetailActivity.this);
                mReviewsRecyclerView.setAdapter(mReviewsAdapter);
            }
        }
    }

}
