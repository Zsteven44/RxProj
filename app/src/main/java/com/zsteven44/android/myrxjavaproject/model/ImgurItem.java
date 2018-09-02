package com.zsteven44.android.myrxjavaproject.model;

public abstract class ImgurItem {

    private String id;
    private String title;
    private String description;
    private String link;
    private int ups;
    private int downs;
    private int views;

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
