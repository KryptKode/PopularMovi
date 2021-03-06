package com.kryptkode.cyberman.popularmovies2.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Cyberman on 6/18/2017.
 */

public class Trailers implements Parcelable {
    public static final String YOUTUBE_BASE_URL = "https://www.youtube.com/watch?v=";
    public static final String  YOUTUBE_THUMBNAIL_BASE_URL = "https://img.youtube.com/vi/";
    public static final int URL = 100;
    public static final String TRAILERS = "trailers" ;
    public static final String TITLE = "title";
    private static final String DEFAULTJPG =  "/default.jpg";
    public static final String PAGE = "page";
    public static final String IS_EMPTY = "empty";
    public static final String MOVIE_ID = "id" ;
    private String type;
    private String name;
    private String key;
    private String site;
    private String youtubeUrl;

    public String getYoutubeThumbnail() {
        return YOUTUBE_THUMBNAIL_BASE_URL + getKey() + DEFAULTJPG;
    }


    public Trailers(String name, String key, String site, String type) {
        this.name = name;
        this.key = key;
        this.site = site;
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getYoutubeUrl() {
        return  YOUTUBE_BASE_URL + getKey();
    }



    public static ArrayList<Trailers> generateDummyTrailers(int number){
        ArrayList<Trailers> trailersArrayList = new ArrayList<>();
        Trailers trailers;
        for (int i = 0; i < number; i++){
            trailers = new Trailers( i%2 == 0?"WonderWoman": "Logan", "5lGoQhFb4NM",i%2 == 0?"Youtube":"TUbemate", i%2 == 0?"Trailer":"Teaser" );
            trailersArrayList.add(trailers);
        }

        return trailersArrayList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.type);
        dest.writeString(this.name);
        dest.writeString(this.key);
        dest.writeString(this.site);
        dest.writeString(this.youtubeUrl);
    }

    protected Trailers(Parcel in) {
        this.type = in.readString();
        this.name = in.readString();
        this.key = in.readString();
        this.site = in.readString();
        this.youtubeUrl = in.readString();
    }

    public static final Parcelable.Creator<Trailers> CREATOR = new Parcelable.Creator<Trailers>() {
        @Override
        public Trailers createFromParcel(Parcel source) {
            return new Trailers(source);
        }

        @Override
        public Trailers[] newArray(int size) {
            return new Trailers[size];
        }
    };
}
