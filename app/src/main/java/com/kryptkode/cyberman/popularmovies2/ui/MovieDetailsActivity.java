package com.kryptkode.cyberman.popularmovies2.ui;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bluejamesbond.text.DocumentView;
import com.kryptkode.cyberman.popularmovies2.R;
import com.kryptkode.cyberman.popularmovies2.adapters.ReviewsAdapter;
import com.kryptkode.cyberman.popularmovies2.adapters.TrailersAdapter;
import com.kryptkode.cyberman.popularmovies2.callbacks.ReviewsCallback;
import com.kryptkode.cyberman.popularmovies2.callbacks.TrailersCallBack;
import com.kryptkode.cyberman.popularmovies2.data.MovieContract;
import com.kryptkode.cyberman.popularmovies2.model.Movie;
import com.kryptkode.cyberman.popularmovies2.model.Reviews;
import com.kryptkode.cyberman.popularmovies2.model.Trailers;
import com.kryptkode.cyberman.popularmovies2.utilities.JsonGet;
import com.kryptkode.cyberman.popularmovies2.utilities.JsonParser;
import com.kryptkode.cyberman.popularmovies2.utilities.NetworkUtil;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;

import static com.kryptkode.cyberman.popularmovies2.ui.FavouritesActivity.LOADER_ID;

public class MovieDetailsActivity extends AppCompatActivity implements View.OnClickListener, TrailersAdapter.OnClickTrailerIconListener {

    public static final int TRAILERS_LOADER_ID = 200;
    public static final int REVIEWS_LOADER_ID = 100;
    private static final String MOVIE_REVIEWS = "reviews";
    private static final String MOVIE_TRAILERS = "trailers";

    private FloatingActionButton fab;
    private TextView ratingsTextView;
    private TextView releaseDateTextView;
    private TextView durationTextView;
    private DocumentView documentView;
    private ImageView moviePoster;
    private Button allReviewsButton;
    private Button allTrailersButton;
    private String imageURL;
    private String movieTitle;
    private String movieReleaseDate;
    private String movieOverview;
    private double movieRating;
    private boolean check;
    private int movieID;


    private String trailersUrl;
    private String reviewsUrl;

    private RecyclerView trailersRecyclerView;
    private LinearLayoutManager trailersGridLayoutManager;
    private TrailersAdapter trailersAdapter;
    private ArrayList<Trailers> trailersArrayList;
    private ProgressBar trailersLoadingProgressBar;
    private TextView trailersLoadingTextView;


    private RecyclerView reviewsRecyclerView;
    private LinearLayoutManager reviewsLinearLayoutManager;
    private ReviewsAdapter reviewsAdapter;
    private ArrayList<Reviews> reviewsArrayList;

    private TrailersCallBack trailerCallback;
    private ReviewsCallback reviewsCallback;
    private ProgressBar reviewsLoadingProgressBar;
    private TextView reviewsLoadingTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ratingsTextView = (TextView) findViewById(R.id.detail_rating_text_view);
        //overviewTextView = (TextView) findViewById(R.id.detail_overview);
        releaseDateTextView = (TextView) findViewById(R.id.detail_release_date_text_view);
        documentView = (DocumentView) findViewById(R.id.documentView);
        durationTextView = (TextView) findViewById(R.id.detail_duration_text_view);
        moviePoster = (ImageView) findViewById(R.id.detail_image_view);

        allReviewsButton = (Button) findViewById(R.id.all_reviews_button);
        allTrailersButton = (Button) findViewById(R.id.all_trailers_button);
        allReviewsButton.setOnClickListener(this);
        allTrailersButton.setOnClickListener(this);
        trailersArrayList = new ArrayList<>();
        trailersRecyclerView = (RecyclerView) findViewById(R.id.trailers_recycler_view);
        trailersGridLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        trailersRecyclerView.setLayoutManager(trailersGridLayoutManager);
        trailersAdapter = new TrailersAdapter(this, trailersArrayList);
        trailersAdapter.setTrailerIconListener(this);
        trailersRecyclerView.setAdapter(trailersAdapter);

        trailerCallback = new TrailersCallBack(getApplicationContext());
        trailerCallback.setListener(trailersLoadedListener);

        reviewsCallback = new ReviewsCallback(getApplicationContext());
        reviewsCallback.setListener(reviewsLoadedListener);

