package com.example.videoeditor.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import com.example.videoeditor.Entities.Draft;
import com.example.videoeditor.Entities.Music;
import com.example.videoeditor.Utils.StringListConverter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Draft.class, Music.class}, version = 2,exportSchema = false)
@TypeConverters(StringListConverter.class)
public abstract class DraftDatabase extends RoomDatabase {

    public abstract DraftDao draftDao();
    public abstract MusicDao musicDao();

    private static volatile DraftDatabase INSTANCE;
    private static final int NUM_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUM_OF_THREADS);

    public static synchronized DraftDatabase getDatabase(Context context) {

        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), DraftDatabase.class, "draft_db")
                    .build();
        }

        return INSTANCE;
    }
}
