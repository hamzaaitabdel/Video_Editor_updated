package com.example.videoeditor.UI.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.videoeditor.R;
import com.example.videoeditor.Utils.Provider;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;

import java.io.File;
import java.io.IOException;

import static com.example.videoeditor.UI.activities.MainActivity.mapAdViewToLayout;

public class ShareActivity extends AppCompatActivity {
    private SurfaceView surfaceView;
    private MediaPlayer mediaPlayer;
    private SurfaceHolder holder;
    private ImageView playBt,thumb;
    private String path="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        initViews();
        Intent intent = getIntent();
        path = intent.getStringExtra("path");
        initNative();
        holder=surfaceView.getHolder();
        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setDisplay(holder);
//                Glide.with(ShareActivity.this)
//                        .load(path)
//                        .thumbnail()
//                        .into(thumb);
                try {
                    mediaPlayer.setDataSource(ShareActivity.this,FileProvider.getUriForFile(getApplicationContext(), getPackageName()+".fileprovider", new File(path)));
                    mediaPlayer.setOnPreparedListener(mediaPlayer -> {
                        setVideoSize();
                        mediaPlayer.start();
                        new Handler(getMainLooper()).postDelayed(()->{
                            mediaPlayer.pause();
                        },100);
                    });
                    mediaPlayer.setOnCompletionListener(mediaPlayer -> {
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

    private void initViews() {
       surfaceView = findViewById(R.id.share_surface);
       playBt = findViewById(R.id.video_play_bt);
       thumb = findViewById(R.id.thumb_im);
    }

    public void finishActivity(View view) {
        onBackPressed();
    }
    private void initNative() {
        //native
        AdLoader adLoader = new AdLoader.Builder(this, Provider.nativeId)
                .forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
                    @Override
                    public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                        UnifiedNativeAdView adView = (UnifiedNativeAdView) getLayoutInflater().inflate(R.layout.native_ad_layout,null);
                        mapAdViewToLayout(unifiedNativeAd,adView);
                        FrameLayout frameLayout = findViewById(R.id.share_native_container);
                        frameLayout.removeAllViews();
                        frameLayout.addView(adView);
                    }
                })
                .withAdListener(new AdListener() {
                    @Override
                    public void onAdFailedToLoad(LoadAdError adError) {
                    }
                })
                .withNativeAdOptions(new NativeAdOptions.Builder()
                        .build())
                .build();
        adLoader.loadAd(new AdRequest.Builder().build());
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

    public void pausePlayVideo(View view) {
        if(mediaPlayer.isPlaying()){
            mediaPlayer.pause();
            playBt.setVisibility(View.VISIBLE);
        }else{
            mediaPlayer.start();
            playBt.setVisibility(View.GONE);
        }
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

    public void onFbShare(View view) {
       shareAs("com.facebook.katana",'v');
    }

    public void onInstaShare(View view) {
        shareAs("com.instagram.android",'v');
    }

    public void onPinterestShare(View view) {
        shareAs("com.pinterest",'v');
    }

    public void onWtspShare(View view) {
        shareAs("com.whatsapp",'v');
    }

    public void onTwitterShare(View view) {
        shareAs("com.twitter.android",'v');
    }

    public void onYtShare(View view) {
        shareAs("com.google.android.youtube",'v');
    }
    public void shareAs(String packageName,char type){
        //type=v --> it's a video
        //type=i --> it's an Image
        try{
            Intent picMessageIntent = new Intent(android.content.Intent.ACTION_SEND);
            if(type=='v'){
                picMessageIntent.setType("video/*");
            }else{
                picMessageIntent.setType("image/*");
            }

            picMessageIntent.setPackage(packageName);
            File pic = new File(path);
            Uri uri = FileProvider.getUriForFile(getApplicationContext(), getPackageName()+".fileprovider", pic);
            picMessageIntent.putExtra(Intent.EXTRA_STREAM, uri);

            startActivity(Intent.createChooser(picMessageIntent, "Share via"));
        }
        catch(Exception c){
            Toast.makeText(ShareActivity.this,c.getMessage(),Toast.LENGTH_LONG).show();
        }
    }
}