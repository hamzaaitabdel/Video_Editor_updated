package com.example.videoeditor.Entities;

public class StudioItem {
    private String path,date,name;

    public StudioItem(String path, String date, String name) {
        this.path = path;
        this.date = date;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public String getDate() {
        return date;
    }
}
