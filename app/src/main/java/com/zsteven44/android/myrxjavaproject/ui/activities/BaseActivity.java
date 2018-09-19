package com.zsteven44.android.myrxjavaproject.ui.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.zsteven44.android.myrxjavaproject.MyRxApplication;

import javax.inject.Inject;

public abstract class BaseActivity extends AppCompatActivity {
    @Inject public SharedPreferences sharedPrefs;
    @Inject public Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MyRxApplication) getApplication()).getAppComponent().inject(this);
    }
}
