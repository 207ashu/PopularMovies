package com.example.ashut.popularmoviespart1.Adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ashut.popularmoviespart1.SingleItems.MovieItem;
import com.example.ashut.popularmoviespart1.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by ICRIER on 07-07-2017.
 */

public class MovieAdapter extends ArrayAdapter {

    private Context mContext;
    private int layoutResourceId;
   public static ArrayList<MovieItem> mGridData = new ArrayList<MovieItem>();


    public MovieAdapter(@NonNull Context mContext, @LayoutRes int layoutResourceId, @NonNull ArrayList<MovieItem> mGridData) {
        super(mContext, layoutResourceId, mGridData);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.mGridData = mGridData;
    }


    public void setGridData(ArrayList<MovieItem> mGridData) {
        this.mGridData = mGridData;
        notifyDataSetChanged();
    }
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(R.layout.single_movie_item, parent, false);

        }

        MovieItem item = mGridData.get(position);

        TextView titleTextView = (TextView) convertView.findViewById(R.id.movie_name);
        titleTextView.setText(item.getTitle());

        ImageView imageView = (ImageView) convertView.findViewById(R.id.movie_image);
        Picasso.with(mContext).load("https://image.tmdb.org/t/p/w500"+item.getPosterPath()).into(imageView);


        return convertView;
    }

}
