package com.example.videoeditor.Entities;

import java.io.Serializable;

public class Followers implements Serializable {
    private int imgFollowers;
    private String txtFollowers;
    private String txtFollowersHashtags;

    public Followers(int i, String str, String str2) {
        this.imgFollowers = i;
        this.txtFollowers = str;
        this.txtFollowersHashtags = str2;
    }

    public int getImgFollowers() {
        return this.imgFollowers;
    }

    public void setImgFollowers(int i) {
        this.imgFollowers = i;
    }

    public String getTxtFollowers() {
        return this.txtFollowers;
    }

    public void setTxtFollowers(String str) {
        this.txtFollowers = str;
    }

    public String getTxtFollowersHashtags() {
        return this.txtFollowersHashtags;
    }

    public void setTxtFollowersHashtags(String str) {
        this.txtFollowersHashtags = str;
    }
}
