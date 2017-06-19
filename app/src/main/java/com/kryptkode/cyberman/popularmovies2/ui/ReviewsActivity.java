package com.kryptkode.cyberman.popularmovies2.ui;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.kryptkode.cyberman.popularmovies2.R;
import com.kryptkode.cyberman.popularmovies2.adapters.ReviewsAdapter;
import com.kryptkode.cyberman.popularmovies2.model.Reviews;

import java.util.ArrayList;

public class ReviewsActivity extends AppCompatActivity {
    private RecyclerView reviewsRecyclerView;
    private LinearLayoutManager reviewsLinearLayoutManager;
    private ReviewsAdapter reviewsAdapter;
    private ArrayList<Reviews> reviewsArrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getBundleExtra(Reviews.REVIEWS);
        reviewsArrayList = bundle.getParcelableArrayList(Reviews.REVIEWS);
        getSupportActionBar().setTitle(getString(R.string.reviews_title, bundle.getString(Reviews.TITLE)));
        reviewsRecyclerView = (RecyclerView) findViewById(R.id.reviews_activity_rv);
        reviewsLinearLayoutManager = new LinearLayoutManager(this);
        reviewsAdapter = new ReviewsAdapter(this, reviewsArrayList, false);
        reviewsRecyclerView.setLayoutManager(reviewsLinearLayoutManager);
        reviewsRecyclerView.setAdapter(reviewsAdapter);

    }

}
