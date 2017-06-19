package com.kryptkode.cyberman.popularmovies2.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.kryptkode.cyberman.popularmovies2.R;
import com.kryptkode.cyberman.popularmovies2.model.Trailers;

import java.util.ArrayList;
import java.util.zip.Inflater;

/**
 * Created by Cyberman on 6/18/2017.
 */

public class TrailersAdapter extends RecyclerView.Adapter<TrailersAdapter.TrailersViewHolder>{
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<Trailers> trailersArrayList;
    private OnClickTrailerIconListener trailerIconListener;

    public TrailersAdapter(Context context, ArrayList<Trailers> trailersArrayList) {
        this.context = context;
        this.trailersArrayList = trailersArrayList;
        this.inflater = LayoutInflater.from(context);
    }


    public interface OnClickTrailerIconListener{
        void onClickTrailerIcon(int position);
    }


    public void setTrailerIconListener(OnClickTrailerIconListener trailerIconListener) {
        this.trailerIconListener = trailerIconListener;
    }

    @Override
    public TrailersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TrailersViewHolder (inflater.inflate(R.layout.trailer_item, parent, false));
    }

    @Override
    public void onBindViewHolder(TrailersViewHolder holder, int position) {
        Trailers trailer = trailersArrayList.get(position);
        if(position % 2 == 0){
            holder.tralerIcon.setImageResource(R.drawable.ic_ondemand_video_red_72dp);
        }
        else {
            holder.tralerIcon.setImageResource(R.drawable.ic_ondemand_video_black_72dp);
        }

        holder.tralerIcon.setTag(R.string.trailer_url_id, trailer.getYoutubeUrl() );
        holder.trailerTitle.setText(trailer.getName());
    }

    @Override
    public int getItemCount() {
        return trailersArrayList.size();
    }



    class TrailersViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView tralerIcon;
        ImageView playIcon;
        TextView trailerTitle;
        TrailersViewHolder(View itemView) {
            super(itemView);
            tralerIcon = (ImageView) itemView.findViewById(R.id.trailer_image_view);
            trailerTitle = (TextView) itemView.findViewById(R.id.trailer_title);
            playIcon = (ImageView) itemView.findViewById(R.id.trailer_image_view_play);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            trailerIconListener.onClickTrailerIcon(getAdapterPosition());
        }
    }

}
