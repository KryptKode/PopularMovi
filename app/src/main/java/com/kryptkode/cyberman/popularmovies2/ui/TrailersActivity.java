package com.kryptkode.cyberman.popularmovies2.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.kryptkode.cyberman.popularmovies2.R;
import com.kryptkode.cyberman.popularmovies2.adapters.TrailersAdapter;
import com.kryptkode.cyberman.popularmovies2.model.Trailers;

import java.util.ArrayList;

public class TrailersActivity extends AppCompatActivity implements TrailersAdapter.OnClickTrailerIconListener {

    private RecyclerView trailersRecyclerView;
    private StaggeredGridLayoutManager trailersGridLayoutManager;
    private TrailersAdapter trailersAdapter;
    private ArrayList<Trailers> trailersArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trailers);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getBundleExtra(Trailers.TRAILERS);
        trailersArrayList = bundle.getParcelableArrayList(Trailers.TRAILERS);
        getSupportActionBar().setTitle(getString(R.string.trailers_activity_title, bundle.getString(Trailers.TITLE)));

        trailersRecyclerView = (RecyclerView) findViewById(R.id.trailers_activity_rv);
        trailersAdapter = new TrailersAdapter(this, trailersArrayList);
        trailersAdapter.setTrailerIconListener(this);
        trailersGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        trailersRecyclerView.setLayoutManager(trailersGridLayoutManager);
        trailersRecyclerView.setAdapter(trailersAdapter);

    }

    @Override
    public void onClickTrailerIcon(int position) {
        startVideoIntent(position);
    }

    private void startVideoIntent( int position ) {
        Trailers trailer = trailersArrayList.get(position);
        Uri imageVideoLink = Uri.parse(trailer.getYoutubeUrl());

        Intent intent = new Intent(Intent.ACTION_VIEW, imageVideoLink);
        Intent chooserIntent = Intent.createChooser(intent, getString(R.string.chooser_msg));
        startActivity(chooserIntent);

    }
}
