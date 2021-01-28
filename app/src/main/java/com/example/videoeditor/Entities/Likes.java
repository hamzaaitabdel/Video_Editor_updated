package com.example.videoeditor.Entities;

import java.io.Serializable;

public class Likes implements Serializable {
    private int imgLikes;
    private String txtLikes;
    private String txtLikesHashtags;

    public Likes(int i, String str, String str2) {
        this.imgLikes = i;
        this.txtLikes = str;
        this.txtLikesHashtags = str2;
    }

    public int getImgLikes() {
        return this.imgLikes;
    }

    public void setImgLikes(int i) {
        this.imgLikes = i;
    }

    public String getTxtLikes() {
        return this.txtLikes;
    }

    public void setTxtLikes(String str) {
        this.txtLikes = str;
    }

    public String getTxtLikesHashtags() {
        return this.txtLikesHashtags;
    }

    public void setTxtLikesHashtags(String str) {
        this.txtLikesHashtags = str;
    }
}
