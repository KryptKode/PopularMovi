package com.kryptkode.cyberman.popularmovies2.ui;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.kryptkode.cyberman.popularmovies2.R;
import com.kryptkode.cyberman.popularmovies2.adapter.MovieAdapter;
import com.kryptkode.cyberman.popularmovies2.model.Movie;

public class MovieDetailsActivity extends AppCompatActivity {

    private FloatingActionButton fab;
    private TextView ratingsTextView;
    private TextView overviewTextView;
    private boolean check;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        ratingsTextView = (TextView) findViewById(R.id.rating);
        overviewTextView = (TextView) findViewById(R.id.overview_content);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (check){
                    changeIconStatus(false);
                    check = false;
                    Snackbar.make(view, R.string.removed_from_favorites, Snackbar.LENGTH_SHORT).show();
                }
                else{
                    changeIconStatus(true);
                    check = true;
                    Snackbar.make(view, R.string.added_to_favorites, Snackbar.LENGTH_SHORT).show();
                }
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getBundleExtra(Movie.MOVIE_RELEASE_DATE);
        if (bundle.containsKey(Movie.MOVIE_TITLE)){
            getSupportActionBar().setTitle(bundle.getString(Movie.MOVIE_TITLE));

        }
        if (bundle.containsKey(Movie.MOVIE_OVERVIEW)){
            overviewTextView.setText(bundle.getString(Movie.MOVIE_OVERVIEW));
        }
        if (bundle.containsKey(Movie.MOVIE_VOTE)){
            ratingsTextView.setText(String.valueOf(bundle.getDouble(Movie.MOVIE_VOTE)));
        }
        if (bundle.containsKey(Movie.MOVIE_FAVOURITE)){
            check = bundle.getBoolean(Movie.MOVIE_FAVOURITE);
         changeIconStatus(check);
        }

    }

    public void changeIconStatus(boolean status){
        if (status){
            fab.setImageResource(R.drawable.ic_star_black_24dp);
        }
        else {
            fab.setImageResource(R.drawable.ic_star_border_black_24dp);
        }
    }

}
