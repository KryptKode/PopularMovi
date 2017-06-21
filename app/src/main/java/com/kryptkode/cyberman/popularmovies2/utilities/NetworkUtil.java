package com.kryptkode.cyberman.popularmovies2.utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

import com.kryptkode.cyberman.popularmovies2.BuildConfig;

import java.net.MalformedURLException;
import java.net.URL;

public abstract class NetworkUtil extends Context {


    //constants declaration
    private final static String BASE_URL = "https://api.themoviedb.org/3/movie";//API base url
    private final static String API_KEY_QUERY_PARAM = "api_key";
    private static final String  API_KEY = BuildConfig.API_KEY;
    public final static String POPULAR = "popular"; //sort by the popularity rating
    public final static String RATING = "top_rated";//sort by the top viewed rating
    public static final String VIDEOS = "videos";
    public static final String REVIEWS = "reviews";


    //method to build the appropriate URL when viewing by RATINGS or POPULARITY
    public static URL buildPopularMoviesURL(String sortParameter){
        Uri builtUri = null;
        switch (sortParameter){
            case POPULAR:
                builtUri = Uri.parse(BASE_URL).buildUpon()
                        .appendPath(POPULAR)
                        .appendQueryParameter(API_KEY_QUERY_PARAM,API_KEY )
                        .build();
                break;
            case RATING:
                builtUri = Uri.parse(BASE_URL).buildUpon()
                        .appendPath(RATING)
                        .appendQueryParameter(API_KEY_QUERY_PARAM,API_KEY )
                        .build();
                break;
        }
        URL url = null;
        try{
            url = new URL (builtUri.toString());
        } catch (MalformedURLException | NullPointerException e){
            e.printStackTrace();
        }
        return url;
    }

    public static String buildReviewsUrl(String id){
        Uri.Builder builtUri = Uri.parse(BASE_URL)
                .buildUpon()
                .appendPath(id)
                .appendPath(REVIEWS)
                .appendQueryParameter(API_KEY_QUERY_PARAM, API_KEY);
        URL url = null;
        try{
            url = new URL (builtUri.toString());
        } catch (MalformedURLException | NullPointerException e){
            e.printStackTrace();
        }
        return url.toString();
    }

    public static String buildTrailersUrl(String id){
        Uri.Builder builtUri = Uri.parse(BASE_URL)
                .buildUpon()
                .appendPath(id)
                .appendPath(VIDEOS)
                .appendQueryParameter(API_KEY_QUERY_PARAM, API_KEY);
        URL url = null;
        try{
            url = new URL (builtUri.toString());
        } catch (MalformedURLException | NullPointerException e){
            e.printStackTrace();
        }
        return url.toString();
    }


    //method to check if there is internet connectivity
    public static boolean isOnline(Context context) {
        ConnectivityManager connMgr = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

}
