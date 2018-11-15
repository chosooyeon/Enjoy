package com.example.sooyeon.graduationproject;

public class Memo {
    public String title;
    public String url;

    public Memo() {

    }

    public String getmUrl() {
        return mUrl;
    }

    public void setmUrl(String mUrl) {
        this.mUrl = mUrl;
    }

    public String mUrl;

    public Memo(String url){
        this.title = url.length() > 20 ? url.substring(0, 20)+"...":url;
        this.url = url;
        //this.mUrl = title.replace("/", "");
        //this.mUrl = title.replace(".", "");
        //this.mUrl = title.replace(":", "");
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean equals(Object object){
        return object instanceof Memo && url.equals(((Memo)object).getUrl());
    }
}
