package com.zsteven44.android.myrxjavaproject.ui.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.zsteven44.android.myrxjavaproject.R;
import com.zsteven44.android.myrxjavaproject.ui.fragments.ImgurFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.drawer_layout) DrawerLayout drawerLayout;
    @BindView(R.id.frame_layout) FrameLayout frameLayout;
    @BindView(R.id.main_toolbar) Toolbar main_toolbar;
    @BindView(R.id.navigation_view) NavigationView navigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(main_toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        navigationView.setNavigationItemSelectedListener(this);



    }

    @Override
    protected void onStart() {
        super.onStart();
        Timber.d("Activity started.");
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Timber.d("Drawer item selected: %s", item.getTitle());
        switch (item.getItemId()) {
            case R.id.nav_item_1:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frame_layout, new ImgurFragment())
                        .commit();
                main_toolbar.setTitle(R.string.installed_apps_fragment_title);
                break;
            case R.id.nav_item_2:
                getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame_layout, new ImgurFragment())
                    .commit();
                main_toolbar.setTitle(R.string.imgur_fragment_title);
                break;
            default:
                break;
        }
        item.setChecked(true);
        drawerLayout.closeDrawers();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Timber.d("NavigationDrawer button press.");
        switch(item.getItemId()){
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;

        }
        return super.onOptionsItemSelected(item);
    }


}
