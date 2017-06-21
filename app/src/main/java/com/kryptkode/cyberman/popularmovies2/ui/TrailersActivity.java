package com.kryptkode.cyberman.popularmovies2.ui;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.kryptkode.cyberman.popularmovies2.R;
import com.kryptkode.cyberman.popularmovies2.adapters.TrailersAdapter;
import com.kryptkode.cyberman.popularmovies2.callbacks.TrailersCallBack;
import com.kryptkode.cyberman.popularmovies2.model.Trailers;
import com.kryptkode.cyberman.popularmovies2.utilities.EndlessRecyclerViewScrollListener;
import com.kryptkode.cyberman.popularmovies2.utilities.JsonParser;
import com.kryptkode.cyberman.popularmovies2.utilities.NetworkUtil;

import java.util.ArrayList;

public class TrailersActivity extends AppCompatActivity implements TrailersAdapter.OnClickTrailerIconListener {

    public static final String TAG = TrailersActivity.class.getSimpleName();
    public static final int ID = 100;

    private RecyclerView trailersRecyclerView;
    private GridLayoutManager trailersGridLayoutManager;
    private TrailersAdapter trailersAdapter;
    private ArrayList<Trailers> trailersArrayList;

    private TextView noTrailersTextView;
    private String trailersUrl;
    private boolean dataHasLoaded;
    private int pageNumber;
    private int movieId;
    EndlessRecyclerViewScrollListener scrollListener;

    private TrailersCallBack trailersCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getBundleExtra(Trailers.TRAILERS);
        trailersArrayList = bundle.getParcelableArrayList(Trailers.TRAILERS);
        Boolean isEmpty = bundle.getBoolean(Trailers.IS_EMPTY);
        if (trailersArrayList.isEmpty()){
            pageNumber = 1;
            if(isEmpty){
                noTrailersTextView = (TextView) findViewById(R.id.trailers_no_trailers_text_view);
                noTrailersTextView.setVisibility(View.VISIBLE);
            }
        }
        else{
            pageNumber = 2;
        }
        movieId = bundle.getInt(Trailers.MOVIE_ID);
        getSupportActionBar().setTitle(getString(R.string.reviews_title, bundle.getString(Trailers.TITLE)));
        trailersRecyclerView = (RecyclerView) findViewById(R.id.reviews_activity_rv);


        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            trailersGridLayoutManager = new GridLayoutManager(this, 2);
        }
        else{
            trailersGridLayoutManager = new GridLayoutManager(this, 3 );
        }

        trailersUrl = NetworkUtil.buildTrailersUrl(String.valueOf(movieId));

        trailersCallback  = new TrailersCallBack(getApplicationContext());
        trailersCallback.setListener(listener);

        trailersAdapter = new TrailersAdapter(this, trailersArrayList);
        trailersRecyclerView.setLayoutManager(trailersGridLayoutManager);
        trailersRecyclerView.setAdapter(trailersAdapter);

        scrollListener = new EndlessRecyclerViewScrollListener(trailersGridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if (dataHasLoaded){
                    pageNumber++;
                    dataHasLoaded = false;
                    Log.e(TAG, "onLoadMore: PAGE NUMBER = " + pageNumber );
                }

                if (NetworkUtil.isOnline(getApplicationContext())){
                    loadMoreTrailersData(pageNumber);

                }
                else{
                    Snackbar.make(findViewById(R.id.trailers_container), R.string.is_not_connected, Snackbar.LENGTH_SHORT).show();

                }
            }
        };

        trailersRecyclerView.addOnScrollListener(scrollListener);
        loadMoreTrailersData(pageNumber);
    }


    private void loadMoreTrailersData(int pageNumber) {
        Bundle queryBundle = new Bundle();
        queryBundle.putString(Trailers.TRAILERS, trailersUrl);
        queryBundle.putInt(Trailers.PAGE, pageNumber);

        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<String> loader = loaderManager.getLoader(ID);
        if (loader == null) {
            loaderManager.initLoader(ID, queryBundle, trailersCallback);
        } else {
            loaderManager.restartLoader(ID, queryBundle, trailersCallback);
        }
    }


    TrailersCallBack.TrailersLoadedListener listener = new TrailersCallBack.TrailersLoadedListener() {
        @Override
        public void onTrailersLoadStart() {

        }

        @Override
        public void onTrailersLoaded(String data) {
            parseTrailersData(data);
            trailersAdapter.notifyDataSetChanged();
            dataHasLoaded = true;
        }
    };

    private void parseTrailersData(String data) {

        ArrayList<Trailers> tempArray = JsonParser.parseMovieTrailersJSON(data);
        for (int i = 0; i< tempArray.size(); i++){
            trailersArrayList.add(tempArray.get(i));
        }
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
