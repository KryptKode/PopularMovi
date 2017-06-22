package com.kryptkode.cyberman.popularmovies2.ui;

import android.opengl.EGL14;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.kryptkode.cyberman.popularmovies2.R;
import com.kryptkode.cyberman.popularmovies2.adapters.ReviewsAdapter;
import com.kryptkode.cyberman.popularmovies2.callbacks.ReviewsCallback;
import com.kryptkode.cyberman.popularmovies2.model.Reviews;
import com.kryptkode.cyberman.popularmovies2.utilities.EndlessRecyclerViewScrollListener;
import com.kryptkode.cyberman.popularmovies2.utilities.JsonParser;
import com.kryptkode.cyberman.popularmovies2.utilities.NetworkUtil;

import java.util.ArrayList;

public class ReviewsActivity extends AppCompatActivity {

    public static final String TAG = ReviewsActivity.class.getSimpleName();
    public static final int ID = 100;
    private RecyclerView reviewsRecyclerView;
    private LinearLayoutManager reviewsLinearLayoutManager;
    private ReviewsAdapter reviewsAdapter;
    private ArrayList<Reviews> reviewsArrayList;
    private TextView noReviewsTextView;
    private String reviewsUrl;
    private boolean dataHasLoaded;
    private int pageNumber;
    private int movieId;
    EndlessRecyclerViewScrollListener scrollListener;

    private ReviewsCallback reviewsCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        noReviewsTextView = (TextView) findViewById(R.id.reviews_no_reviews_text_view);

        Bundle bundle = getIntent().getBundleExtra(Reviews.REVIEWS);
        reviewsArrayList = bundle.getParcelableArrayList(Reviews.REVIEWS);
        Boolean isEmpty = bundle.getBoolean(Reviews.IS_EMPTY);
        if (reviewsArrayList.isEmpty()){
            pageNumber = 1;
            if(isEmpty){
                noReviewsTextView.setVisibility(View.VISIBLE);
            }
        }
        else{
            pageNumber = 2;
        }
        movieId = bundle.getInt(Reviews.MOVIE_ID);
        getSupportActionBar().setTitle(getString(R.string.reviews_title, bundle.getString(Reviews.TITLE)));
        reviewsRecyclerView = (RecyclerView) findViewById(R.id.reviews_activity_rv);

        reviewsLinearLayoutManager = new LinearLayoutManager(this);

        reviewsUrl = NetworkUtil.buildReviewsUrl(String.valueOf(movieId));

        reviewsCallback  = new ReviewsCallback(getApplicationContext());
        reviewsCallback.setListener(listener);

        reviewsAdapter = new ReviewsAdapter(this, reviewsArrayList, false);
        reviewsRecyclerView.setLayoutManager(reviewsLinearLayoutManager);
        reviewsRecyclerView.setAdapter(reviewsAdapter);

        scrollListener = new EndlessRecyclerViewScrollListener(reviewsLinearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if (dataHasLoaded){
                    pageNumber++;
                    dataHasLoaded = false;
                    Log.e(TAG, "onLoadMore: PAGE NUMBER = " + pageNumber );
                }

                if (NetworkUtil.isOnline(getApplicationContext())){
                    loadMoreReviewsData(pageNumber);

                }
                else{
                    Snackbar.make(findViewById(R.id.reviews_container), R.string.is_not_connected, Snackbar.LENGTH_SHORT).show();

                }
            }
        };

        reviewsRecyclerView.addOnScrollListener(scrollListener);
        loadMoreReviewsData(pageNumber);
    }


    private void loadMoreReviewsData(int pageNumber) {
        Bundle queryBundle = new Bundle();
        queryBundle.putString(Reviews.REVIEWS, reviewsUrl);
        queryBundle.putInt(Reviews.PAGE, pageNumber);

        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<String> loader = loaderManager.getLoader(ID);
        if (loader == null) {
            loaderManager.initLoader(ID, queryBundle, reviewsCallback);
        } else {
            loaderManager.restartLoader(ID, queryBundle, reviewsCallback);
        }
    }


    ReviewsCallback.ReviewsLoadedListener listener = new ReviewsCallback.ReviewsLoadedListener() {
        @Override
        public void onReviewsLoadStart() {

        }

        @Override
        public void onReviewsLoaded(String data) {
            parseReviewsData(data);
            reviewsAdapter.notifyDataSetChanged();
            dataHasLoaded = true;
        }
    };

    private void parseReviewsData(String data) {

        ArrayList<Reviews> tempArray = JsonParser.parseMovieReviewsJSON(data);
        for (int i = 0; i< tempArray.size(); i++){
            reviewsArrayList.add(tempArray.get(i));
        }
    }

}
