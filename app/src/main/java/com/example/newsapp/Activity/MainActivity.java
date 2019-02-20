package com.example.newsapp.Activity;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
    private android.support.v7.widget.SearchView mSearchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mArticleViewModel = ViewModelProviders.of(MainActivity.this).get(ArticlesViewModel.class);
        recyclerView = findViewById(R.id.recycler_view);
        mProgress = findViewById(R.id.progress);
        recyclerView.setVisibility(View.INVISIBLE);
        mNewsAPI = APIUtils.getAPIService();
        articles = new ArrayList<>();

        //List<ArticleEntity> cachedArticles;

                List<ArticleEntity> cachedArticles = mArticleViewModel.getAllWords();
                if(cachedArticles != null){
                    Log.d("TAG", "cached");
                    for(ArticleEntity articleEntity : cachedArticles){
                        NewsArticle newsArticle = new NewsArticle(articleEntity.title,
                                articleEntity.description,
                                articleEntity.url,
                                articleEntity.urlToImage,
                                articleEntity.content);
                        articles.add(newsArticle);

                        final NewsAdapter adapter = new NewsAdapter(MainActivity.this, articles);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(MainActivity.this);
                        recyclerView.setLayoutManager(mLayoutManager);
                        recyclerView.post(() -> {
                            mProgress.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                            recyclerView.setAdapter(adapter);
                        });
                    }
                } else {
                    if(isNetworkAvailable()) {
                        mProgress.setVisibility(View.VISIBLE);
                        makeAPICall();
                    } else {
                        mProgress.setVisibility(View.GONE);
                        Toast.makeText(MainActivity.this, "Internet connection not available.", Toast.LENGTH_LONG).show();
                    }
                }




        mSwipeRefreshLayout = findViewById(R.id.refresh);


        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            mProgress.setVisibility(View.VISIBLE);
            if(isNetworkAvailable()) {
                new Thread(() -> {
                    articles.clear();
                    makeAPICall();
                }).start();
                mSwipeRefreshLayout.setRefreshing(false);
            } else {
                mProgress.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, "Internet connection not available.", Toast.LENGTH_LONG).show();
            }
        });


        mSearchView = findViewById(R.id.action_search);

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

    public void searchAPICall(String query){
        mNewsAPI.searchArticles(query, APIKey)
                .doOnComplete(() -> {
                    //  Toast.makeText(MainActivity.this, "New articles available. Swipe down to update.", Toast.LENGTH_SHORT).show();
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(articlesWrapper -> {
                    //Log.d("TAG", "I am here");
                    ArrayList<NewsArticle> news = articlesWrapper.getArticles();
                    for(NewsArticle article:news){
                        ArticleEntity articleEntity = new ArticleEntity(article.getTitle(), article.getUrl(), article.getUrlToImge(), article.getDescription(), article.getContent());
                        mArticleViewModel.insert(articleEntity);
                        //Log.d("TAG", "I am here" + article.getTitle());
                    }

                    final NewsAdapter adapter = new NewsAdapter(this, news);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
                    recyclerView.setLayoutManager(mLayoutManager);
                    recyclerView.post(() -> {
                        mProgress.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        recyclerView.setAdapter(adapter);
                    });
                });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        mSearchView = (android.support.v7.widget.SearchView) item.getActionView();
        mSearchView.setQueryHint("Enter Article title");
        mSearchView.setSubmitButtonEnabled(true);
        mSearchView.setOnQueryTextListener(new android.support.v7.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(final String s) {
                mProgress.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.INVISIBLE);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("123", "zfhkjzflk");
                        searchAPICall(s);
                    }
                }).start();
                return true;
            }

            @Override
            public boolean onQueryTextChange(final String s) {
                return false;
            }
        });
        mSearchView.setOnCloseListener(() -> {
            NewsAdapter newsAdapter = new NewsAdapter(MainActivity.this, articles);
            recyclerView.setAdapter(newsAdapter);
            return false;
        });

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

        if (id == R.id.offline) {
            Intent intent = new Intent(MainActivity.this, OfflineNews.class);
            startActivity(intent);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}

