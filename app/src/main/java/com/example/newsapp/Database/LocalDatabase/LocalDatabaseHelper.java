package com.example.newsapp.Database.LocalDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.newsapp.API.Model.NewsArticle;

import java.util.ArrayList;
import java.util.List;

public class LocalDatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "articles_db_local";
    public static final String TABLE_NAME = "offline";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_URL = "url";
    public static final String COLUMN_URLTOIMAGE = "urlToImage";
    public static final String COLUMN_CONTENT = "content";
    public static final String COLUMN_TIMESTAMP = "timestamp";

    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
            + COLUMN_TITLE + " TEXT PRIMARY KEY,"
            + COLUMN_DESCRIPTION + " TEXT,"
            + COLUMN_URL + " TEXT,"
            + COLUMN_URLTOIMAGE + " TEXT,"
            + COLUMN_CONTENT + " TEXT,"
            + COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP"
            + ")";

    public LocalDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public boolean insertRecord(NewsArticle newsArticle) {
        List<NewsArticle> articles = getAllArticles();
        if(articles != null && articles.contains(newsArticle)) {
            return false;
        } else {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(COLUMN_TITLE, newsArticle.getTitle());
            values.put(COLUMN_DESCRIPTION, newsArticle.getDescription());
            values.put(COLUMN_URL, newsArticle.getUrl());
            values.put(COLUMN_URLTOIMAGE, newsArticle.getUrlToImge());
            values.put(COLUMN_CONTENT, newsArticle.getContent());
            long id = db.insert(TABLE_NAME, null, values);
            db.close();
            return true;
        }
    }

    public List<NewsArticle> getAllArticles() {
        List<NewsArticle> articles = new ArrayList<NewsArticle>();
        String selectQuery = "SELECT  * FROM " + TABLE_NAME + " ORDER BY " +
                COLUMN_TIMESTAMP + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                NewsArticle article = new NewsArticle(cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_URL)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_URLTOIMAGE)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_CONTENT)));

                articles.add(article);
            } while (cursor.moveToNext());
        }
        db.close();
        return articles;
    }

    public void removeArticle(String title) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_TITLE + " = ?",
                    new String[]{title});
        db.close();

    }
}
