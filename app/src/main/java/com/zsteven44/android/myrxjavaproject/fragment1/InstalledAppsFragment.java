package com.zsteven44.android.myrxjavaproject.fragment1;

import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zsteven44.android.myrxjavaproject.R;
import com.zsteven44.android.myrxjavaproject.applications.ApplicationAdapter;
import com.zsteven44.android.myrxjavaproject.applications.ApplicationInfo;
import com.zsteven44.android.myrxjavaproject.applications.ApplicationInfoExtra;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observable;

public class InstalledAppsFragment extends Fragment {

    @BindView(R.id.recyclerView)RecyclerView recyclerView;

    private ApplicationAdapter applicationAdapter;
    private Unbinder unbinder;
    private File fileDir;

    public InstalledAppsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_imgur, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        applicationAdapter = new ApplicationAdapter(
                new ArrayList<ApplicationInfo>(),
                R.layout.row_layout_application);
        recyclerView.setAdapter(applicationAdapter);
    }

    public Observable<ApplicationInfo> getApps() {
        return Observable.create(subscriber -> {
            List<ApplicationInfoExtra> apps = new ArrayList<>();
            final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
            mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
            List<ResolveInfo> resolveInfos = getActivity()
                    .getPackageManager()
                    .queryIntentActivities(mainIntent, 0);
            for (ResolveInfo info: resolveInfos) {
                apps.add(new ApplicationInfoExtra(getActivity(), info));
            }


        });

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
