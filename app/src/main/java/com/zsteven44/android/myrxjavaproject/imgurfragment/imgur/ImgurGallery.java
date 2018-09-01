package com.zsteven44.android.myrxjavaproject.imgurfragment.imgur;

import java.util.List;

public class ImgurGallery extends ImgurItem {

    private String cover;
    private List<ImgurImage> images;
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
                        boolean isAlbum) {
        super(id, title, description, link, ups, downs, views);
        this.cover = cover;
        this.images = images;
        this.is_album = isAlbum;
    }

    public boolean getIsAlbum() {
        return is_album;
    }

    public void setIsAlbum(boolean is_album) {
        this.is_album = is_album;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public List<ImgurImage> getImageList() {
        return images;
    }

    public void setImageList(List<ImgurImage> images) {
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