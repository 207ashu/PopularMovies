package com.example.ashut.popularmoviespart1;

import android.content.ContentValues;
import android.content.Context;
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
import com.example.ashut.popularmoviespart1.Database.MoviesContract;
import com.example.ashut.popularmoviespart1.Jsons.DeatilJson;
import com.example.ashut.popularmoviespart1.Jsons.MoviesJson;
import com.example.ashut.popularmoviespart1.Jsons.ReviewsJson;
import com.example.ashut.popularmoviespart1.Jsons.TrailersJson;
import com.example.ashut.popularmoviespart1.SingleItems.ReviewsItem;
import com.example.ashut.popularmoviespart1.SingleItems.TrailersItem;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;

public class DetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{


    private static final String TAG = DetailActivity.class.getSimpleName();

    public static RecyclerView.LayoutManager mLayoutManager, mReviewsLayoutManager;
    private  TextView dTitle, dReleaseDate, dAverageVote, dOverview, dTrailers, dReviews;
    private static TextView dErrorMessageDisplay;
    public Toast mToast;
    private static ScrollView dScrollView;
    private static RecyclerView mRecyclerView, mReviewsRecyclerView;
    private static ArrayList<TrailersItem> mTrailersData;
    private static ArrayList<ReviewsItem> mReviewsData;
    HashMap<String, String> mdata;
   public static String movieId,movieTitle;
    boolean Internet;
    private ImageView dBackground, dPoster;
    private ProgressBar dLoadingIndicator;
    private TrailersAdapter mTrailersAdapter;
    private ReviewsAdapter mReviewsAdapter;
    private  static final int LoaderId=0;
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






        toggle = (ToggleButton) findViewById(R.id.toggleButton);
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    ContentValues cv = new ContentValues();
                    cv.put(MoviesContract.MoviesEntry.MOVIE_ID,movieId);
                    cv.put(MoviesContract.MoviesEntry.MOVIE_NAME,movieTitle);


                    if(mToast!=null)
                        mToast.cancel();
                    mToast=Toast.makeText(DetailActivity.this,"Marked as Favourite",Toast.LENGTH_LONG);
                    mToast.show();

                } else {




                    /*
                    if(mToast!=null)
                        mToast.cancel();
                    mToast=Toast.makeText(DetailActivity.this,"Removed from Favourite",Toast.LENGTH_LONG);
                    mToast.show();
                    */
                }
            }
        });


        getSupportLoaderManager().initLoader(LoaderId,null,this);

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



    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<Cursor>(this) {
            @Override
            public Cursor loadInBackground() {
                try {
                    String mSelection=" movieId = ?";
                    String[] mSelectionArgs=new String[]{movieId};
                    return getContentResolver().query(MoviesContract.MoviesEntry.BASE_CONTENT_URI,
                            null,
                            mSelection,
                            mSelectionArgs,
                            null);

                } catch (Exception e) {
                    Log.e(TAG, "Failed to asynchronously load data.");
                    e.printStackTrace();
                    return null;
                }
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if(data.getCount()==0){
dTitle.setText(data.getCount());
        }
        else {dTitle.setText(data.getCount());
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

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
