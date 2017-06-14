package com.kryptkode.cyberman.popularmovies2.ui;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.kryptkode.cyberman.popularmovies2.R;
import com.kryptkode.cyberman.popularmovies2.adapter.MovieAdapter;
import com.kryptkode.cyberman.popularmovies2.model.Movie;

import java.util.ArrayList;

public class PopularMoviesActivity extends AppCompatActivity  {


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


        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            gridLayoutManager = new GridLayoutManager(this, 2);
        }
        else{
            gridLayoutManager = new GridLayoutManager(this, 3 );
        }
        movieArrayList = Movie.testLoad();
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        movieAdapter = new MovieAdapter(this, movieArrayList);
        //set the listener defined in the adapter class
        movieAdapter.setListener(onItemClickListener); //listener defined to handle clicks on the container and favorites icon
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(movieAdapter);

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
                Snackbar.make(findViewById(R.id.movie_container), getString(R.string.sorting_by_popularity), Snackbar.LENGTH_SHORT).show();
                break;

            case R.id.action_sort_by_rating:
                Snackbar.make(findViewById(R.id.movie_container), getString(R.string.sorting_by_rating), Snackbar.LENGTH_SHORT).show();
                break;
            case R.id.action_favorites:
                Snackbar.make(findViewById(R.id.movie_container), getString(R.string.connected), Snackbar.LENGTH_SHORT).show();
                break;
        }
        return true;
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
            bundle.putDouble(Movie.MOVIE_VOTE, item.getVoteAverage());
            bundle.putBoolean(Movie.MOVIE_FAVOURITE, item.isFavourite());
            Intent intent = new Intent(PopularMoviesActivity.this, MovieDetailsActivity.class);
            intent.putExtra(Movie.MOVIE_RELEASE_DATE, bundle);
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
                Snackbar.make(findViewById(R.id.movie_container), getString(R.string.added_to_favorites), Snackbar.LENGTH_SHORT).show();
            }

            movieAdapter.notifyDataSetChanged();
        }
    };

}