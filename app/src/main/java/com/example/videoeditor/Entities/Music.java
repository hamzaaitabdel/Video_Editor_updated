package com.example.videoeditor.Entities;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "musics_db")
public class Music {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name,path;
    private int duration;

    public Music(String name, String path, int duration) {
        this.name = name;
        this.path = path;
        this.duration = duration;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public int getDuration() {
        return duration;
    }

    @Ignore
    private String res;
    @Ignore
    public String getRes() {
        return res;
    }
    @Ignore
    public void setRes(String res) {
        this.res = res;
    }
}
