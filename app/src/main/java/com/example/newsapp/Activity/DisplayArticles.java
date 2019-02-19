package com.example.newsapp.Activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.newsapp.API.Model.NewsArticle;
import com.example.newsapp.R;

public class DisplayArticles extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_articles);
        Bundle extras = getIntent().getExtras();
        NewsArticle newsArticle = null;
        if(extras != null){
            newsArticle = (NewsArticle)extras.getSerializable("newsArticle");
        }

        TextView title = findViewById(R.id.displaytitle);
        TextView content = findViewById(R.id.displaycontent);
        ImageView imageView = findViewById(R.id.displayimage);
        Glide.with(this).load(newsArticle.getUrlToImge()).into(imageView);
        title.setText(newsArticle.getTitle());
        content.setText(newsArticle.getContent());


    }

}
