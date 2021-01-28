package com.example.videoeditor.Entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "draft_db")
public class Draft {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    private String date;

    @NonNull
    private String[] pics;

    private int trans;

    private int frame;

    private int effect;

    private String music;

    private int duration;

    public Draft(@NonNull String date, @NonNull String[] pics, int trans, int frame, int effect, String music, int duration) {
        this.date = date;
        this.pics = pics;
        this.trans = trans;
        this.frame = frame;
        this.effect = effect;
        this.music = music;
        this.duration = duration;
    }

    public int getId() {
        return id;
    }

    @NonNull
    public String getDate() {
        return date;
    }

    @NonNull
    public String[] getPics() {
        return pics;
    }

    public int getTrans() {
        return trans;
    }

    public int getFrame() {
        return frame;
    }

    public int getEffect() {
        return effect;
    }

    public String getMusic() {
        return music;
    }

    public int getDuration() {
        return duration;
    }

    public void setId(int id) {
        this.id = id;
    }
}
