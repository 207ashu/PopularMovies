package com.example.ashut.popularmoviespart1.Adapters;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ashut.popularmoviespart1.DetailedReview;
import com.example.ashut.popularmoviespart1.R;
import com.example.ashut.popularmoviespart1.SingleItems.ReviewsItem;

import java.util.List;

/**
 * Created by ICRIER on 21-07-2017.
 */

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ViewHolder> {
    public static List<ReviewsItem> ReviewsData;
    Context mContext;

    public ReviewsAdapter(List<ReviewsItem> reviewsData, Context mContext) {
        this.ReviewsData = reviewsData;
        this.mContext = mContext;
    }

    @Override
    public ReviewsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_review,parent,false);
        return new ReviewsAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ReviewsAdapter.ViewHolder holder, int position) {
        final ReviewsItem Data=ReviewsData.get(position);
        holder.content.setText(Data.getContent());
        holder.author.setText(Data.getAuthor());

        holder.readMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(mContext, DetailedReview.class);
                intent.putExtra("author",Data.getAuthor());
                intent.putExtra("content",Data.getContent());
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return ReviewsData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView author,content,readMore;


        public ViewHolder(View itemView) {
            super(itemView);
            author=(TextView)itemView.findViewById(R.id.tv_reviewAuthor);
            content=(TextView)itemView.findViewById(R.id.tv_reviewContent);
            readMore=(TextView)itemView.findViewById(R.id.tv_readMore);

        }
    }
}
