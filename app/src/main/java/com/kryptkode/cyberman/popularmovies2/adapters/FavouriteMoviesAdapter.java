package com.kryptkode.cyberman.popularmovies2.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kryptkode.cyberman.popularmovies2.R;
import com.kryptkode.cyberman.popularmovies2.data.MovieContract;
import com.kryptkode.cyberman.popularmovies2.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.zip.Inflater;

/**
 * Created by Cyberman on 6/14/2017.
 */



public class FavouriteMoviesAdapter extends RecyclerView.Adapter<FavouriteMoviesAdapter.FavouriteMovieViewHolder>{

    private Cursor cursor;
    private LayoutInflater inflater;
    private Context context;
    private ItemClickListener clickListener;

    public interface ItemClickListener{
        void onItemClick(int position);
    }

    public void setClickListener(ItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public FavouriteMoviesAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public FavouriteMovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FavouriteMovieViewHolder(inflater.inflate(R.layout.movie_favorites_item, parent, false));
    }

    @Override
    public void onBindViewHolder(FavouriteMovieViewHolder holder, int position) {
        ImageView moviePoster = holder.favoritesMoviePoster;
        TextView movieTitle = holder.favoritesMovieTitle;
        TextView movieRating = holder.favoritesMovieRating;

        Movie movie = getFavouriteMoviesDataFromCursor(cursor, position);
        holder.itemView.setTag(movie.getId());
        movieTitle.setText(movie.getOriginalTitle());
        movieRating.setText(String.valueOf(movie.getRatings()));

        // load the movie poster into the imageView
        Picasso.with(context)
                .load(movie.getPosterPath()) //get the low resolution image
                .placeholder(R.drawable.loading)
                .error(R.drawable.no_image)
                .into(moviePoster);
    }

    @Override
    public int getItemCount() {
        if (cursor == null){
            return  0;
        }
        return cursor.getCount();
    }

    public Cursor swapCursor(Cursor c) {
        // check if this cursor is the same as the previous cursor (mCursor)
        if (cursor == c) {
            return null; // bc nothing has changed
        }
        Cursor temp = cursor;
        this.cursor = c; // new cursor value assigned

        //check if this is a valid cursor, then update the cursor
        if (c != null) {
            this.notifyDataSetChanged();
        }
        return temp;
    }

    public Movie getFavouriteMoviesDataFromCursor(Cursor cursor, int position){
        Movie favouriteMovie = new Movie();
        cursor.moveToPosition(position);

        //set the movie's title
        favouriteMovie.setOriginalTitle(cursor
                .getString(cursor.getColumnIndex(MovieContract.FavouritesEntry.FAVOURITES_COLUMN_MOVIE_TITLE)));

        //set the movie's poster path
        favouriteMovie.setPosterUrl(cursor
                .getString(cursor.getColumnIndex(MovieContract.FavouritesEntry.FAVOURITES_COLUMN_MOVIE_POSTER)));

        //set the movie's release date
        favouriteMovie.setReleaseDate(cursor
                .getString(cursor.getColumnIndex(MovieContract.FavouritesEntry.FAVOURITES_COLUMN_MOVIE_RELEASE_DATE)));

        //set the movie's overview
        favouriteMovie.setOverview(cursor
                .getString(cursor.getColumnIndex(MovieContract.FavouritesEntry.FAVOURITES_COLUMN_MOVIE_OVERVIEW)));

        //set the movie's rating
        favouriteMovie.setRatings(cursor
                .getDouble(cursor.getColumnIndex(MovieContract.FavouritesEntry.FAVOURITES_COLUMN_MOVIE_RATING)));

        //set the id
        favouriteMovie.setId(cursor
                .getInt(cursor.getColumnIndex(MovieContract.FavouritesEntry._ID)));

        return favouriteMovie;
    }

    class FavouriteMovieViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener{

        private ImageView favoritesMoviePoster;
        private TextView favoritesMovieTitle;
        private TextView favoritesMovieRating;
        private View favouritesContainer;
        FavouriteMovieViewHolder(View itemView) {
            super(itemView);
            favoritesMoviePoster = (ImageView) itemView.findViewById(R.id.favourites_movie_poster);
            favoritesMovieRating = (TextView) itemView.findViewById(R.id.favourites_movie_ratings);
            favoritesMovieTitle = (TextView) itemView.findViewById(R.id.favorites_movie_title);
            favouritesContainer = itemView.findViewById(R.id.favourites_container);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(getAdapterPosition());
        }
    }
}
