package com.kryptkode.cyberman.popularmovies2.ui;

import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.kryptkode.cyberman.popularmovies2.R;
import com.kryptkode.cyberman.popularmovies2.adapters.MovieAdapter;
import com.kryptkode.cyberman.popularmovies2.data.MovieContract;
import com.kryptkode.cyberman.popularmovies2.model.Movie;
import com.kryptkode.cyberman.popularmovies2.utilities.JsonGet;
import com.kryptkode.cyberman.popularmovies2.utilities.NetworkUtil;
import com.kryptkode.cyberman.popularmovies2.utilities.EndlessRecyclerViewScrollListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.view.View.INVISIBLE;

public class PopularMoviesActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {


    private static String MOVIE_URL = "url" ;
    private final static String SORT_PARAM = NetworkUtil.POPULAR;
    //views for indicating loading
    private ProgressBar progressBar;
    private TextView loadingTextView;

    //views for indicating error, no internet
    private TextView errorTextView;
    private ImageView errorImageView;

    private RecyclerView recyclerView; //recycler view
    private MovieAdapter movieAdapter;
    private ArrayList<Movie> movieArrayList;
    private GridLayoutManager gridLayoutManager;

    private EndlessRecyclerViewScrollListener endlessRecyclerViewScrollListener;

    private static final int LOADER_ID = 100;

