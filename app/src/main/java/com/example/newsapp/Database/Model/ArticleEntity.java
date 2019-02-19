package com.example.newsapp.Database.Model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import io.reactivex.annotations.NonNull;

@Entity(tableName = "article_table")
public class ArticleEntity {

    public ArticleEntity(String title, String url, String urlToImage, String description, String content) {
        this.title = title;
        this.url = url;
        this.urlToImage = urlToImage;
        this.description = description;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public String getUrlToImage() {
        return urlToImage;
    }

    public String getDescription() {
        return description;
    }

    public String getContent() {
        return content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "title")
    public String title;

    @ColumnInfo(name = "url")
    public String url;

    @ColumnInfo(name = "urlToImage")
    public String urlToImage;

    @ColumnInfo(name = "description")
    public String description;

    @ColumnInfo(name = "content")
    public String content;

}
