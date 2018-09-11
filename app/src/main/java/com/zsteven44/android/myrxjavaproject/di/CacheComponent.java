package com.zsteven44.android.myrxjavaproject.di;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = CacheModule.class)
public interface CacheComponent {

    void inject(Application application);

}
