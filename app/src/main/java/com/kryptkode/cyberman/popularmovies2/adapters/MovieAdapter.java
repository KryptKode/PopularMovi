package com.kryptkode.cyberman.popularmovies2.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kryptkode.cyberman.popularmovies2.R;
import com.kryptkode.cyberman.popularmovies2.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Cyberman on 6/14/2017.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder>{

    //declare the constants
    private static final String POSTER = "POSTER";
    private static final String MOVIE_TITLE = "MOVIE_TITLE";
    private static final String MOVIE_RELEASE_DATE = "MOVIE_RELEASE_DATE";
    private static final String MOVIE_VOTE = "MOVIE_VOTE";
    private static final String MOVIE_OVERVIEW = "MOVIE_OVERVIEW";

    private Context context;
    private ArrayList<Movie> moviesList;
    private LayoutInflater inflater;
    private OnItemClickListener listener;


    public interface OnItemClickListener{
        void onContainerClicked(int id);
        void onFavouritesIconClicked(int id);
    }

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    //movie adapter constructor to instantiate the inflater and array list of movies
    public MovieAdapter(Context context, ArrayList<Movie> moviesList) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.moviesList = moviesList;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.movie_item, parent, false);

        return new MovieViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        Movie movie = moviesList.get(position);
        ImageView poster = holder.moviePoster;
        TextView title = holder.movieTitleTextView;
        ImageView favouriteIcon = holder.favoritesStar;

        //set the title text view to the title of the movie
        title.setText(movie.getOriginalTitle());

        if (movie.isFavourite()){
            favouriteIcon.setImageResource(R.drawable.ic_star_black_24dp);
        }
        else {
            favouriteIcon.setImageResource(R.drawable.ic_star_border_black_24dp);
        }

        // set the movie poster content description to include the title of the movie
            String movieContentDescription = context.getString(R.string.movie_poster_content_description, movie.getOriginalTitle());
            poster.setContentDescription(movieContentDescription);

        // load the movie poster into the imageView
        Picasso.with(context)
                .load(movie.getPoster(false)) //get the low resolution image
                .placeholder(R.drawable.loading)
                .error(R.drawable.no_image)
                .into(poster);
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }

    public class MovieViewHolder extends  RecyclerView.ViewHolder implements  View.OnClickListener{

        private ImageView moviePoster;
        private TextView movieTitleTextView;
        private ImageView favoritesStar;
        private View itemRoot;

        public MovieViewHolder(View itemView) {
            super(itemView);
            itemRoot = itemView.findViewById(R.id.item_root);
            moviePoster = (ImageView) itemView.findViewById(R.id.movie_poster);
            movieTitleTextView = (TextView) itemView.findViewById(R.id.movie_title_text_view);
            favoritesStar = (ImageView) itemView.findViewById(R.id.movie_favorite_star);
            itemView.setOnClickListener(this);
            favoritesStar.setOnClickListener(this);




        }


        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.item_root:
                    listener.onContainerClicked(getAdapterPosition());
                    break;
                case R.id.movie_favorite_star:
                    listener.onFavouritesIconClicked(getAdapterPosition());
                    break;
            }
        }
    }




}
