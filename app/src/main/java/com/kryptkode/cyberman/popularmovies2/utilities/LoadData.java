package com.kryptkode.cyberman.popularmovies2.utilities;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

/**
 * Created by Cyberman on 6/18/2017.
 */

public class LoadData implements LoaderManager.LoaderCallbacks {

    public interface MoviesLoadindListener{
        void beforeLoading();
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {

    }

    @Override
    public void onLoaderReset(Loader loader) {

    }
}
