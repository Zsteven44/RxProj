package com.zsteven44.android.myrxjavaproject.di;

import android.content.Context;
import android.content.SharedPreferences;

import com.zsteven44.android.myrxjavaproject.MyRxApplication;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {
    private final String SHARED_PREFERENCE_KEY = "PREFERENCE_FILE_KEY";
    private final MyRxApplication application;

    public AppModule(MyRxApplication application) {
        this.application = application;
    }

    @Provides
    @Singleton
    public Context provideApplicationContext(){
        return application;
    }

    @Provides
    @Singleton
    public SharedPreferences provideSharedPreferences() {
        return application
                .getSharedPreferences(
                        SHARED_PREFERENCE_KEY,
                        Context.MODE_PRIVATE);
    }
}
