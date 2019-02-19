package com.example.newsapp.Database;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;


import com.example.newsapp.API.Model.NewsArticle;
import com.example.newsapp.Database.Model.ArticleEntity;

import java.util.ArrayList;
import java.util.List;

public class ArticlesViewModel extends AndroidViewModel{
    private ArticleRepository mRepository;

    private List<ArticleEntity> mAllWords;

    public ArticlesViewModel (Application application) {
        super(application);
        mRepository = new ArticleRepository(application);
        mAllWords = mRepository.getAllWords();
    }

    public void clearCache(){
        mRepository.delete();
    }

    public List<ArticleEntity> getAllWords() { return mAllWords; }

    public void insert(ArticleEntity word) { mRepository.insert(word); }
}
