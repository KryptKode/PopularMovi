package com.kryptkode.cyberman.popularmovies2.callbacks;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.util.Log;

import com.kryptkode.cyberman.popularmovies2.model.Reviews;
import com.kryptkode.cyberman.popularmovies2.utilities.JsonGet;

import org.json.JSONObject;

/**
 * Created by Cyberman on 6/21/2017.
 */

public class ReviewsCallback implements LoaderManager.LoaderCallbacks<String> {
    private Context context;
    private String reviewsUrl;
    private int page;
    public static final String TAG = ReviewsCallback.class.getSimpleName();

    private ReviewsCallback.ReviewsLoadedListener listener;

    public interface ReviewsLoadedListener{
        void onReviewsLoadStart();
        void onReviewsLoaded(String data);
    }

    public void setListener(ReviewsCallback.ReviewsLoadedListener listener) {
        this.listener = listener;
    }

    public ReviewsCallback(Context context) {
        this.context = context;
    }
    /*    public void setContext(Context context) {
        this.context = context;
    }*/

    @Override
    public Loader<String> onCreateLoader(int id, Bundle args) {

        reviewsUrl = args.getString(Reviews.REVIEWS);
        page = args.getInt(Reviews.PAGE);
        return new AsyncTaskLoader<String>(context) {
            String JSONData;

            @Override
            protected void onStartLoading() {

                /* If no arguments were passed, we don't have a query to perform. Simply return. */
                if (reviewsUrl == null) {
                    return;
                }
                /*
                 * When we initially begin loading in the background, we want to display the
                 * loading indicator to the user
                 */
                listener.onReviewsLoadStart();

                //  If JSONData is not null, deliver that result. Otherwise, force a load
                /*
                 * If we already have cached results, just deliver them now. If we don't have any
                 * cached results, force a load.
                 */
                if (JSONData != null) {
                    deliverResult(JSONData);
                } else {
                    forceLoad();
                }
            }

            @Override
            public String loadInBackground() {
                String str = null;
                JSONObject jsonObject = JsonGet.getDataFromWeb(reviewsUrl, page);
                Log.e("TRA", "--> REVIEWS URL = " + reviewsUrl);
                if (jsonObject != null)
                    str = jsonObject.toString();
                return str;

            }

            @Override
            public void deliverResult(String data) {
                JSONData = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {

        listener.onReviewsLoaded(data);
        Log.e(TAG, "onLoadFinished: " + data );
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }
}
