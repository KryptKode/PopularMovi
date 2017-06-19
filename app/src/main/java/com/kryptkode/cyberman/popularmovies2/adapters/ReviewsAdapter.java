package com.kryptkode.cyberman.popularmovies2.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kryptkode.cyberman.popularmovies2.R;
import com.kryptkode.cyberman.popularmovies2.model.Reviews;

import java.util.ArrayList;
import java.util.zip.Inflater;

/**
 * Created by Cyberman on 6/18/2017.
 */

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewsViewHolder>{
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<Reviews> reviewsArrayList;
    private boolean showingInDetailActivity;

    public ReviewsAdapter(Context context, ArrayList<Reviews> reviewsArrayList, boolean showingInDetailActivity){
        this.context = context;
        this.reviewsArrayList = reviewsArrayList;
        this.showingInDetailActivity = showingInDetailActivity;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public ReviewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ReviewsViewHolder(inflater.inflate(R.layout.reviews_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ReviewsViewHolder holder, int position) {
        Reviews review = reviewsArrayList.get(position);
        String firstLetterOfAuthorName = review.getAuthor().substring(0,1);
        holder.reviewAvatarTextView.setText(firstLetterOfAuthorName);
        holder.reviewAuthortextView.setText(review.getAuthor());
        if(showingInDetailActivity){
            holder.reviewBodytextView.setMaxLines(5);
            holder.reviewBodytextView.setEllipsize(TextUtils.TruncateAt.END);
        }
        holder.reviewBodytextView.setText(review.getContent());

    }

    @Override
    public int getItemCount() {
        return reviewsArrayList.size();
    }

    class ReviewsViewHolder extends RecyclerView.ViewHolder{
        TextView reviewAvatarTextView;
        TextView reviewAuthortextView;
        TextView reviewBodytextView;
     ReviewsViewHolder(View itemView) {
        super(itemView);
        reviewAvatarTextView = (TextView) itemView.findViewById(R.id.review_logo);
        reviewAuthortextView = (TextView) itemView.findViewById(R.id.review_author);
        reviewBodytextView = (TextView) itemView.findViewById(R.id.review_body);
    }
}
}
