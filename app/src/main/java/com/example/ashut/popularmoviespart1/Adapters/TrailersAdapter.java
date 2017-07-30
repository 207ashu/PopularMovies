package com.example.ashut.popularmoviespart1.Adapters;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.ashut.popularmoviespart1.R;
import com.example.ashut.popularmoviespart1.SingleItems.TrailersItem;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by ICRIER on 20-07-2017.
 */

public class TrailersAdapter extends RecyclerView.Adapter<TrailersAdapter.ViewHolder>{
   public static List<TrailersItem> TrailersData;
    Context mContext;

    public TrailersAdapter(List<TrailersItem> trailersData, Context mContext) {
        TrailersData = trailersData;
        this.mContext = mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v=LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_trailer,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final TrailersItem Data=TrailersData.get(position);
        Picasso.with(mContext).load(Data.getImageURL()).into(holder.image);


        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String id=Data.getKey();
                Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
                Intent webIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://www.youtube.com/watch?v=" + id));
                try {
                    mContext. startActivity(appIntent);
                } catch (ActivityNotFoundException ex) {
                    mContext.startActivity(webIntent);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return TrailersData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView image;
        public LinearLayout layout;

        public ViewHolder(View itemView) {
            super(itemView);
            image=(ImageView)itemView.findViewById(R.id.trailer_image);
            layout=(LinearLayout)itemView.findViewById(R.id.each_trailer);
        }
    }
}
