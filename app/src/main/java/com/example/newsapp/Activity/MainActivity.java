package com.example.newsapp.Activity;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.newsapp.API.Model.NewsArticle;
import com.example.newsapp.API.NewsAPI;
import com.example.newsapp.Adapter.NewsAdapter;
import com.example.newsapp.Database.ArticlesViewModel;
import com.example.newsapp.Database.Model.ArticleEntity;
import com.example.newsapp.R;
import com.example.newsapp.Utils.APIUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private ArticlesViewModel mArticleViewModel;
    private ArrayList<NewsArticle> articles;
    private RecyclerView recyclerView;
    private ProgressBar mProgress;
    private NewsAPI mNewsAPI;
    private String APIKey = "d83e2de86b554a28b0eb55dc23f1fecd";
    private SwipeRefreshLayout mSwipeRefreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mArticleViewModel = ViewModelProviders.of(MainActivity.this).get(ArticlesViewModel.class);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mProgress = findViewById(R.id.progress);
        recyclerView.setVisibility(View.INVISIBLE);
        mNewsAPI = APIUtils.getAPIService();
        articles = new ArrayList<>();

        List<ArticleEntity> cachedArticles;
        cachedArticles = mArticleViewModel.getAllWords();

        if(cachedArticles != null){
            Log.d("TAG", "cached");
            for(ArticleEntity articleEntity : cachedArticles){
                NewsArticle newsArticle = new NewsArticle(articleEntity.title,
                        articleEntity.description,
                        articleEntity.url,
                        articleEntity.urlToImage,
                        articleEntity.content);
                articles.add(newsArticle);

                final NewsAdapter adapter = new NewsAdapter(this, articles);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.post(() -> {
                    mProgress.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    recyclerView.setAdapter(adapter);
                });
            }
        } else {
            mProgress.setVisibility(View.VISIBLE);
            makeAPICall();
        }

        mSwipeRefreshLayout = findViewById(R.id.refresh);
        mProgress.setVisibility(View.VISIBLE);

        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            new Thread(() -> {
                articles.clear();
                makeAPICall();
            }).start();
            mSwipeRefreshLayout.setRefreshing(false);
        });
    }

    public void makeAPICall(){
        //mProgress.setVisibility(View.VISIBLE);
        mNewsAPI.getNewsArticles("us", APIKey)
                .doOnComplete(() -> {
                  //  Toast.makeText(MainActivity.this, "New articles available. Swipe down to update.", Toast.LENGTH_SHORT).show();
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(articlesWrapper -> {
                    //Log.d("TAG", "I am here");
                    articles = articlesWrapper.getArticles();
                    mArticleViewModel.clearCache();
                    for(NewsArticle article:articles){
                        ArticleEntity articleEntity = new ArticleEntity(article.getTitle(), article.getUrl(), article.getUrlToImge(), article.getDescription(), article.getContent());
                        mArticleViewModel.insert(articleEntity);
                        //Log.d("TAG", "I am here" + article.getTitle());
                    }

                    final NewsAdapter adapter = new NewsAdapter(this, articles);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
                    recyclerView.setLayoutManager(mLayoutManager);
                    recyclerView.post(new Runnable() {
                        @Override
                        public void run() {
                            mProgress.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                            recyclerView.setAdapter(adapter);
                        }
                    });
                });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
