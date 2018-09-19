package com.zsteven44.android.myrxjavaproject;

import android.app.Application;
import android.content.Context;

import com.facebook.stetho.Stetho;
import com.squareup.leakcanary.LeakCanary;
import com.zsteven44.android.myrxjavaproject.di.AppComponent;
import com.zsteven44.android.myrxjavaproject.di.AppModule;
import com.zsteven44.android.myrxjavaproject.di.DaggerAppComponent;

import timber.log.Timber;

public class MyRxApplication extends Application {
    private static Context context;
    private AppComponent appComponent;

    public MyRxApplication() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        MyRxApplication.context = getApplicationContext();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);

        Stetho.initializeWithDefaults(this);

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

        appComponent = buildComponent();
        appComponent.inject(this);
    }

    public AppComponent buildComponent(){
        return DaggerAppComponent
                .builder()
                .appModule(new AppModule(this))
                .build();
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }

    public static Context getAppContext() {
        return MyRxApplication.context;
    }


}
