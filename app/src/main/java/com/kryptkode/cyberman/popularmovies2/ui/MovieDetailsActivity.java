package com.kryptkode.cyberman.popularmovies2.ui;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.kryptkode.cyberman.popularmovies2.R;
import com.kryptkode.cyberman.popularmovies2.model.Movie;

public class MovieDetailsActivity extends AppCompatActivity {

    private TextView ratingsTextView;
    private TextView overviewTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        ratingsTextView = (TextView) findViewById(R.id.rating);
        overviewTextView = (TextView) findViewById(R.id.overview_content);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
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

    }

}
