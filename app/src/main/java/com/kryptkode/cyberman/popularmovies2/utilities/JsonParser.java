package com.kryptkode.cyberman.popularmovies2.utilities;

import com.kryptkode.cyberman.popularmovies2.model.Movie;
import com.kryptkode.cyberman.popularmovies2.model.Reviews;
import com.kryptkode.cyberman.popularmovies2.model.Trailers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Cyberman on 6/18/2017.
 */

public class JsonParser {
    public static ArrayList<Movie> parseMoviesJSON(String data){
        ArrayList<Movie>  movieArrayList = new ArrayList<>();
        String poster;
        String title;
        String releaseDate;
        String overview;
        double vote_average;
        int id;
        if (data != null) {
            try {
                JSONObject movie = new JSONObject(data);
                JSONArray results = movie.getJSONArray("results");
                for (int i = 0; i < results.length(); i++) {
                    JSONObject currentJson = results.getJSONObject(i);
                    poster = currentJson.getString("poster_path");
                    title = currentJson.getString("original_title");
                    overview = currentJson.getString("overview");
                    releaseDate = currentJson.getString("release_date");
                    vote_average = currentJson.getDouble("vote_average");
                    id = currentJson.getInt("id");
                     Movie movieObject = new Movie(title, poster, overview, releaseDate, vote_average, id);
                    movieArrayList.add(movieObject);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return movieArrayList;
    }

    public  static ArrayList<Reviews> parseMovieReviewsJSON(String data){
        ArrayList<Reviews> reviewsArrayList = new ArrayList<>();
        String authorName;
        String content;
        if (data!= null){

            try{
                JSONObject review = new JSONObject(data);
                JSONArray results = review.getJSONArray("results");

                for (int i= 0; i < results.length(); i++){
                    JSONObject currentJSON = results.getJSONObject(i);
                    authorName = currentJSON.getString("author");
                    content = currentJSON.getString("content");
                    Reviews reviews = new Reviews(authorName, content);
                    reviewsArrayList.add(reviews);
                }
            }catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return reviewsArrayList;
    }

   public static ArrayList<Trailers> parseMovieTrailersJSON(String data){
       ArrayList<Trailers> trailersArray = new ArrayList<>();
       String name;
       String key;
       String site;
       String type;
       if (data!= null){

           try{
               JSONObject review = new JSONObject(data);
               JSONArray results = review.getJSONArray("results");

               for (int i= 0; i < results.length(); i++){
                   JSONObject currentJSON = results.getJSONObject(i);
                   name = currentJSON.getString("name");
                   key = currentJSON.getString("key");
                   site = currentJSON.getString("site");
                   type = currentJSON.getString("type");
                   Trailers trailers = new Trailers(name, key, site, type);
                   trailersArray.add(trailers);
               }
           }catch (JSONException e) {
               e.printStackTrace();
           }
       }

       return trailersArray;

   }
}
