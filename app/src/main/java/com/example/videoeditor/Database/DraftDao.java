package com.example.videoeditor.Database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.videoeditor.Entities.Draft;

import java.util.List;

@Dao
public interface DraftDao {

    @Insert
    void saveToDraft(Draft draft);

    @Delete
    void deleteDraft(Draft draft);

    @Update
    void updateDraft(Draft draft);

    @Query("SELECT * FROM draft_db")
    LiveData<List<Draft>> getAllDrafts();

    @Query("DELETE FROM draft_db")
    void deleteAllDrafts();

    @Query("SELECT * FROM draft_db WHERE id=:id")
    Draft getDraft(int id);
}
