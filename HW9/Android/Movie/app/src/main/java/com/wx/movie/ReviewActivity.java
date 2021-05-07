package com.wx.movie;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class ReviewActivity extends AppCompatActivity {
    private TextView tv_title;
    private TextView tv_rating;
    private TextView tv_content;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        initView();
        Intent intent = getIntent();
        String title =  intent.getStringExtra("title");
        String rating = intent.getStringExtra("rating");
        String content = intent.getStringExtra("content");

        tv_title.setText(title);
        tv_rating.setText(rating);
        tv_content.setText(content);
    }

    private void initView() {
        tv_title = findViewById(R.id.tv_review_title);
        tv_rating = findViewById(R.id.tv_review_rating);
        tv_content = findViewById(R.id.tv_review_content);
    }
}