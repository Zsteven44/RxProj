package com.zsteven44.android.myrxjavaproject.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;

import java.util.List;

@Entity
public class ImgurGallery extends ImgurItem {

    @ColumnInfo(name="cover")
    private String cover;

    @Ignore
    private List<ImgurImage> images;

    @Ignore
    private boolean is_album;

    public ImgurGallery(String id,
                        String title,
                        String description,
                        String link,
                        int ups,
                        int downs,
                        int views,
                        String cover,
                        List<ImgurImage> images,
                        boolean is_album) {
        super(id, title, description, link, ups, downs, views);
        this.cover = cover;
        this.images = images;
        this.is_album = is_album;
    }

    public boolean getIs_Album() {
        return is_album;
    }

    public void setIs_Album(boolean is_album) {
        this.is_album = is_album;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public List<ImgurImage> getImages() {
        return images;
    }

    public void setImages(List<ImgurImage> images) {
        this.images = images;
    }

    @Override
    public String toString() {
        return "ImgurGallery{" +
                "cover='" + cover + '\'' +
                ", images=" + images +
                ", is_album=" + is_album +
                super.toString()+
                '}';
    }
}
