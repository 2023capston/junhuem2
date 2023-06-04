package com.example.yolijoli;

import com.google.firebase.Timestamp;

public class coInfo {
    String coTitle;
    String coContent;
    Timestamp timest;
    static String imgUrl;

    public coInfo() {
    }

    public String getCoTitle() {
        return coTitle;
    }

    public void setCoTitle(String coTitle) {
        this.coTitle = coTitle;
    }

    public String getCoContent() {
        return coContent;
    }

    public void setCoContent(String coContent) {
        this.coContent = coContent;
    }

    public Timestamp getTimest() {
        return timest;
    }

    public void setTimest(Timestamp timest) {
        this.timest = timest;
    }

    public coInfo(String imgUrl){
        this.imgUrl = imgUrl;
    }

    public static String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
