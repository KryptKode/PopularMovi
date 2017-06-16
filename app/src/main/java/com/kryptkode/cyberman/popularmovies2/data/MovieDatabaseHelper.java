package com.kryptkode.cyberman.popularmovies2.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.kryptkode.cyberman.popularmovies2.data.MovieContract.FavouritesEntry;

/**
 * Created by Cyberman on 6/14/2017.
 */

public class MovieDatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "popular_movies.db";
    public static final int DATABASE_VERSION = 1;

    //constants for the SQL commands and syntax
    public static final String CREATE_TABLE = "CREATE TABLE ";
    public static final String COMMA_SEP = ", ";
    public static final String TEXT = " TEXT ";
    public static final String PRIMARY_KEY = " PRIMARY KEY ";
    public static final String NOT_NULL = " NOT NULL ";
    public static final String INTEGER = " INTEGER ";
    public static final String REAL = " REAL ";
    public static final String DEFAULT = " DEFAULT ";
    public static final String TIME_STAMP = "CURRENT_TIMESTAMP";
    public static final String AUTOINCREMENT = " AUTOINCREMENT ";
    public static final String OPEN_PARENTHESIS = "(";
    public static final String CLOSE_PARENTHESIS = " );";


    public MovieDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_CMD = CREATE_TABLE + FavouritesEntry.FAVOURITES_TABLE_NAME + OPEN_PARENTHESIS +
                FavouritesEntry._ID + INTEGER + NOT_NULL +  PRIMARY_KEY + COMMA_SEP +
                FavouritesEntry.FAVOURITES_COLUMN_MOVIE_TITLE + TEXT + NOT_NULL + COMMA_SEP +
                FavouritesEntry.FAVOURITES_COLUMN_MOVIE_RELEASE_DATE + TEXT + COMMA_SEP +
                FavouritesEntry.FAVOURITES_COLUMN_MOVIE_RATING + REAL + DEFAULT + "0.0" + COMMA_SEP +
                FavouritesEntry.FAVOURITES_COLUMN_MOVIE_OVERVIEW + TEXT + COMMA_SEP +
                FavouritesEntry.FAVOURITES_COLUMN_MOVIE_POSTER + TEXT + COMMA_SEP +
                FavouritesEntry.FAVOURITES_COLUMN_MOVIE_TIMESTAMP + TEXT + DEFAULT + TIME_STAMP + CLOSE_PARENTHESIS;

        Log.e("SQL", "-->" + CREATE_TABLE_CMD);
        db.execSQL(CREATE_TABLE_CMD);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + FavouritesEntry.FAVOURITES_TABLE_NAME);
        onCreate(db);
    }
}
