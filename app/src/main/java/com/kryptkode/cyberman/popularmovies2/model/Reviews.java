package com.kryptkode.cyberman.popularmovies2.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Cyberman on 6/18/2017.
 */

public class Reviews implements Parcelable {
    public static final String REVIEWS = "reviews";
    public static final String TITLE =  "title";
    public static final String PAGE = "page";
    private String author;
    private String content;

    public Reviews(String author, String content) {
        this.author = author;
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }



    public static ArrayList<Reviews> generateDummyReviews(int number){
        ArrayList<Reviews> reviewsArrayList = new ArrayList<>();
        for (int i = 0; i < number; i++){
            Reviews review = new Reviews( i %2 == 0? "Gimly" : "Movie Queen41", i %2 == 0? "I'd just like to thank Patty Jenkins for making a DCIThoughtSheWasWithUniverse movie that wasn't fucking garbage.\r\n\r\nIf I'm being completely honest, the two people I went to the cinema to watch _Wonder Woman_ with and I did spend the next two hours after coming out of our screening discussing the various problems with the movie, but we also all agreed on one thing: We still loved it.\r\n\r\nMaybe it's just the rose-coloured glasses of comparison, but I had an excellent time with _Wonder Woman_, and I'm excited to go back to the cinema and watch it, at least one more time.\r\n\r\nIt's the first time I've said that about a DC movie since _The Dark Knight Rises_.\r\n\r\n_Final rating:★★★½ - I strongly recommend you make the time.": "**The First Great DCEU Film**\r\n\r\nThis film is the origin story of Diana Prince/Wonder Woman (Gal Gadot), who was first introduced in Batman v. Superman last year. She is born and trained on Themyscira, the hidden island where the powerful warrior women known as the Amazons live. One day, Steve Trevor (Chris Pine), an American World War I spy, crashes off the coast of Themyscira and is rescued by Diana and the two team up to take down Ares, the God of War, and the Germans, who are developing a very deadly form of mustard gas. There are fantastic action sequences in this film, especially by Gal Gadot. It's amazing to see her single-handedly storm the German front, inspiring the Allies to fight with her. Gadot takes over from the legendary Linda Carter and makes the role her own. She has great chemistry with Pine. They are complete equals in this film. It's refreshing to see the female lead in a superhero film not be the love interest. The only negative part of the film are the lackluster villains. Hopefully, Wonder Woman will have more formidable foes in future films.");
            reviewsArrayList.add(review);
        }
        return reviewsArrayList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.author);
        dest.writeString(this.content);
    }

    protected Reviews(Parcel in) {
        this.author = in.readString();
        this.content = in.readString();
    }

    public static final Creator<Reviews> CREATOR = new Creator<Reviews>() {
        @Override
        public Reviews createFromParcel(Parcel source) {
            return new Reviews(source);
        }

        @Override
        public Reviews[] newArray(int size) {
            return new Reviews[size];
        }
    };
}
