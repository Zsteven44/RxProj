package com.zsteven44.android.myrxjavaproject.applications;

import android.support.annotation.NonNull;

public class ApplicationInfo implements Comparable<Object> {
    private long lastUpdateTime;
    private String name;
    private String icon;

    public long getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(long lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public ApplicationInfo(final String name,
                           final String icon,
                           long lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
        this.name = name;
        this.icon = icon;
    }

    @Override
    public int compareTo(@NonNull Object other) {
        ApplicationInfo app2 = (ApplicationInfo) other;
        return getName().compareTo(app2.getName());
    }
}
