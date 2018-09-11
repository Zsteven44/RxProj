package com.zsteven44.android.myrxjavaproject.repository.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.zsteven44.android.myrxjavaproject.MyRxApplication;
import com.zsteven44.android.myrxjavaproject.R;

public class CachedData {
    private final String SHARED_PREFERNCE_KEY = "PREFERENCE_FILE_KEY";
    private Context context;
    private SharedPreferences sharedPrefs;

    public CachedData(@NonNull final MyRxApplication application) {
        this.context = application;
        this.sharedPrefs = application.getSharedPreferences(SHARED_PREFERNCE_KEY, Context.MODE_PRIVATE);

    }

    public String getCachedSearchTerm() {
        return sharedPrefs.getString(context.getString(R.string.cached_search_term_key), "");
    }
    public String getCachedSearchWindow(){
        return sharedPrefs.getString(context.getString(R.string.cached_search_window_key), "");
    }
    public String getCachedSearchType(){
        return sharedPrefs.getString(context.getString(R.string.cached_search_type_key), "");
    }

    public void setCachedSearchParams(@NonNull final String term,
                                        @NonNull final String window,
                                        @NonNull final String type) {
        sharedPrefs.edit()
                .putString(context.getString(R.string.cached_search_term_key), term)
                .putString(context.getString(R.string.cached_search_window_key), window)
                .putString(context.getString(R.string.cached_search_type_key), type)
                .apply();

    }

}
