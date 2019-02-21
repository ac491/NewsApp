package com.example.newsapp.Activity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.newsapp.API.Model.NewsArticle;
import com.example.newsapp.Adapter.NewsAdapter;
import com.example.newsapp.Database.LocalDatabase.LocalDatabaseHelper;
import com.example.newsapp.R;

import java.util.ArrayList;
import java.util.List;

public class OfflineNews extends AppCompatActivity {

    private LocalDatabaseHelper mDb;
    private RecyclerView recyclerView;
    private ProgressBar mProgress;
    private SwipeRefreshLayout mSwipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline_news);

        recyclerView = findViewById(R.id.recycler_viewOffline);
        mProgress = findViewById(R.id.progressOffline);
        recyclerView.setVisibility(View.INVISIBLE);
        mSwipe = findViewById(R.id.refreshOffline);

        mDb = new LocalDatabaseHelper(this);
        getLocalRecords();

        mSwipe.setOnRefreshListener(() -> {
            mProgress.setVisibility(View.VISIBLE);
                new Thread(this::getLocalRecords).start();
                mSwipe.setRefreshing(false);
        });

    }


    public void getLocalRecords(){

        List<NewsArticle> articles = new ArrayList<>();
        articles = mDb.getAllArticles();
        Log.d("TAG", String.valueOf(articles.size()));

        if(articles.size() == 0){
            recyclerView.setVisibility(View.GONE);
            mProgress.setVisibility(View.GONE);
            ConstraintLayout constraintLayout = findViewById(R.id.constraint);
            Snackbar snackbar = Snackbar
                    .make(constraintLayout, "No articles added to offline. Long press on a news card to add it to offline.", Snackbar.LENGTH_LONG);
            snackbar.show();
        } else {
            final NewsAdapter adapter = new NewsAdapter(this, articles, true);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(OfflineNews.this);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.post(() -> {
                mProgress.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                recyclerView.setAdapter(adapter);
            });
        }
    }
}
