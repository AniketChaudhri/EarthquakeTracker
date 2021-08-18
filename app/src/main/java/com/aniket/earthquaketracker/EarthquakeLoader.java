package com.aniket.earthquaketracker;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import java.util.List;

public class EarthquakeLoader extends AsyncTaskLoader<List<Earthquake>> {
    private String murl;
    public EarthquakeLoader(@NonNull Context context, String url) {
        super(context);
        murl = url;

    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Nullable
    @Override
    public List<Earthquake> loadInBackground() {
        if (murl == null) return null;
        List<Earthquake> result = QueryUtils.fetchEarthquakeData(murl);
        return result;

    }
}
