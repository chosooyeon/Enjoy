package com.example.a8306_03.enjoy;

public class Url {
    private String picture;
    private String title;
    private String url;

    public Url(String picture, String title, String url) {
        this.picture = picture;
        this.title = title;
        this.url = url;
    }

    public String getPicture() {
        return picture;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public String toString() {
        return "Url{" +
                "picture='" + picture + '\'' +
                ", title='" + title + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
