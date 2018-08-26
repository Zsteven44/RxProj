package com.zsteven44.android.myrxjavaproject.applications;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zsteven44.android.myrxjavaproject.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ApplicationAdapter extends RecyclerView.Adapter<ApplicationAdapter.ApplicationViewHolder> {

    private List<ApplicationInfo> applicationList = new ArrayList<>();
    private int rowLayout;

    public ApplicationAdapter(@Nullable final List<ApplicationInfo> applicationList,
                              int rowLayout) {
        this.applicationList = applicationList;
        this.rowLayout = rowLayout;
    }

    public void addApplications(final List<ApplicationInfo> appList) {
        applicationList.clear();
        applicationList.addAll(appList);
        notifyDataSetChanged();

    }

    public void addApplication(int position,
                               final ApplicationInfo appInfo) {
        if (position < 0) position = 0;
        applicationList.add(position, appInfo);
        notifyItemInserted(position);
    }


    @Override
    public ApplicationViewHolder onCreateViewHolder(final ViewGroup parent,
                                                    int viewType) {
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(rowLayout, parent, false);
        return new ApplicationViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ApplicationViewHolder holder, int position) {
        final ApplicationInfo appInfo = applicationList.get(position);
        holder.appTitle.setText(appInfo.getName());
        getBitMap(appInfo.getIcon()).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(holder.appIcon::setImageBitmap);
    }

    @Override
    public int getItemCount() {
        return (applicationList != null) ? applicationList.size() : 0 ;
    }

    private Observable<Bitmap> getBitMap(String icon) {
        return Observable.create(subscriber -> {
            subscriber.onNext(BitmapFactory.decodeFile(icon));
            subscriber.onComplete();
        });
    }

    public static class ApplicationViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.row_app_title)TextView appTitle;
        @BindView(R.id.row_app_icon)ImageView appIcon;

        public ApplicationViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
