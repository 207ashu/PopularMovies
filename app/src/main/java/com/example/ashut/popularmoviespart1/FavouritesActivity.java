package com.example.ashut.popularmoviespart1;

import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.example.ashut.popularmoviespart1.Adapters.MovieAdapter;
import com.example.ashut.popularmoviespart1.Database.MoviesContentProvider;
import com.example.ashut.popularmoviespart1.SingleItems.MovieItem;

import java.util.ArrayList;

public class FavouritesActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static GridView mGridView;
    private MovieAdapter mMovieAdapter;
    private static ArrayList<MovieItem> mGridData;
    private static TextView tvFavouritesError,tvFavourites;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);


        mGridView=(GridView)findViewById(R.id.favouritesGridView);
        tvFavourites=(TextView)findViewById(R.id.tv_favourites);
        tvFavouritesError=(TextView)findViewById(R.id.tv_favourites_error);




        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                MovieItem item = (MovieItem) parent.getItemAtPosition(position);
                String movieId=item.getMovieId();
                String movieTitle=item.getTitle();
                String posterPath=item.getPosterPath();
                Intent intent = new Intent(FavouritesActivity.this, DetailActivity.class);
                intent.putExtra("MOVIE_ID",movieId);
                intent.putExtra("MOVIE_TITLE",movieTitle);
                intent.putExtra("POSTER_PATH",posterPath);
                startActivity(intent);


            }
        });

        getSupportLoaderManager().initLoader(0,null,this);


    }





    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        return new CursorLoader(this,MoviesContentProvider.CONTENT_URI,null,null,null,MoviesContentProvider.movieName);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if(data.getCount()==0){
            tvFavourites.setVisibility(View.GONE);
            tvFavouritesError.setVisibility(View.VISIBLE);
        }
        if (data.moveToFirst()) {
            do {
                String title=data.getString(data.getColumnIndex(MoviesContentProvider.movieName));
                String movieId=data.getString(data.getColumnIndex(MoviesContentProvider.movieId));
                String posterPath=data.getString(data.getColumnIndex(MoviesContentProvider.posterPath));

                MovieItem item = new MovieItem(title, posterPath,movieId);

                item.setTitle(title);
                item.setPosterPath(posterPath);
                item.setMovieId(movieId);
                MovieAdapter.mGridData.add(item);

            } while (data.moveToNext());

        }

        mMovieAdapter.setGridData(mGridData);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        mGridData = new ArrayList<>();
        mMovieAdapter = new MovieAdapter(this, R.layout.single_movie_item, mGridData);
        mGridView.setAdapter(mMovieAdapter);
        getSupportLoaderManager().restartLoader(0,null,this);

    }
}
