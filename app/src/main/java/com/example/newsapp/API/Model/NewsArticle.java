package com.example.newsapp.API.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class NewsArticle implements Serializable{

    public NewsArticle(String title, String description, String url, String urlToImge, String content) {
        this.title = title;
        this.description = description;
        this.url = url;
        this.urlToImge = urlToImge;
        this.content = content;
    }

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("url")
    @Expose
    private String url;

    @SerializedName("urlToImage")
    @Expose
    private String urlToImge;

    @SerializedName("content")
    @Expose
    private String content;

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getUrl() {
        return url;
    }

    public String getUrlToImge() {
        return urlToImge;
    }

    public String getContent() {
        return content;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setUrlToImge(String urlToImge) {
        this.urlToImge = urlToImge;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
