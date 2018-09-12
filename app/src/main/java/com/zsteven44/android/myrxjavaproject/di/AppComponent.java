package com.zsteven44.android.myrxjavaproject.di;

import com.zsteven44.android.myrxjavaproject.repository.utils.CachedData;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {

    void inject(CachedData cachedData);

}