    private boolean dataHasLoaded = false; //used to determine if the data has loaded
    private int pageNumber = 1;
    private String moviesURL;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popular_movies);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //instantiate the views
        progressBar = (ProgressBar) findViewById(R.id.loading_progress_bar);
        loadingTextView = (TextView) findViewById(R.id.loading_text_view);
        errorTextView = (TextView) findViewById(R.id.error_text_view);
        errorImageView = (ImageView) findViewById(R.id.error_image_view);

        //instantiate the movie url
        moviesURL = NetworkUtil.buildPopularMoviesURL(SORT_PARAM).toString();

        //initialize the loader
        getSupportLoaderManager().initLoader(LOADER_ID, null, this);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            gridLayoutManager = new GridLayoutManager(this, 2);
        }
        else{
            gridLayoutManager = new GridLayoutManager(this, 3 );
        }
        movieArrayList = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        movieAdapter = new MovieAdapter(this, movieArrayList);
        //set the listener defined in the adapter class
        movieAdapter.setListener(onItemClickListener); //listener defined to handle clicks on the container and favorites icon
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(movieAdapter);

        getData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.action_sort_by_popularity:
                getMovies(NetworkUtil.POPULAR);
                break;

            case R.id.action_sort_by_rating:
                getMovies( NetworkUtil.RATING);
                break;
            case R.id.action_favorites:
                Intent intent = new Intent(this, FavouritesActivity.class);
                startActivity(intent);
                break;
        }
        return true;
    }


    // called when user selects a means to sort the movies
    private void getMovies(String sort){
        moviesURL = NetworkUtil.buildPopularMoviesURL(sort).toString(); //sets the URl to the right one for rating
        if (NetworkUtil.isOnline(getApplicationContext())){
           //TODO: Reset Views
            if (sort.equals(NetworkUtil.POPULAR)){
                Snackbar.make(findViewById(R.id.movie_container), getString(R.string.sorting_by_popularity), Snackbar.LENGTH_SHORT).show();
            }
            else{
                Snackbar.make(findViewById(R.id.movie_container), getString(R.string.sorting_by_rating), Snackbar.LENGTH_SHORT).show();
            }
            getData();
        }
        else{
            Snackbar.make(findViewById(R.id.movie_container), getString(R.string.is_not_connected), Snackbar.LENGTH_SHORT).show();

        }
    }



    public void showNoInternetError(){
        recyclerView.setVisibility(View.INVISIBLE);
        errorImageView.setVisibility(View.VISIBLE);
        errorTextView.setVisibility(View.VISIBLE);
        Snackbar.make(findViewById(R.id.movie_container), getString(R.string.turn_on_internet), Snackbar.LENGTH_LONG).show();
    }
    public void hideNoInternetError(){
        recyclerView.setVisibility(View.VISIBLE);
        errorImageView.setVisibility(View.INVISIBLE);
        errorTextView.setVisibility(View.INVISIBLE);
        Snackbar.make(findViewById(R.id.movie_container), getString(R.string.connected), Snackbar.LENGTH_LONG).show();
    }

    public void showLoadingIndicators(){
        loadingTextView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
    }

    public void hideLoadingIndicators(){
        loadingTextView.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
    }

    private MovieAdapter.OnItemClickListener onItemClickListener = new MovieAdapter.OnItemClickListener() {
        //called when user clicks a movie poster or the text view
        @Override
        public void onContainerClicked(int position) {
            Movie item = movieArrayList.get(position);
            Bundle bundle = new Bundle();
            bundle.putString(Movie.MOVIE_TITLE, item.getOriginalTitle());
            bundle.putString(Movie.MOVIE_OVERVIEW, item.getOverview());
            bundle.putString(Movie.POSTER_URL, item.getPoster(true));
            bundle.putString(Movie.MOVIE_RELEASE_DATE, item.getReleaseDate());
            bundle.putDouble(Movie.MOVIE_VOTE, item.getRatings());
            bundle.putInt(Movie.MOVIE_ID, item.getId());
            bundle.putBoolean(Movie.MOVIE_FAVOURITE, item.isFavourite());
            Intent intent = new Intent(PopularMoviesActivity.this, MovieDetailsActivity.class);
            intent.putExtra(Movie.BUNDLE, bundle);
            startActivity(intent);
        }

        //called when the user clicks the favourites icon
        @Override
        public void onFavouritesIconClicked(int position) {
            Movie item = movieArrayList.get(position);


            if (item.isFavourite()) {
                item.setFavourite(false);
                Snackbar.make(findViewById(R.id.movie_container), getString(R.string.removed_from_favorites), Snackbar.LENGTH_SHORT).show();
            } else {
                item.setFavourite(true);

                ContentValues values = new ContentValues();
                values.put(MovieContract.FavouritesEntry._ID, item.getId());
                values.put(MovieContract.FavouritesEntry.FAVOURITES_COLUMN_MOVIE_TITLE, item.getOriginalTitle());
                values.put(MovieContract.FavouritesEntry.FAVOURITES_COLUMN_MOVIE_RATING, item.getRatings());
                values.put(MovieContract.FavouritesEntry.FAVOURITES_COLUMN_MOVIE_OVERVIEW, item.getOverview());
                values.put(MovieContract.FavouritesEntry.FAVOURITES_COLUMN_MOVIE_POSTER, item.getPoster(false));
                values.put(MovieContract.FavouritesEntry.FAVOURITES_COLUMN_MOVIE_RELEASE_DATE, item.getReleaseDate());

                Uri uri = getContentResolver().insert(MovieContract.FavouritesEntry.CONTENT_URI, values);

                Snackbar.make(findViewById(R.id.movie_container), getString(R.string.added_to_favorites) + " -->" +uri, Snackbar.LENGTH_SHORT).show();
            }

            movieAdapter.notifyDataSetChanged();
        }
    };

    public void getData(){
        if (NetworkUtil.isOnline(getApplicationContext())){
            //get data from the net

            Bundle queryBundle = new Bundle();
            queryBundle.putString(MOVIE_URL, moviesURL);

            LoaderManager loaderManager = getSupportLoaderManager();
            Loader<String> loader = loaderManager.getLoader(LOADER_ID);
            if (loader == null) {
                loaderManager.initLoader(LOADER_ID, queryBundle, this);
            } else {
                loaderManager.restartLoader(LOADER_ID, queryBundle, this);
            }
        }
        else{
            Snackbar.make(findViewById(R.id.movie_container), getString(R.string.sorting_by_rating), Snackbar.LENGTH_SHORT).show();
           //if the datahas not loaded or the movies array list is null, it implies that the data has not loaded once
            if (!dataHasLoaded || null == movieArrayList){
                showNoInternetError();
            }

        }
    }

    private void loadMovieData(String jsonData) {
        String poster;
        String title;
        String releaseDate;
        String overview;
        double popularity;
        double vote_average;
        int id;
        if (jsonData != null) {
            try {
                JSONObject movie = new JSONObject(jsonData);
                JSONArray results = movie.getJSONArray("results");
                for (int i = 0; i < results.length(); i++) {
                    JSONObject currentJson = results.getJSONObject(i);
                    poster = currentJson.getString("poster_path");
                    title = currentJson.getString("original_title");
                    overview = currentJson.getString("overview");
                    releaseDate = currentJson.getString("release_date");
                    popularity = currentJson.getDouble("popularity");
                    vote_average = currentJson.getDouble("vote_average");
                    id = currentJson.getInt("id");
                    Movie movieObject = new Movie(title, poster, overview, releaseDate, vote_average, id);
                    movieArrayList.add(movieObject);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Loader<String> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<String>(this) {

            String JSONData;
            @Override
            protected void onStartLoading() {

                /* If no arguments were passed, we don't have a query to perform. Simply return. */
                if (args == null) {
                    return;
                }

                /*
                 * When we initially begin loading in the background, we want to display the
                 * loading indicator to the user
                 */
                showLoadingIndicators();

                //  If JSONData is not null, deliver that result. Otherwise, force a load
                /*
                 * If we already have cached results, just deliver them now. If we don't have any
                 * cached results, force a load.
                 */
                if (JSONData != null) {
                    deliverResult(JSONData);
                } else {
                    forceLoad();
                }
            }

            @Override
            public String loadInBackground() {
                String str = null;
                JSONObject jsonObject = JsonGet.getDataFromWeb(moviesURL, pageNumber);
                if(jsonObject != null)
                   str = jsonObject.toString();
                Log.e("URL", String.valueOf(pageNumber));
                //Log.e("URL", str);
                return str;

            }

            @Override
            public void deliverResult(String data) {
                JSONData = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        loadMovieData(data);
        hideLoadingIndicators();
        hideNoInternetError();
        dataHasLoaded = true;
        movieAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }
}