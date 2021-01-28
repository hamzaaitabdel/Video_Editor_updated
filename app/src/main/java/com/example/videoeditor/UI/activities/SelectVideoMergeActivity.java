package com.example.videoeditor.UI.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.videoeditor.Adapters.AlbumsSpinnerAdapter;
import com.example.videoeditor.Adapters.DraftAdapter;
import com.example.videoeditor.Adapters.ImagesRVAdapter;
import com.example.videoeditor.Adapters.MergeSelectedVideoAdapter;
import com.example.videoeditor.Adapters.MergeVideoAdapter;
import com.example.videoeditor.Adapters.SelectedImagesAdapter;
import com.example.videoeditor.Entities.Album;
import com.example.videoeditor.Entities.Image;
import com.example.videoeditor.Entities.Video;
import com.example.videoeditor.R;
import com.example.videoeditor.Utils.MediaUtils;
import com.example.videoeditor.VideoApplication;

import java.util.ArrayList;
import java.util.List;

import static com.example.videoeditor.UI.activities.CreateActivity.requestPermission;

public class SelectVideoMergeActivity extends AppCompatActivity {
    private MergeVideoAdapter adapter;
    private MergeSelectedVideoAdapter selecteAdapter;
    private List<Video> videos,selected;
    private RecyclerView recyclerView,recyclerViewSelected;
    private TextView go;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_video_merge);
        initViews();
        requestPermission(this);
        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SelectVideoMergeActivity.this,MergeVideoActivity.class);
                intent.putParcelableArrayListExtra("selected", (ArrayList<Video>) selected);
                //intent.putParcelableArrayListExtra("selected", (ArrayList<Video>) selected);
                startActivity(intent);
            }
        });
        MediaUtils.getVideosCursor(this,((VideoApplication)getApplication()).executorService, cursor->{
                while (cursor.moveToNext()) {

                    String path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
                    String name = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE));
                    int duration = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION));
                    videos.add(new Video(name, path, duration));
                }
            runOnUiThread(()->{
                if(adapter!=null)
                    adapter.notifyDataSetChanged();
            });

        });
        adapter = new MergeVideoAdapter(getApplicationContext(), videos, new MergeVideoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int i) {
                selected.add(videos.get(i));
                selecteAdapter.notifyItemInserted(selected.size()-1);
                videos.remove(i);
                adapter.notifyItemRemoved(i);
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(adapter);
        selecteAdapter =new MergeSelectedVideoAdapter(getApplicationContext(), selected, new MergeSelectedVideoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int i) {
                videos.add(selected.get(i));
                selected.remove(i);
                selecteAdapter.notifyItemRemoved(i);
                adapter.notifyItemInserted(videos.size()-1);
            }
        });
        recyclerViewSelected.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerViewSelected.setAdapter(selecteAdapter);
    }


    private void initViews() {
    videos = new ArrayList<>();
    selected = new ArrayList<>();
    recyclerView = findViewById(R.id.mergeVideoSelect);
    recyclerViewSelected = findViewById(R.id.mergeVideoSelected);
    go=findViewById(R.id.merge);
    }

    public void finishActivity(View view) {
        finish();
    }

}