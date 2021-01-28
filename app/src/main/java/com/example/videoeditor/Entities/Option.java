package com.example.videoeditor.Entities;

public class Option {
    private String name;
    private int iconRes;

    public Option(String name, int iconRes) {
        this.name = name;
        this.iconRes = iconRes;
    }

    public String getName() {
        return name;
    }

    public int getIconRes() {
        return iconRes;
    }
}
