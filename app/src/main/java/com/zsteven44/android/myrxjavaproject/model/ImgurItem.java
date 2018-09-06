package com.zsteven44.android.myrxjavaproject.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public abstract class ImgurItem {

    @PrimaryKey private String id;
    @ColumnInfo(name="title") private String title;
    @ColumnInfo(name="description") private String description;
    @ColumnInfo(name="link") private String link;
    @ColumnInfo(name="ups") private int ups;
    @ColumnInfo(name="downs")private int downs;
    @ColumnInfo(name="views")private int views;

    public ImgurItem(String id, String title, String description, String link, int ups, int downs, int views) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.link = link;
        this.ups = ups;
        this.downs = downs;
        this.views = views;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public int getUps() {
        return ups;
    }

    public void setUps(int ups) {
        this.ups = ups;
    }

    public int getDowns() {
        return downs;
    }

    public void setDowns(int downs) {
        this.downs = downs;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    @Override
    public String toString() {
        return "ImgurItem{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", link='" + link + '\'' +
                ", ups=" + ups +
                ", downs=" + downs +
                ", views=" + views +
                '}';
    }
}
