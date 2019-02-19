package com.example.newsapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.newsapp.API.Model.NewsArticle;
import com.example.newsapp.Activity.DisplayArticles;
import com.example.newsapp.R;

import java.io.Serializable;
import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

    private Context mContext;
    private List<NewsArticle> mNewsArticleList;

    public NewsAdapter(Context mContext, List<NewsArticle> mNewsArticleList) {
        this.mContext = mContext;
        this.mNewsArticleList = mNewsArticleList;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.news_card, parent, false);

        return new NewsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, final int position) {

        NewsArticle article = mNewsArticleList.get(position);
        holder.title.setText(article.getTitle());
        holder.summary.setText(article.getDescription());
        Glide.with(mContext).load(article.getUrlToImge()).into(holder.article_thumbnail);
        holder.article_thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, DisplayArticles.class);
                Bundle b = new Bundle();
                b.putSerializable("newsArticle", (Serializable) mNewsArticleList.get(position));
                intent.putExtras(b);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mNewsArticleList.size();
    }

    public class NewsViewHolder extends RecyclerView.ViewHolder {
        public TextView title, summary;
        public ImageView article_thumbnail;

        public NewsViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            summary = (TextView) view.findViewById(R.id.summary);
            article_thumbnail = (ImageView) view.findViewById(R.id.news_thumbnail);
        }
    }
}
