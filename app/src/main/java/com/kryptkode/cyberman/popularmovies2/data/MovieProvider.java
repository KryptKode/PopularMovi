package com.kryptkode.cyberman.popularmovies2.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.kryptkode.cyberman.popularmovies2.R;

/**
 * Created by Cyberman on 6/14/2017.
 */

public class MovieProvider extends ContentProvider {

    private MovieDatabaseHelper movieDatabaseHelper;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    private static final int FAVOURITES = 100;
    private static final int ONE_FAVOURITE = 101;


    static {
        //matcher for all the favourites in the database
        sUriMatcher.addURI(MovieContract.AUTHORITY,
                MovieContract.FavouritesEntry.FAVOURITES_TABLE_NAME, FAVOURITES);

        //matcher for one favourite in the database
       sUriMatcher.addURI(MovieContract.AUTHORITY,
               MovieContract.FavouritesEntry.FAVOURITES_TABLE_NAME + "/#", ONE_FAVOURITE );

    }

    //called when the content provider is created
    @Override
    public boolean onCreate() {

        movieDatabaseHelper = new MovieDatabaseHelper(getContext());

        return true;
    }


    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        final SQLiteDatabase database = movieDatabaseHelper.getReadableDatabase();

        Cursor cursor;
        switch(sUriMatcher.match(uri)){
            //get all contents of the db to use in the recycler view
            case FAVOURITES:
                sortOrder = MovieContract.FavouritesEntry.FAVOURITES_COLUMN_MOVIE_TIMESTAMP + " ASC";
                cursor = database.query(MovieContract.FavouritesEntry.FAVOURITES_TABLE_NAME, projection,
                        selection, selectionArgs, null, null, sortOrder );
                break;
            //get one of the favorite specified by the URi ID
            case ONE_FAVOURITE:
                selection = MovieContract.FavouritesEntry._ID + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                sortOrder = MovieContract.FavouritesEntry.FAVOURITES_COLUMN_MOVIE_TIMESTAMP + " ASC";
                cursor = database.query(MovieContract.FavouritesEntry.FAVOURITES_TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            //default exception
            default:
                throw  new UnsupportedOperationException(getContext().getString(R.string.unknown_uri) + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }


    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {

        // get a reference to the database with the helper, writeable because we want to write to the data base
        SQLiteDatabase database = movieDatabaseHelper.getWritableDatabase();

        Uri returnedUri; //Uri to be returned

        //check the Uri if it matches the Uris defined above
        switch (sUriMatcher.match(uri)){
            case FAVOURITES:

                //database method insert( returns the id of the row that was inserted into the database)
                long id = database.insert(MovieContract.FavouritesEntry.FAVOURITES_TABLE_NAME, null, values);
                // the db.insert() method returns -1 if the insert was unsuccessful
                    returnedUri = MovieContract.FavouritesEntry.buildFavouritesUri(id);

                break;
            default:
                throw  new UnsupportedOperationException(getContext().getString(R.string.unknown_uri) + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return  returnedUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {

        SQLiteDatabase database = movieDatabaseHelper.getWritableDatabase();
        int deletedItems;
        switch (sUriMatcher.match(uri)){

            case FAVOURITES:

                deletedItems = database.delete(MovieContract.FavouritesEntry.FAVOURITES_TABLE_NAME, null, null );

                break;
            case ONE_FAVOURITE:

                selection = MovieContract.FavouritesEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                deletedItems = database.delete(MovieContract.FavouritesEntry.FAVOURITES_TABLE_NAME, selection, selectionArgs );
                break;
            default:
                throw  new UnsupportedOperationException(getContext().getString(R.string.unknown_uri) + uri);
        }

        if (deletedItems != 0){

            getContext().getContentResolver().notifyChange(uri, null);
        }

        return deletedItems;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
