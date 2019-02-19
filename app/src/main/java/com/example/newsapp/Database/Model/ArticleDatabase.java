package com.example.newsapp.Database.Model;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {ArticleEntity.class}, version = 1, exportSchema = false)
public abstract class ArticleDatabase extends RoomDatabase {
    public abstract ArticlesDao ArticlesDao();

    private static volatile ArticleDatabase INSTANCE;

    //To make the database singleton
    public static ArticleDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (ArticleDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            ArticleDatabase.class, "article_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
