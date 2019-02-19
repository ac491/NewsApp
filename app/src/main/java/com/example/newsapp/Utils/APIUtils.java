package com.example.newsapp.Utils;


import com.example.newsapp.API.NewsAPI;

public class APIUtils {

    private APIUtils() {}

    public static final String BASE_URL = "https://newsapi.org";

    public static NewsAPI getAPIService() {

        return RetrofitClient.getClient(BASE_URL).create(NewsAPI.class);
    }

}
