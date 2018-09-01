package com.zsteven44.android.myrxjavaproject.imgurfragment.imgur;

import java.util.List;

public class ImgurGalleryList {

    private List<ImgurGallery> data;

    public ImgurGalleryList(List<ImgurGallery> data) {
        this.data = data;
    }

    public List<ImgurGallery> getData() {
        return data;
    }

    public void setData(List<ImgurGallery> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ImgurGalleryList{" +
                "data=" + data +
                '}';
    }
}
