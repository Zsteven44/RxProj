package com.zsteven44.android.myrxjavaproject.repository.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;

import com.zsteven44.android.myrxjavaproject.MyRxApplication;

import javax.inject.Inject;

public class CachedData {
    private final String SHARED_PREFERNCE_KEY = "PREFERENCE_FILE_KEY";
    private final String CACHED_SEARCH_TERM_KEY = "cached_search_term";
    private final String CACHED_SEARCH_WINDOW_KEY = "cached_search_window";
    private final String CACHED_SEARCH_TYPE_KEY = "cached_search_type";
    private SharedPreferences sharedPrefs;

    @Inject private Context context;

    public CachedData() {
        MyRxApplication.getAppComponent().inject(this);
        this.sharedPrefs = context.getSharedPreferences(SHARED_PREFERNCE_KEY, Context.MODE_PRIVATE);

    }

    public String getCachedSearchTerm() {
        return sharedPrefs.getString(CACHED_SEARCH_TERM_KEY, "");
    }
    public String getCachedSearchWindow(){
        return sharedPrefs.getString(CACHED_SEARCH_WINDOW_KEY, "");
    }
    public String getCachedSearchType(){
        return sharedPrefs.getString(CACHED_SEARCH_TYPE_KEY, "");
    }

    public void setCachedSearchParams(@Nullable final String term,
                                        @Nullable final String window,
                                        @Nullable final String type) {
        SharedPreferences.Editor editor = sharedPrefs.edit();
        if (term != null) editor.putString((CACHED_SEARCH_TERM_KEY), term);
        if (window  != null) editor.putString(CACHED_SEARCH_WINDOW_KEY, window);
        if (type != null) editor.putString(CACHED_SEARCH_TYPE_KEY, type);
        editor.apply();

    }

}
