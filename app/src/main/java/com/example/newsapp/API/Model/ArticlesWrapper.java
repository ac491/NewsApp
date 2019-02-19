package com.example.newsapp.API.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ArticlesWrapper {

    @SerializedName("articles")
    @Expose
    private ArrayList<NewsArticle> articles;

    public ArrayList<NewsArticle> getArticles() {
        return articles;
    }

    public void setArticles(ArrayList<NewsArticle> articles) {
        this.articles = articles;
    }
}
