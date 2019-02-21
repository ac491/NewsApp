package com.example.newsapp.API;

import com.example.newsapp.API.Model.ArticlesWrapper;


import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface NewsAPI {

    @Headers("Content-Type: application/json")
    @GET("/v2/top-headlines")
    Observable<ArticlesWrapper> getNewsArticles(@Query("country") String country, @Query("category") String category, @Query("apiKey") String apiKey);

    @Headers("Content-Type: application/json")
    @GET("/v2/everything")
    Observable<ArticlesWrapper> searchArticles(@Query("q") String query, @Query("apiKey") String apiKey);

    @Headers("Content-Type: application/json")
    @GET("/v2/top-headlines")
    Call<ArticlesWrapper> getHeadLine(@Query("country") String country, @Query("pageSize") int pageSize, @Query("apiKey") String apiKey);
}
