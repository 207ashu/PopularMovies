package com.example.ashut.popularmoviespart1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class DetailedReview extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_review);
        TextView author=(TextView)findViewById(R.id.tv_detail_author);
        TextView content=(TextView)findViewById(R.id.tv_detail_content);

        author.setText(getIntent().getStringExtra("author"));
        content.setText(getIntent().getStringExtra("content"));
    }
}
