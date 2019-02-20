package com.example.newsapp.Database.Model;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

@Database(entities = {ArticleEntity.class}, version = 1)
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
                            .addCallback(new Callback() {
                                @Override
                                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                                    super.onCreate(db);
                                    Log.d("TAG","oncreate");
                                }

                                @Override
                                public void onOpen(@NonNull SupportSQLiteDatabase db) {
                                    super.onOpen(db);
                                    Log.d("TAG","onOpen");
                                }
                            })
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
