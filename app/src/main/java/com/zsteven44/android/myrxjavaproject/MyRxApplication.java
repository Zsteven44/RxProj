package com.zsteven44.android.myrxjavaproject;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;

import com.facebook.stetho.Stetho;
import com.squareup.leakcanary.LeakCanary;
import com.zsteven44.android.myrxjavaproject.di.CacheComponent;

import timber.log.Timber;

public class MyRxApplication extends Application {
    private static Context context;

    private CacheComponent cacheComponent;

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

        cacheComponent = createCacheComponent();

    }

    public static Context getAppContext() {
        return MyRxApplication.context;
    }

    public static String getAppVersion() {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } finally{

            return "1.0";
        }
    }

    public CacheComponent getCacheComponent() {
        return cacheComponent;
    }
    private CacheComponent createCacheComponent() {
        return null;
    }

}
