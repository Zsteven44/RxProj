package com.zsteven44.android.myrxjavaproject.di;

import com.zsteven44.android.myrxjavaproject.MyRxApplication;
import com.zsteven44.android.myrxjavaproject.repository.ImgurRepository;
import com.zsteven44.android.myrxjavaproject.ui.activities.BaseActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {

    void inject(MyRxApplication app);
    void injectCachedData(ImgurRepository repo);
    void inject(BaseActivity activity);
}