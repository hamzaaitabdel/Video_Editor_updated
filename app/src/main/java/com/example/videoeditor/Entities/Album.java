package com.example.videoeditor.Entities;

public class Album {
    private String name;
    private int count;
    private String thumbnail;

    public Album(String name, int count, String thumbnail) {
        this.name = name;
        this.count = count;
        this.thumbnail = thumbnail;
    }

    public String getName() {
        return name;
    }

    public void increaseCount() {
        this.count++;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public String getThumbnail() {
        return thumbnail;
    }
}
