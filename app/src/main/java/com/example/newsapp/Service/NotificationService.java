package com.example.newsapp.Service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.newsapp.API.Model.ArticlesWrapper;
import com.example.newsapp.API.Model.NewsArticle;
import com.example.newsapp.API.NewsAPI;
import com.example.newsapp.Activity.MainActivity;
import com.example.newsapp.R;
import com.example.newsapp.Utils.APIUtils;
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationService extends JobService {
    @Override
    public boolean onStartJob(JobParameters job) {
        NewsAPI newsAPI = APIUtils.getAPIService();
        Call<ArticlesWrapper> call = newsAPI.getHeadLine( getApplicationContext().getResources().getConfiguration().locale.getCountry(), 1, MainActivity.APIKey);
        call.enqueue(new Callback<ArticlesWrapper>() {
            @Override
            public void onResponse(Call<ArticlesWrapper> call, Response<ArticlesWrapper> response) {
                ArrayList<NewsArticle> articles = new ArrayList<>();
                articles = response.body().getArticles();
                NewsArticle article = articles.get(0);
                Log.d("TAG", "Notification " + article.getTitle());
                showNotification(article.getTitle(), article.getDescription());
            }

            @Override
            public void onFailure(Call<ArticlesWrapper> call, Throwable t) {

            }
        });
        return false;
    }

    void showNotification(String title, String content) {
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("default",
                    "YOUR_CHANNEL_NAME",
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("YOUR_NOTIFICATION_CHANNEL_DISCRIPTION");
            mNotificationManager.createNotificationChannel(channel);
        }
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), "default")
                .setSmallIcon(R.mipmap.ic_launcher) // notification icon
                .setContentTitle(title) // title for notification
                .setContentText(content)// message for notification
                .setSound(defaultSoundUri) // set alarm sound for notification
                .setAutoCancel(true); // clear notification after click
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pi);
        mNotificationManager.notify(0, mBuilder.build());
    }


    @Override
    public boolean onStopJob(JobParameters job) {
        return false;
    }
}
