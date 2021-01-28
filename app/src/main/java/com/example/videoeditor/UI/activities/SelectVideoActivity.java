package com.example.videoeditor.UI.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.videoeditor.Adapters.VideoRVAdapter;
import com.example.videoeditor.Entities.Video;
import com.example.videoeditor.R;
import com.example.videoeditor.Utils.MediaUtils;
import com.example.videoeditor.VideoApplication;

import java.util.ArrayList;
import java.util.List;

import static com.example.videoeditor.UI.activities.CreateActivity.requestPermission;

public class SelectVideoActivity extends AppCompatActivity {

    private VideoRVAdapter videoRVAdapter;
    private List<Video> videos;
    private RecyclerView recyclerView;
    private TextView emptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_video);
        initViews();
        requestPermission(this);
        videos = new ArrayList<>();
        videoRVAdapter = new VideoRVAdapter(getApplicationContext(), videos, i -> {
            Video selected=videos.get(i);
            Intent intent = new Intent(getBaseContext(), CutVideoActivity.class);
            intent.putExtra("uri", selected.getUri());
            startActivity(intent);


        });
        MediaUtils.getVideosCursor(this,((VideoApplication)getApplication()).executorService,cursor->{
                while (cursor.moveToNext()) {

                    String path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
                    String name = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE));
                    int duration = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION));
                    videos.add(new Video(name, path, duration));
            }
            runOnUiThread(()->{
                videoRVAdapter.swapData(videos);
                if(videos.size()==0){
                    emptyView.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.INVISIBLE);
                }
            });
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,false));
        recyclerView.setAdapter(videoRVAdapter);
    }

    private void initViews() {
        recyclerView = findViewById(R.id.rv_select_video);
        emptyView = findViewById(R.id.empty_view_video);


    }

    public void finishActivity(View view) {
        finish();
    }
}