package com.example.ashut.popularmoviespart1;

import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v4.app.LoaderManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.ashut.popularmoviespart1.Adapters.MovieAdapter;
import com.example.ashut.popularmoviespart1.Jsons.MoviesJson;
import com.example.ashut.popularmoviespart1.SingleItems.MovieItem;

import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity  {


    private static TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;
    private static TextView mHeadingDisplay;
    static int a=0;
    private static GridView mGridView;
    private MovieAdapter mMovieAdapter;
    private static ArrayList<MovieItem> mGridData;
    boolean InternetStatus;
    String movieId="";String movieTitle="";String posterPath="";
    MenuItem sort;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mGridView=(GridView) findViewById(R.id.gridView);
        mLoadingIndicator=(ProgressBar)findViewById(R.id.pb_loader);
        mErrorMessageDisplay=(TextView) findViewById(R.id.tv_error_message);
        mHeadingDisplay=(TextView)findViewById(R.id.tv_heading) ;
/*        if(a==1){
            makeMovieQuery(R.id.popular_menu);
        }
        else if(a==2)
            makeMovieQuery(R.id.top_rated_menu);
        else
            makeMovieQuery();*/
        makeMovieQuery();


        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                MovieItem item = (MovieItem) parent.getItemAtPosition(position);
                movieId=item.getMovieId();
                movieTitle=item.getTitle();
                posterPath=item.getPosterPath();
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra("MOVIE_ID",movieId);
                intent.putExtra("MOVIE_TITLE",movieTitle);
                intent.putExtra("POSTER_PATH",posterPath);
                startActivity(intent);


            }
        });
    }



    private void makeMovieQuery() {
        mHeadingDisplay.setText("Now Playing");
        mGridData = new ArrayList<>();
        mMovieAdapter = new MovieAdapter(this, R.layout.single_movie_item, mGridData);
        mGridView.setAdapter(mMovieAdapter);
        URL movieSearchUrl = NetworkUtility.buildUrl();
        new MovieQueryTask().execute(movieSearchUrl);
    }
    private void makeMovieQuery(int menuItemSelected) {
        if(menuItemSelected==R.id.top_rated_menu)
            mHeadingDisplay.setText(R.string.Top_rated);
        else if(menuItemSelected==R.id.popular_menu)
            mHeadingDisplay.setText(R.string.most_popular);
        else
            mHeadingDisplay.setText(R.string.now_playing);

        mGridData = new ArrayList<>();
        mMovieAdapter = new MovieAdapter(this, R.layout.single_movie_item, mGridData);
        mGridView.setAdapter(mMovieAdapter);
        URL movieSearchUrl = NetworkUtility.buildUrl(menuItemSelected);
        new MovieQueryTask().execute(movieSearchUrl);
    }

    public static void showErrorMessage() {
        mGridView.setVisibility(View.INVISIBLE);
        mHeadingDisplay.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    public void showJsonData() {
        mHeadingDisplay.setVisibility(View.VISIBLE);
        mGridView.setVisibility(View.VISIBLE);
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
    }
    public  boolean checkInternet(){
        boolean internetStatus=true;
        ConnectivityManager connec=(ConnectivityManager)getSystemService(getBaseContext().CONNECTIVITY_SERVICE);
        if(connec.getNetworkInfo(0).getState()== android.net.NetworkInfo.State.CONNECTED||connec.getNetworkInfo(1).getState()==android.net.NetworkInfo.State.CONNECTED
                ||connec.getNetworkInfo(0).getState()== NetworkInfo.State.CONNECTING||connec.getNetworkInfo(1).getState()== NetworkInfo.State.CONNECTING)
            internetStatus=true;
        else if(connec.getNetworkInfo(0).getState()== NetworkInfo.State.DISCONNECTED||connec.getNetworkInfo(1).getState()== NetworkInfo.State.DISCONNECTED)
            internetStatus=false;

        return internetStatus;
    }




    private class MovieQueryTask extends AsyncTask<URL, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(URL... params) {
            URL searchUrl = params[0];
            String movieSearchResults = null;
            try {
                InternetStatus=checkInternet();
                if(InternetStatus==true)
                movieSearchResults = NetworkUtility.getResponseFromHttpUrl(searchUrl);
                else movieSearchResults=null;
            } catch (Exception e) {
                e.printStackTrace();
                movieSearchResults=null;
            }
            MoviesJson.getJsonData(movieSearchResults);
            return movieSearchResults;
        }

        @Override
        protected void onPostExecute(String movieSearchResults) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (movieSearchResults != null && !movieSearchResults.equals("")) {
                showJsonData();
                mMovieAdapter.setGridData(mGridData);
                int abc=mGridData.size();

            } else {
                showErrorMessage();
            }
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
     //   sort=item;

        int itemThatWasClickedId = item.getItemId();
        //int leavingItem;
       /* if(a==0)
            itemThatWasClickedId=R.id.now_playing_menu;

*/
        if (itemThatWasClickedId == R.id.now_playing_menu)
        {
            a=0;
            item.setChecked(true);
            mHeadingDisplay.setText(R.string.now_playing);
            makeMovieQuery(R.id.now_playing_menu);
            return true;
        }
        if (itemThatWasClickedId == R.id.popular_menu)
        {
            a=1;
            item.setChecked(true);
            mHeadingDisplay.setText(R.string.most_popular);
            makeMovieQuery(R.id.popular_menu);
            return true;
        }  if (itemThatWasClickedId == R.id.top_rated_menu) {
            a=2;
            item.setChecked(true);
            mHeadingDisplay.setText(R.string.Top_rated);
            makeMovieQuery(R.id.top_rated_menu);
            return true;
        }
        if(itemThatWasClickedId==R.id.favourites_menu){
            //a=0;
           // item.setChecked(true);
            Intent i=new Intent(MainActivity.this,FavouritesActivity.class);
            startActivity(i);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

   /* @Override
    public void onBackPressed() {
        if(isFilterOn==true){
            makeMovieQuery();
        }else {
            super.onBackPressed();
        }}*/

    @Override
    protected void onResume() {
        super.onResume();
        if(a==1){
          //  sort.setChecked(true);
            makeMovieQuery(R.id.popular_menu);
        }
       else if(a==2)
            makeMovieQuery(R.id.top_rated_menu);
        else
            makeMovieQuery();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("sort", a);
        /*outState.putIntArray("ARTICLE_SCROLL_POSITION",
                new int[]{ mGridView.getScrollX(), mGridView.getScrollY()});*/
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        a = savedInstanceState.getInt("sort");
        if(a==1){
           // mHeadingDisplay.setText(R.string.most_popular);
            makeMovieQuery(R.id.popular_menu);
        }
        else if(a==2){
            makeMovieQuery(R.id.top_rated_menu);
           // mHeadingDisplay.setText(R.string.Top_rated);
        }
        else{
            makeMovieQuery();
          //  mHeadingDisplay.setText(R.string.now_playing);
        }/*

        final int[] position = savedInstanceState.getIntArray("ARTICLE_SCROLL_POSITION");
        if(position != null)
            mGridView.post(new Runnable() {
                public void run() {
                    mGridView.scrollTo(position[0], position[1]);
                }
            });
*/
    }
}