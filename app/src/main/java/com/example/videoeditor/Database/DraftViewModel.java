package com.example.videoeditor.Database;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.videoeditor.Entities.Draft;

import java.util.List;

public class DraftViewModel extends AndroidViewModel {
    private DraftDao dao;
    LiveData<List<Draft>> drafts;

    public DraftViewModel(@NonNull Application application) {
        super(application);
        DraftDatabase database = DraftDatabase.getDatabase(application);
        dao=database.draftDao();
        drafts = dao.getAllDrafts();
    }

    public void insert(Draft draft){
        DraftDatabase.databaseWriteExecutor.execute(() -> {
            dao.saveToDraft(draft);
        });
    }

    public void delete(Draft draft){
        DraftDatabase.databaseWriteExecutor.execute(() -> {
            dao.deleteDraft(draft);
        });
    }

    public void update(Draft draft){
        DraftDatabase.databaseWriteExecutor.execute(() -> {
            dao.updateDraft(draft);
        });
    }

    public void deleteAll(){
        DraftDatabase.databaseWriteExecutor.execute(() -> {
            dao.deleteAllDrafts();
        });
    }
    public void getDraft(int id,OnDraftReady onDraftReady){
        DraftDatabase.databaseWriteExecutor.execute(()->{
            Draft draft =dao.getDraft(id);
            onDraftReady.onDraftReady(draft);
        });
    }
    public LiveData<List<Draft>> getDrafts(){
        return drafts;
    }


}
