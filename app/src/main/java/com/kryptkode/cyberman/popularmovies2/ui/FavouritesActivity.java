package com.kryptkode.cyberman.popularmovies2.ui;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;

import com.kryptkode.cyberman.popularmovies2.R;
import com.kryptkode.cyberman.popularmovies2.adapters.FavouriteMoviesAdapter;
import com.kryptkode.cyberman.popularmovies2.data.MovieContract;
import com.kryptkode.cyberman.popularmovies2.model.Movie;

public class FavouritesActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final String LOG_TAG = FavouritesActivity.class.getSimpleName();
    public static final int LOADER_ID = 0;
    private RecyclerView recyclerView;
    private FavouriteMoviesAdapter favouriteMoviesAdapter;
    private LinearLayoutManager linearLayoutManager;

    //cursor to hold the data queryied from the database
    private Cursor cursor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Favourite Movies");
        recyclerView = (RecyclerView) findViewById(R.id.favorites_recycler_view); //gert reference to the recycler view
        favouriteMoviesAdapter = new FavouriteMoviesAdapter(this); //instantiate the adadpter
        linearLayoutManager = new LinearLayoutManager(this); //instantiate the layout manager
        favouriteMoviesAdapter.setClickListener(clickListener);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(favouriteMoviesAdapter);

        getSupportLoaderManager().initLoader(LOADER_ID, null, this);

        //swipe to delete, implemented in OnSwiped()
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                // Build appropriate uri with String row id appended
                String stringId = Integer.toString((int) viewHolder.itemView.getTag());
                Uri uri = MovieContract.FavouritesEntry.CONTENT_URI;
                uri = uri.buildUpon().appendPath(stringId).build();

                // COMPLETED (2) Delete a single row of data using a ContentResolver
                getContentResolver().delete(uri, null, null);

                // COMPLETED (3) Restart the loader to re-query for all tasks after a deletion
               getSupportLoaderManager().restartLoader(LOADER_ID, null, FavouritesActivity.this);
                favouriteMoviesAdapter.notifyDataSetChanged();
               Log.e("DEL", "--> " + viewHolder.getAdapterPosition());

            }
        }).attachToRecyclerView(recyclerView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getSupportLoaderManager().restartLoader(LOADER_ID, null, this);
    }

    // called when any of the favourite movies in the recycler view is clicked
    FavouriteMoviesAdapter.ItemClickListener clickListener = new FavouriteMoviesAdapter.ItemClickListener() {
        @Override
        public void onItemClick(int position) {
            Movie item = favouriteMoviesAdapter.getFavouriteMoviesDataFromCursor(cursor, position);
            Bundle bundle = new Bundle();
            bundle.putString(Movie.MOVIE_TITLE, item.getOriginalTitle());
            bundle.putString(Movie.MOVIE_OVERVIEW, item.getOverview());
            bundle.putString(Movie.POSTER_URL, item.getPosterPath());
            bundle.putString(Movie.MOVIE_RELEASE_DATE, item.getReleaseDate());
            bundle.putDouble(Movie.MOVIE_VOTE, item.getRatings());
            bundle.putBoolean(Movie.MOVIE_FAVOURITE, true);
            Intent intent = new Intent(FavouritesActivity.this, MovieDetailsActivity.class);
            intent.putExtra(Movie.MOVIE_FAVOURITE, bundle);
            startActivity(intent);
        }
    };


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<Cursor>(this) {
            // Initialize a Cursor, this will hold all the task data

            // onStartLoading() is called when a loader first starts loading data
            @Override
            protected void onStartLoading() {
                if (cursor != null) {
                    // Delivers any previously loaded data immediately
                    deliverResult(cursor);
                } else {
                    // Force a new load
                    forceLoad();
                }
            }

            // loadInBackground() performs asynchronous loading of data
            @Override
            public Cursor loadInBackground() {
                // Will implement to load data
                Cursor cursor;
                // Query and load all task data in the background; sort by priority
                // [Hint] use a try/catch block to catch any errors in loading data

                try {
                    cursor = getContentResolver().query(MovieContract.FavouritesEntry.CONTENT_URI,
                            null,
                            null,
                            null,
                            null);
                    return cursor;

                } catch (Exception e) {
                    Log.e(LOG_TAG, "Failed to asynchronously load data.");
                    e.printStackTrace();
                    return null;
                }



            }

            // deliverResult sends the result of the load, a Cursor, to the registered listener
            public void deliverResult(Cursor data) {
               cursor = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        favouriteMoviesAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        favouriteMoviesAdapter.swapCursor(null);
    }
}

