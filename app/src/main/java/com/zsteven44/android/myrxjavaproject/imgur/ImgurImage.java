package com.zsteven44.android.myrxjavaproject.imgur;

public class ImgurImage extends ImgurItem {

    private String type;

    public ImgurImage(String id, String title, String description, String link, int ups, int downs, int views, String type) {
        super(id, title, description, link, ups, downs, views);
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
