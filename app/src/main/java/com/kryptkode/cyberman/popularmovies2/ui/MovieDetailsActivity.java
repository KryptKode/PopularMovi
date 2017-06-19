package com.kryptkode.cyberman.popularmovies2.ui;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.kryptkode.cyberman.popularmovies2.R;
import com.kryptkode.cyberman.popularmovies2.adapters.ReviewsAdapter;
import com.kryptkode.cyberman.popularmovies2.adapters.TrailersAdapter;
import com.kryptkode.cyberman.popularmovies2.data.MovieContract;
import com.kryptkode.cyberman.popularmovies2.model.Movie;
import com.kryptkode.cyberman.popularmovies2.model.Reviews;
import com.kryptkode.cyberman.popularmovies2.model.Trailers;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MovieDetailsActivity extends AppCompatActivity implements View.OnClickListener , TrailersAdapter.OnClickTrailerIconListener{

    private FloatingActionButton fab;
    private TextView ratingsTextView;
    private TextView overviewTextView;
    private TextView releaseDateTextView;
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

    private RecyclerView trailersRecyclerView;
    private LinearLayoutManager trailersGridLayoutManager;
    private TrailersAdapter trailersAdapter;
    private ArrayList<Trailers> trailersArrayList;


    private RecyclerView reviewsRecyclerView;
    private LinearLayoutManager reviewsLinearLayoutManager;
    private ReviewsAdapter reviewsAdapter;
    private ArrayList<Reviews> reviewsArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        ratingsTextView = (TextView) findViewById(R.id.rating);
        overviewTextView = (TextView) findViewById(R.id.overview_content);
        releaseDateTextView = (TextView) findViewById(R.id.release_date);
        moviePoster = (ImageView) findViewById(R.id.detail_image_view);

        allReviewsButton = (Button) findViewById(R.id.all_reviews_button);
        allTrailersButton = (Button) findViewById(R.id.all_trailers_button);
        allReviewsButton.setOnClickListener(this);
        allTrailersButton.setOnClickListener(this);

        trailersArrayList = Trailers.generateDummyTrailers(100);
        trailersRecyclerView = (RecyclerView) findViewById(R.id.trailers_recycler_view);
        trailersGridLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        trailersRecyclerView.setLayoutManager(trailersGridLayoutManager);
        trailersAdapter = new TrailersAdapter(this, trailersArrayList);
        trailersAdapter.setTrailerIconListener(this);
        trailersRecyclerView.setAdapter(trailersAdapter);

        reviewsArrayList = Reviews.generateDummyReviews(100);
        reviewsRecyclerView = (RecyclerView) findViewById(R.id.reviews_recycler_view);
        reviewsLinearLayoutManager = new LinearLayoutManager(this);
        reviewsRecyclerView.setLayoutManager(reviewsLinearLayoutManager);
        reviewsAdapter = new ReviewsAdapter(this, reviewsArrayList, true);
        reviewsRecyclerView.setAdapter(reviewsAdapter);



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
                    ContentValues values = new ContentValues();
                    values.put(MovieContract.FavouritesEntry._ID, movieID);
                    values.put(MovieContract.FavouritesEntry.FAVOURITES_COLUMN_MOVIE_TITLE, movieTitle);
                    values.put(MovieContract.FavouritesEntry.FAVOURITES_COLUMN_MOVIE_RATING, movieRating);
                    values.put(MovieContract.FavouritesEntry.FAVOURITES_COLUMN_MOVIE_OVERVIEW,movieOverview);
                    values.put(MovieContract.FavouritesEntry.FAVOURITES_COLUMN_MOVIE_POSTER, imageURL);
                    values.put(MovieContract.FavouritesEntry.FAVOURITES_COLUMN_MOVIE_RELEASE_DATE, movieReleaseDate);

                    Uri uri = getContentResolver().insert(MovieContract.FavouritesEntry.CONTENT_URI, values);
                    Snackbar.make(view, R.string.added_to_favorites, Snackbar.LENGTH_SHORT).show();
                }
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Bundle bundle;
        if (getIntent().hasExtra(Movie.MOVIE_FAVOURITE)){
            bundle = getIntent().getBundleExtra(Movie.MOVIE_FAVOURITE);
        }
        else{
             bundle = getIntent().getBundleExtra(Movie.BUNDLE);
        }

        if (bundle != null) {
            if (bundle.containsKey(Movie.MOVIE_TITLE)) {
                movieTitle = bundle.getString(Movie.MOVIE_TITLE);
                getSupportActionBar().setTitle(movieTitle);

            }
            if (bundle.containsKey(Movie.MOVIE_OVERVIEW)) {
                movieOverview = bundle.getString(Movie.MOVIE_OVERVIEW);
                overviewTextView.setText(movieOverview);
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
            if (bundle.containsKey(Movie.MOVIE_ID)){
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

    }

    //nethod to change the icon status
    public void changeIconStatus(boolean status){
        if (status){
            fab.setImageResource(R.drawable.ic_star_black_24dp);
        }
        else {
            fab.setImageResource(R.drawable.ic_star_border_black_24dp);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.all_reviews_button:
                launchReviewsActivity();
                break;

            case R.id.all_trailers_button:
                launchTrailersActivity();
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

    private void startVideoIntent( int position ) {
        Trailers trailer = trailersArrayList.get(position);
        Uri imageVideoLink = Uri.parse(trailer.getYoutubeUrl());
        Log.e("THUMBNAIL", "URL--> " + trailer.getYoutubeUrl());

        Intent intent = new Intent(Intent.ACTION_VIEW, imageVideoLink);
        Intent chooserIntent = Intent.createChooser(intent, getString(R.string.chooser_msg));
        startActivity(chooserIntent);

    }
}
