package com.example.newsapp.Database.Model;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;


import java.util.ArrayList;
import java.util.List;

@Dao
public interface ArticlesDao {


    @Insert
    void insertArticle(ArticleEntity newsArticle);

    @Query("DELETE FROM article_table")
    void deleteArticles();

    @Query("SELECT * FROM article_table")
    List<ArticleEntity> getAllArticles();

}
