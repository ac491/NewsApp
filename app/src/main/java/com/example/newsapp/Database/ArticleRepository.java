package com.example.newsapp.Database;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import com.example.newsapp.Database.Model.ArticleDatabase;
import com.example.newsapp.Database.Model.ArticleEntity;
import com.example.newsapp.Database.Model.ArticlesDao;

import java.util.ArrayList;
import java.util.List;

public class ArticleRepository {

    private ArticlesDao mArticleDao;
    private List<ArticleEntity> mAllArticles;

    ArticleRepository(Application application) {
        ArticleDatabase db = ArticleDatabase.getDatabase(application);
        mArticleDao = db.ArticlesDao();
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                mAllArticles = mArticleDao.getAllArticles();
            }
        });
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    List<ArticleEntity> getAllWords() {
        return mAllArticles;
    }


    public void insert (ArticleEntity article) {
        Log.d("TAG","Insert");
        new insertAsyncTask(mArticleDao).execute(article);
    }

    public void delete (){
        new Thread(new Runnable() {
            @Override
            public void run() {
                mArticleDao.deleteArticles();
            }
        }).start();

    }

    private static class insertAsyncTask extends AsyncTask<ArticleEntity, Void, Void> {

        private ArticlesDao mAsyncTaskDao;

        insertAsyncTask(ArticlesDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final ArticleEntity... params) {
                mAsyncTaskDao.insertArticle(params[0]);
                return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    ArrayList<ArticleEntity> articles = (ArrayList<ArticleEntity>) mAsyncTaskDao.getAllArticles();
                    Log.d("TAG", articles.size()+"");
                }
            }).start();

        }
    }

}
