package com.zsteven44.android.myrxjavaproject.repository.utils;

import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;

import com.zsteven44.android.myrxjavaproject.MyRxApplication;
import com.zsteven44.android.myrxjavaproject.R;

import javax.inject.Inject;

public class CachedData {
    private final String SHARED_PREFERNCE_KEY = "PREFERENCE_FILE_KEY";
    private SharedPreferences sharedPrefs;

    @Inject private Context context;

    public CachedData() {
        MyRxApplication.getAppComponent().inject(this);
        this.sharedPrefs = context.getSharedPreferences(SHARED_PREFERNCE_KEY, Context.MODE_PRIVATE);

    }

    public LiveData<String> getCachedSearchTerm() {
        return sharedPrefs.getString(context.getString(R.string.cached_search_term_key), "");
    }
    public String getCachedSearchWindow(){
        return sharedPrefs.getString(context.getString(R.string.cached_search_window_key), "");
    }
    public String getCachedSearchType(){
        return sharedPrefs.getString(context.getString(R.string.cached_search_type_key), "");
    }

    public void setCachedSearchParams(@Nullable final String term,
                                        @Nullable final String window,
                                        @Nullable final String type) {
        SharedPreferences.Editor editor = sharedPrefs.edit();
        if (term != null) editor.putString(context.getString(R.string.cached_search_term_key), term);
        if (window  != null) editor.putString(context.getString(R.string.cached_search_window_key), window);
        if (type != null) editor.putString(context.getString(R.string.cached_search_type_key), type);
        editor.apply();

    }

}
