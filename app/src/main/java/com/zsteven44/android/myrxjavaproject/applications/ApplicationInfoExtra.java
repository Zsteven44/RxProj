package com.zsteven44.android.myrxjavaproject.applications;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;

public class ApplicationInfoExtra implements Comparable<Object> {

    private String name;

    private Context context;
    private ResolveInfo resolveInfo;
    private ComponentName componentName;
    private PackageInfo packageInfo = null;
    private Drawable icon = null;

    public ApplicationInfoExtra(@NonNull final Context context,
                                @NonNull final ResolveInfo resolveInfo) {
        this.context = context;
        this.resolveInfo = resolveInfo;
        this.componentName = new ComponentName(
                resolveInfo.activityInfo.applicationInfo.packageName,
                resolveInfo.activityInfo.name);
        try {
            packageInfo = context.getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch(PackageManager.NameNotFoundException e){
            e.printStackTrace();
        }
    }

    public String getPackageName() {
        return resolveInfo.activityInfo.packageName;
    }

    public String getName() {
        if (name != null ) return name;
        try {
            return getNameFromResolveInfo(resolveInfo);
        } catch (PackageManager.NameNotFoundException e) {
            return getPackageName();
        }
    }

    public Context getContext() {
        return context;
    }


    public ResolveInfo getResolveInfo() {
        return resolveInfo;
    }


    public ComponentName getComponentName() {
        return componentName;
    }


    public PackageInfo getPackageInfo() {
        return packageInfo;
    }

    public Drawable getIcon() {
        if (icon == null) {
            icon = getResolveInfo().loadIcon(getContext().getPackageManager());
        }
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    private String getNameFromResolveInfo(ResolveInfo ri) throws PackageManager.NameNotFoundException {
        String name = ri.resolvePackageName;
        if (ri.activityInfo != null){
            Resources res = context
                    .getPackageManager()
                    .getResourcesForApplication(ri.activityInfo.applicationInfo);
            Resources engRes = getEnglishResources(res);
            if (ri.activityInfo.labelRes != 0) {
                name = engRes.getString(ri.activityInfo.labelRes);
                if (name == null || name.equals("")) {
                    name = res.getString(ri.activityInfo.labelRes);
                }
            } else {
                name= ri.activityInfo
                        .applicationInfo
                        .loadLabel(context.getPackageManager()).toString();
            }
        }
        return name;
    }

    private Resources getEnglishResources(Resources resources) {
        AssetManager assets  = resources.getAssets();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        Configuration config = new Configuration(resources.getConfiguration());
//        config.locale = Locale.US;
        return new Resources(assets, metrics, config);

    }

    @SuppressLint("NewApi")
    public long getFirstInstallTime() {
        PackageInfo pi = getPackageInfo();
        if (pi != null
                && android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.GINGERBREAD) {
            return pi.firstInstallTime;
        } else {
            return 0;
        }
    }

    @SuppressLint("NewApi")
    public long getLastUpdateTime() {
        PackageInfo pi = getPackageInfo();
        if (pi != null
                && android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.GINGERBREAD) {
            return pi.lastUpdateTime;
        } else {
            return 0;
        }
    }

    @Override
    public int compareTo(@NonNull Object o) {
        ApplicationInfoExtra f = (ApplicationInfoExtra) o;
        return getName().compareTo(f.getName());
    }
}
