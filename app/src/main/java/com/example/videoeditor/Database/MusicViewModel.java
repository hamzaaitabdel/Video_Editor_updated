package com.example.videoeditor.Database;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.videoeditor.Entities.Music;

import java.util.List;

public class MusicViewModel extends AndroidViewModel {
    private MusicDao dao;
    private LiveData<List<Music>> musics;

    public MusicViewModel(@NonNull Application application) {
        super(application);
        DraftDatabase database = DraftDatabase.getDatabase(application);
        dao = database.musicDao();
        musics= dao.getAllMusics();
    }

    public void insertMusic(Music music){
        DraftDatabase.databaseWriteExecutor.execute(()->{
            dao.insert(music);
        });
    }
    public LiveData<List<Music>> getMusics(){
        return musics;
    }
}
