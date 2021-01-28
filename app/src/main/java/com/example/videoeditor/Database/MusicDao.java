package com.example.videoeditor.Database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.videoeditor.Entities.Music;

import java.util.List;

@Dao
public interface MusicDao {
    @Query("SELECT * FROM musics_db")
    LiveData<List<Music>> getAllMusics();

    @Insert
    void insert(Music music);
}
