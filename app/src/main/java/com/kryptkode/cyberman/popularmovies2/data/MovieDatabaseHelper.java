package com.kryptkode.cyberman.popularmovies2.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Cyberman on 6/14/2017.
 */

public class MovieDatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "favorite_movies.db";
    public static final int DATABASE_VERSION = 1;
    public MovieDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