        reviewsArrayList = new ArrayList<>();
        reviewsRecyclerView = (RecyclerView) findViewById(R.id.reviews_recycler_view);
        reviewsLinearLayoutManager = new LinearLayoutManager(this);
        reviewsRecyclerView.setLayoutManager(reviewsLinearLayoutManager);
        reviewsAdapter = new ReviewsAdapter(this, reviewsArrayList, true);
        reviewsRecyclerView.setAdapter(reviewsAdapter);


        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);
        Bundle bundle;
        if (getIntent().hasExtra(Movie.MOVIE_FAVOURITE)) {
            bundle = getIntent().getBundleExtra(Movie.MOVIE_FAVOURITE);
        } else {
            bundle = getIntent().getBundleExtra(Movie.BUNDLE);
        }

        if (bundle != null) {
            if (bundle.containsKey(Movie.MOVIE_TITLE)) {
                movieTitle = bundle.getString(Movie.MOVIE_TITLE);
                getSupportActionBar().setTitle(movieTitle);

            }
            if (bundle.containsKey(Movie.MOVIE_OVERVIEW)) {
                movieOverview = bundle.getString(Movie.MOVIE_OVERVIEW);
                //overviewTextView.setText(movieOverview);
                documentView.setText(movieOverview);
            }
            if (bundle.containsKey(Movie.MOVIE_VOTE)) {
                movieRating = bundle.getDouble(Movie.MOVIE_VOTE);
                ratingsTextView.setText(String.valueOf(movieRating));
            }
            if (bundle.containsKey(Movie.POSTER_URL)) {
                imageURL = bundle.getString(Movie.POSTER_URL);
            }
            if (bundle.containsKey(Movie.MOVIE_RELEASE_DATE)) {
                movieReleaseDate = bundle.getString(Movie.MOVIE_RELEASE_DATE);
                releaseDateTextView.setText(movieReleaseDate);
            }
            if (bundle.containsKey(Movie.MOVIE_ID)) {
                movieID = bundle.getInt(Movie.MOVIE_ID);
            }
            if (bundle.containsKey(Movie.MOVIE_FAVOURITE)) {
                check = bundle.getBoolean(Movie.MOVIE_FAVOURITE);
                changeIconStatus(check);
            }
        }

        Picasso.with(getApplicationContext())
                .load(imageURL) //get the high resolution image
                .placeholder(R.drawable.loading)
                .error(R.drawable.no_image)
                .into(moviePoster);


        loadReviewsData();
        loadTrailersData();
    }

    private void loadReviewsData() {
        if (NetworkUtil.isOnline(this)) {
            Bundle queryBundle = new Bundle();
            queryBundle.putString(MOVIE_REVIEWS, NetworkUtil.buildReviewsUrl((String.valueOf(movieID))));
            queryBundle.putInt(Reviews.PAGE, 1);
            LoaderManager loaderManager = getSupportLoaderManager();
            Loader<String> loader = loaderManager.getLoader(REVIEWS_LOADER_ID);
            if (loader == null) {
                loaderManager.initLoader(REVIEWS_LOADER_ID, queryBundle, reviewsCallback);
            } else {
                loaderManager.restartLoader(REVIEWS_LOADER_ID, queryBundle, reviewsCallback);
            }
        }

    }

    private void loadTrailersData() {
        if (NetworkUtil.isOnline(this)) {
            Bundle queryBundle = new Bundle();
            queryBundle.putString(Trailers.TRAILERS, NetworkUtil.buildTrailersUrl(String.valueOf(movieID)));
            queryBundle.putInt(Trailers.PAGE, 1);

            LoaderManager loaderManager = getSupportLoaderManager();
            Loader<String> loader = loaderManager.getLoader(TRAILERS_LOADER_ID);
            if (loader == null) {
                loaderManager.initLoader(TRAILERS_LOADER_ID, queryBundle, trailerCallback);
            } else {
                loaderManager.restartLoader(TRAILERS_LOADER_ID, queryBundle, trailerCallback);
            }
        }

    }


    //nethod to change the icon status
    public void changeIconStatus(boolean status) {
        if (status) {
            fab.setImageResource(R.drawable.ic_star_black_24dp);
        } else {
            fab.setImageResource(R.drawable.ic_star_border_black_24dp);
        }
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.all_reviews_button:
                launchReviewsActivity();
                break;

            case R.id.all_trailers_button:
                launchTrailersActivity();
                break;
            case R.id.fab:

                if (check) {
                    changeIconStatus(false);
                    check = false;
                    Snackbar.make(v, R.string.removed_from_favorites, Snackbar.LENGTH_SHORT).show();
                } else {
                    changeIconStatus(true);
                    check = true;
                    ContentValues values = new ContentValues();
                    values.put(MovieContract.FavouritesEntry._ID, movieID);
                    values.put(MovieContract.FavouritesEntry.FAVOURITES_COLUMN_MOVIE_TITLE, movieTitle);
                    values.put(MovieContract.FavouritesEntry.FAVOURITES_COLUMN_MOVIE_RATING, movieRating);
                    values.put(MovieContract.FavouritesEntry.FAVOURITES_COLUMN_MOVIE_OVERVIEW, movieOverview);
                    values.put(MovieContract.FavouritesEntry.FAVOURITES_COLUMN_MOVIE_POSTER, imageURL);
                    values.put(MovieContract.FavouritesEntry.FAVOURITES_COLUMN_MOVIE_RELEASE_DATE, movieReleaseDate);
                    getContentResolver().insert(MovieContract.FavouritesEntry.CONTENT_URI, values);
                    Snackbar.make(v, R.string.added_to_favorites, Snackbar.LENGTH_SHORT).show();
                }
                break;
        }
    }

    // called when the ALL TRAILERS button is clicked
    private void launchTrailersActivity() {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(Trailers.TRAILERS, trailersArrayList);
        bundle.putString(Trailers.TITLE, movieTitle);
        Intent intent = new Intent(this, TrailersActivity.class);
        intent.putExtra(Trailers.TRAILERS, bundle);
        startActivity(intent);

    }

    // called when the ALL REVIEWS button is clicked
    private void launchReviewsActivity() {

        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(Reviews.REVIEWS, reviewsArrayList);
        bundle.putString(Reviews.TITLE, movieTitle);
        Intent intent = new Intent(this, ReviewsActivity.class);
        intent.putExtra(Reviews.REVIEWS, bundle);
        startActivity(intent);

    }

    @Override
    public void onClickTrailerIcon(int position) {
        startVideoIntent(position);
    }

    private void startVideoIntent(int position) {
        Trailers trailer = trailersArrayList.get(position);
        Uri imageVideoLink = Uri.parse(trailer.getYoutubeUrl());
        Log.e("THUMBNAIL", "URL--> " + trailer.getYoutubeUrl());

        Intent intent = new Intent(Intent.ACTION_VIEW, imageVideoLink);
        Intent chooserIntent = Intent.createChooser(intent, getString(R.string.chooser_msg));
        startActivity(chooserIntent);

    }

    TrailersCallBack.TrailersLoadedListener trailersLoadedListener = new TrailersCallBack.TrailersLoadedListener() {
        @Override
        public void onTrailersLoadStart() {
            showTrailersLoadingIndicators();
        }

        @Override
        public void onTrailersLoaded(String data) {
            parseTrailersData(data);
            trailersAdapter.notifyDataSetChanged();
            hideTrailersLoadingIndicators();
        }


    };

    private void hideTrailersLoadingIndicators() {
        if (trailersLoadingProgressBar != null) {
            trailersLoadingProgressBar.setVisibility(View.INVISIBLE);
        }
        if (trailersLoadingTextView != null) {
            trailersLoadingTextView.setVisibility(View.INVISIBLE);
        }
    }

    private void showTrailersLoadingIndicators() {

        trailersLoadingProgressBar = (ProgressBar) findViewById(R.id.trailers_loading_progress_bar);
        trailersLoadingTextView = (TextView) findViewById(R.id.trailers_loading_text_view);
        trailersLoadingProgressBar.setVisibility(View.VISIBLE);
        trailersLoadingTextView.setVisibility(View.VISIBLE);
    }

    private void parseTrailersData(String data) {
        ArrayList<Trailers> tempArray = JsonParser.parseMovieTrailersJSON(data);
        for (int i = 0; i< tempArray.size(); i++){
            trailersArrayList.add(tempArray.get(i));
        }


    }


    ReviewsCallback.ReviewsLoadedListener reviewsLoadedListener = new ReviewsCallback.ReviewsLoadedListener() {
        @Override
        public void onReviewsLoadStart() {
            showReviewsLoadingIndicator();
        }

        @Override
        public void onReviewsLoaded(String data) {
            parseReviewsData(data);
            reviewsAdapter.notifyDataSetChanged();
            hideReviewsLoadingIndicator();
        }
    };

    private void hideReviewsLoadingIndicator() {
        if (reviewsLoadingProgressBar != null) {
            reviewsLoadingProgressBar.setVisibility(View.INVISIBLE);
        }

        if (reviewsLoadingTextView != null) {
            reviewsLoadingTextView.setVisibility(View.INVISIBLE);
        }

    }

    private void showReviewsLoadingIndicator() {

        reviewsLoadingProgressBar = (ProgressBar) findViewById(R.id.reviews_loading_progress_bar);
        reviewsLoadingTextView = (TextView) findViewById(R.id.reviews_loading_text_view);
        reviewsLoadingProgressBar.setVisibility(View.VISIBLE);
        reviewsLoadingTextView.setVisibility(View.VISIBLE);
    }

    private void parseReviewsData(String data) {

        ArrayList<Reviews> tempArray = JsonParser.parseMovieReviewsJSON(data);
        for (int i = 0; i< tempArray.size(); i++){
            reviewsArrayList.add(tempArray.get(i));
        }

    }
}
