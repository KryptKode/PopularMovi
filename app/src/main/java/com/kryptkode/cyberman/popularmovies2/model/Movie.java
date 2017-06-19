package com.kryptkode.cyberman.popularmovies2.model;

/**
 * Created by Cyberman on 6/14/2017.
 */

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.ArrayList;

public class Movie implements Parcelable {

    //declare constant fields
    public static final String POSTER_URL = "POSTER_URL";
    public static final String MOVIE_TITLE = "MOVIE_TITLE";
    public static final String MOVIE_RELEASE_DATE = "MOVIE_RELEASE_DATE";
    public static final String MOVIE_VOTE = "MOVIE_VOTE";
    public static final String MOVIE_OVERVIEW = "MOVIE_OVERVIEW";
    public static final String MOVIE_FAVOURITE = "MOVIE_FAVOURITE";
    public static final String BUNDLE = "movie";
    public static final String MOVIE_ID = "id";

    //declare the fields
    private String originalTitle;
    private String posterUrl;
    private String overview;
    private String releaseDate;
    private double ratings;
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


    public Movie() {

    }

    public Movie(String title, String overview, double rating, int id) { // useful for testing model with dummy data
        this.originalTitle = title;
        this.overview = overview;
        this.ratings = rating;
        this.id = id;
    }

    public Movie(String originalTitle, String poster_path, String overview, String releaseDate,
                 double ratings, int id) {
        this.originalTitle = originalTitle;
        this.posterUrl = poster_path;
        this.overview = overview;
        this.ratings = ratings;
        this.releaseDate = releaseDate;
        this.id = id;

    }

    public Movie(Parcel input) {
        originalTitle = input.readString();
        posterUrl = input.readString();
        overview = input.readString();
        ratings = input.readDouble();
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

    public double getRatings() {
        return ratings;
    }

    public int getId() {
        return id;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    //setter methods

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void setRatings(double ratings) {
        this.ratings = ratings;
    }

    public void setId(int id) {
        this.id = id;
    }



    public void setCheck(int check) {
        this.check = check;
    }

    //testing the recycler view with mock data
    public static ArrayList<Movie> testLoad() {
        ArrayList<Movie> movies = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            movies.add(new Movie("The Big Bang Theory" + (i + 1), "Sheldon Cooper has " + i + " Bitches, Oh my damn", (double) i, i));
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
        dest.writeDouble(ratings);
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
