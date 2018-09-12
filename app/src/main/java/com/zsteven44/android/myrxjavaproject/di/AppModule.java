package com.zsteven44.android.myrxjavaproject.di;

import android.content.Context;
import android.support.annotation.NonNull;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {
    private Context context;

    public AppModule(@NonNull final Context context) {
        this.context = context;
    }

    @Singleton
    @Provides
    @NonNull
    public Context provideContext(){
        return context;
    }
}
