package com.kryptkode.cyberman.popularmovies2.model;

/**
 * Created by Cyberman on 6/14/2017.
 */

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.ArrayList;

public class Movie implements Parcelable {

    //declare the fields
    private String originalTitle;
    private String posterUrl;
    private String overview;
    private String releaseDate;
    private double rating;
    private double voteAverage;
    private int id;
    private int check;
    private boolean favourite;

    public boolean isFavourite() {
        return favourite;
    }

    public void setFavourite(boolean favourite) {
        this.favourite = favourite;
        if (favourite){
            check = 1;
        }
        else {
            check = 0;
        }
    }

    public static final String MOVIE_POSTER_BASE_URL = "http://image.tmdb.org/t/p/"; //image base url
    public static final String POSTER_SIZE_LOW_RES= "w185"; //image size used in MainActivity
    private static final String POSTER_SIZE_HIGH_RES = "w500"; //image size used in DetailActivity higher resolution


    public Movie(String title) { // useful for testing model with dummy data
        originalTitle = title;
    }

    public Movie(String originalTitle, String poster_path, String overview, String releaseDate,
                 double voteAverage, double ratings, int id) {
        this.originalTitle = originalTitle;
        this.posterUrl = poster_path;
        this.overview = overview;
        this.voteAverage = voteAverage;
        this.releaseDate = releaseDate;
        this.rating = ratings;
        this.id = id;
    }


    public Movie(Parcel input) {
        originalTitle = input.readString();
        posterUrl = input.readString();
        overview = input.readString();
        voteAverage = input.readDouble();
        rating = input.readDouble();
        releaseDate = input.readString();
        id = input.readInt();
        check = input.readInt();
        if (check == 1){
            favourite = true;
        }
        else {
            favourite = false;
        }
        Log.e("Save", "parcel movie");

    }

    //the getter methods
    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getPosterPath() {
        return posterUrl;
    }

    public String getOverview() {
        return overview;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public String getReleaseDate() {
        return releaseDate;
    }



    //testing the recycler view with mock data
    public static ArrayList<Movie> testLoad() {
        ArrayList<Movie> movies = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            movies.add(new Movie("The Big Bang Theory" + (i + 1)));
        }
        return movies;
    }

    //method to get the image
    public String getPoster(boolean highResolution) {
        String url = null;
        if (highResolution) {
            url = MOVIE_POSTER_BASE_URL + POSTER_SIZE_HIGH_RES + getPosterPath();
        } else {
            url = MOVIE_POSTER_BASE_URL + POSTER_SIZE_LOW_RES + getPosterPath();
        }
        return url;
    }


    //Parcelbles for savedInstanceState
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(originalTitle);
        dest.writeString(posterUrl);
        dest.writeString(overview);
        dest.writeDouble(voteAverage);
        dest.writeDouble(rating);
        dest.writeString(releaseDate);
        dest.writeInt(id);
        dest.writeInt(check);
        Log.e("Save","write to parcel");
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            Log.e("Save","create from parcel");
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            Log.e("Save","new array");
            return new Movie[size];
        }
    };
}
