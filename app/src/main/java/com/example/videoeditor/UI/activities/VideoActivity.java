package com.example.videoeditor.UI.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;

import com.example.videoeditor.R;

import java.io.File;
import java.io.IOException;

public class VideoActivity extends AppCompatActivity {
    private SurfaceView surfaceView;
    private MediaPlayer mediaPlayer;
    private SurfaceHolder holder;
    private ImageView playBt;
    private Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        surfaceView = findViewById(R.id.video_surface);
        playBt = findViewById(R.id.video_play_bt);
        holder = surfaceView.getHolder();
        Intent intent = getIntent();
        handler =new Handler(getMainLooper());
        String path = intent.getStringExtra("path");
        surfaceView.setOnClickListener(view -> {
            handler.removeCallbacksAndMessages(null);
            if(mediaPlayer.isPlaying()){
                playBt.setImageResource(R.drawable.pause_button);
                handler.postDelayed(()->{
                    playBt.setVisibility(View.GONE);
                },2000);
            }else{
                playBt.setImageResource(R.drawable.play_button);
            }
            playBt.setVisibility(View.VISIBLE);
        });
        playBt.setOnClickListener(view -> {
            handler.removeCallbacksAndMessages(null);
            if(mediaPlayer.isPlaying()){
                mediaPlayer.pause();
                playBt.setImageResource(R.drawable.play_button);
            }else{
                mediaPlayer.start();
                playBt.setImageResource(R.drawable.pause_button);
                handler.postDelayed(()->{
                    playBt.setVisibility(View.GONE);
                },2000);
            }
        });
        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
                mediaPlayer = new MediaPlayer(); 
                mediaPlayer.setDisplay(holder);
                try {
                    mediaPlayer.setDataSource(VideoActivity.this, FileProvider.getUriForFile(getApplicationContext(), getPackageName()+".fileprovider", new File(path)));
                    mediaPlayer.setOnPreparedListener(mediaPlayer -> {
                        setVideoSize();
                        mediaPlayer.start();
                        playBt.setVisibility(View.GONE);
                    });
                    mediaPlayer.setOnCompletionListener(mediaPlayer -> {
                        handler.removeCallbacksAndMessages(null);
                        playBt.setImageResource(R.drawable.play_button);
                        playBt.setVisibility(View.VISIBLE);
                    });
                    mediaPlayer.prepare();


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

            }

            @Override
            public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {

            }
        });
    }
    @Override
    protected void onPause() {
        super.onPause();
        mediaPlayer.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.release();
    }
    private void setVideoSize() {

        // // Get the dimensions of the video
        int videoWidth = mediaPlayer.getVideoWidth();
        int videoHeight = mediaPlayer.getVideoHeight();
        float videoProportion = (float) videoWidth / (float) videoHeight;

        // Get the width of the screen
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        int screenHeight = getResources().getDisplayMetrics().heightPixels;
        float screenProportion = (float) screenWidth / (float) screenHeight;

        // Get the SurfaceView layout parameters
        android.view.ViewGroup.LayoutParams lp = surfaceView.getLayoutParams();
        if (videoProportion > screenProportion) {
            lp.width = screenWidth;
            lp.height = (int) ((float) screenWidth / videoProportion);
        } else {
            lp.width = (int) (videoProportion * (float) screenHeight);
            lp.height = screenHeight;
        }
        // Commit the layout parameters
        surfaceView.setLayoutParams(lp);
    }

    public void finishActivity(View view) {
        onBackPressed();
    }
}