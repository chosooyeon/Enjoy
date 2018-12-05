package com.example.sooyeon.graduationproject;

public class Memo {
    public String img;
    public String hashTag;
    public String url;

    public Memo(){

    }

    public Memo(String img, String hashTag, String url) {
        this.img = img;
        this.hashTag = hashTag;
        this.url = url;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getHashTag() {
        return hashTag;
    }

    public void setHashTag(String hashTag) {
        this.hashTag = hashTag;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
