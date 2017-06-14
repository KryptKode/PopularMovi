package com.kryptkode.cyberman.popularmovies2.ui;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.kryptkode.cyberman.popularmovies2.R;
import com.kryptkode.cyberman.popularmovies2.adapter.MovieAdapter;
import com.kryptkode.cyberman.popularmovies2.model.Movie;

import java.util.ArrayList;

public class PopularMoviesActivity extends AppCompatActivity implements MovieAdapter.OnItemClickListener {

    private RecyclerView recyclerView;
    private MovieAdapter movieAdapter;
    private ArrayList<Movie> movieArrayList;
    private GridLayoutManager gridLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popular_movies);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


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
        movieAdapter.setListener(this);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(movieAdapter);




    }

    @Override
    public void onContainerClicked(int position) {
        Movie item = movieArrayList.get(position);
        Intent intent = new Intent(this, MovieDetailsActivity.class);
        startActivity(intent);
    }

    @Override
    public void onFavouritesIconClicked(int position) {
        Movie item = movieArrayList.get(position);

        if(item.isFavourite()){
            item.setFavourite(false);
        }
        else{
            item.setFavourite(true);
        }

        movieAdapter.notifyDataSetChanged();
    }
}
