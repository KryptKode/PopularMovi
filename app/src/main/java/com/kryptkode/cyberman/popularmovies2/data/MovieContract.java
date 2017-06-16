package com.kryptkode.cyberman.popularmovies2.data;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Cyberman on 6/14/2017.
 */

public class MovieContract {

    public static final String AUTHORITY = "com.kryptkode.cyberman.popularmovies.data";

    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static class FavouritesEntry implements BaseColumns {
        //constant for the table name
        public static final String FAVOURITES_TABLE_NAME = "favourites";

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(FAVOURITES_TABLE_NAME).build();

        //constants for the database columns
        public static final String FAVOURITES_COLUMN_MOVIE_TITLE = "title";
        public static final String FAVOURITES_COLUMN_MOVIE_RELEASE_DATE = "release_date";
        public static final String FAVOURITES_COLUMN_MOVIE_RATING = "rating";
        public static final String FAVOURITES_COLUMN_MOVIE_OVERVIEW = "overview";
        public static final String FAVOURITES_COLUMN_MOVIE_POSTER = "poster";
        public static final String FAVOURITES_COLUMN_MOVIE_TIMESTAMP = "time";

        //method to build the Uris of each item
        public static  Uri buildFavouritesUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

    }

    public static  class PopularityEntry implements BaseColumns{
        //constant for the table name
        public static final String FAVOURITES_TABLE_NAME = "popularity";

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(FAVOURITES_TABLE_NAME).build();

        //constants for the database columns
        public static final String POPULARITY_COLUMN_MOVIE_TITLE = "title";
        public static final String POPULARITY_COLUMN_MOVIE_RELEASE_DATE = "release_date";
        public static final String POPULARITY_COLUMN_MOVIE_RATING = "rating";
        public static final String POPULARITY_COLUMN_MOVIE_OVERVIEW = "overview";
        public static final String POPULARITY_COLUMN_MOVIE_POSTER = "poster";
        public static final String POPULARITY_COLUMN_MOVIE_TIMESTAMP = "time";

        //method to build the Uris of each item
        public static  Uri buildFavouritesUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }


    //TODO: to add these tables in a later version
    public static class RatingsEntry implements BaseColumns{
        //constant for the table name
        public static final String FAVOURITES_TABLE_NAME = "ratings";

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(FAVOURITES_TABLE_NAME).build();

        //constants for the database columns
        public static final String RATINGS_COLUMN_MOVIE_TITLE = "title";
        public static final String RATINGS_COLUMN_MOVIE_RELEASE_DATE = "release_date";
        public static final String RATINGS_COLUMN_MOVIE_RATING = "rating";
        public static final String RATINGS_COLUMN_MOVIE_OVERVIEW = "overview";
        public static final String RATINGS_COLUMN_MOVIE_POSTER = "poster";
        public static final String RATINGS_COLUMN_MOVIE_TIMESTAMP = "time";

        //method to build the Uris of each item
        public static  Uri buildFavouritesUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}
