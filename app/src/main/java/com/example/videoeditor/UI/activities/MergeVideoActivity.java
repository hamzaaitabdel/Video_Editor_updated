package com.example.videoeditor.UI.activities;

import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.example.videoeditor.Entities.Video;
import com.example.videoeditor.R;
import com.example.videoeditor.VideoApplication;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.lang.reflect.Array;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import VideoHandle.EpEditor;
import VideoHandle.EpVideo;
import VideoHandle.OnEditorListener;

public class MergeVideoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merge_video);
        Intent intent = getIntent();
        ArrayList<Video> videos = intent.getParcelableArrayListExtra("selected");
        ((VideoApplication)getApplication()).executorService.execute(()->{
            mergeVideos(videos);
        });

        /*
        if (intent!=null){
            ArrayList<Video> videos=intent.getParcelableArrayListExtra("videos");
            File folder = new File(Environment.getExternalStorageDirectory() +
                    File.separator + "VideoCut");
            boolean success = true;
            if (!folder.exists()) {
                success = folder.mkdirs();
            }
            mergeVideos(videos,folder.getPath());
        }

         */
    }

    private void mergeVideos(ArrayList<Video> videos) {
        ArrayList<EpVideo> epVideos =  new  ArrayList<>();
        int wbh=0,hbw=0;
        for(Video video:videos){
            epVideos.add(new EpVideo (video.getUri()));
        }
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(videos.get(0).getUri());
        int width = Integer.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH));
        int height = Integer.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT));
        retriever.release();

        File direct = new File(Environment.getExternalStorageDirectory(),getString(R.string.app_name));
        if(!direct.exists()) {
            direct.mkdir();
        }
        File vidDir = new File(direct,"Videos");
        if(!vidDir.exists()){
            vidDir.mkdir();

        }
        String dstFile =new File(vidDir,"Merge"+ Calendar.getInstance().getTime()+".mp4").getAbsolutePath();
        EpEditor.OutputOption outputOption =new EpEditor.OutputOption(dstFile);
        outputOption.setWidth(width);
        outputOption.setHeight(height);
        outputOption.frameRate = 30;
        outputOption.bitRate = 10;
        EpEditor.merge(epVideos, outputOption, new  OnEditorListener() {
            @Override
            public  void  onSuccess () {
                Log.d("Status","Success");
                runOnUiThread(()->{
                    new Handler(Looper.getMainLooper()).postDelayed(()->{
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        Uri uriw = FileProvider.getUriForFile(getApplicationContext(), getPackageName()+".fileprovider", new File(dstFile));
                        intent.setDataAndType(uriw, "video/mp4");
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        startActivity(intent);
                        finish();
                    },1000);

                });
            }

            @Override
            public  void  onFailure () {
                Log.d("Fail",":'(");

            }

            @Override
            public  void  onProgress ( float  progress ) {
                // Get processing progress here
                Log.d("Progress",""+progress);
            }
        });
    }

}