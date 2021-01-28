package com.example.videoeditor.UI.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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

import static com.example.videoeditor.UI.activities.MainActivity.mapAdViewToLayout;
import static com.example.videoeditor.Utils.AdUtils.loadNative;

public class ShareImageActivity extends AppCompatActivity {
    private ImageView imageView;
    private String imgPath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_image);
        initNative();
        Intent intent = getIntent();
        imgPath = intent.getStringExtra("img_path");
        imageView = findViewById(R.id.preview_img);
        Glide.with(this)
                .load(imgPath)
                .into(imageView);
        imageView.setOnClickListener(view -> {
            Intent intentt = new Intent(Intent.ACTION_VIEW);
            File itemSharew = new File(imgPath);
            Uri uriw = FileProvider.getUriForFile(getApplicationContext(), getPackageName()+".fileprovider", itemSharew);
            intentt.setDataAndType(uriw, "image/*");
            intentt.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(intentt);
        });

    }
    private void initNative() {
        //native
        FrameLayout frameLayout=findViewById(R.id.native_container);
        loadNative(this,frameLayout);
    }

    public void finishActivity(View view) {
        onBackPressed();
    }

    public void onFbShare(View view) {
        shareAs("com.facebook.katana",'i');
    }

    public void onInstaShare(View view) {
        shareAs("com.instagram.android",'i');
    }

    public void onPinterestShare(View view) {
        shareAs("com.pinterest",'i');
    }

    public void onWtspShare(View view) {
        shareAs("com.whatsapp",'i');
    }

    public void onTwitterShare(View view) {
        shareAs("com.twitter.android",'i');
    }
    public void onShareOnOther(View view) { shareAs('i');}
    public void onYtShare(View view) {
        shareAs("com.google.android.youtube",'i');
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
            File pic = new File(imgPath);
            Uri uri = FileProvider.getUriForFile(getApplicationContext(), getPackageName()+".fileprovider", pic);
            picMessageIntent.putExtra(Intent.EXTRA_STREAM, uri);

            startActivity(Intent.createChooser(picMessageIntent, "Share via"));
        }
        catch(Exception c){
            Toast.makeText(ShareImageActivity.this,c.getMessage(),Toast.LENGTH_LONG).show();
        }
    }
    public void shareAs(char type){
        //type=v --> it's a video
        //type=i --> it's an Image
        try{
            Intent picMessageIntent = new Intent(android.content.Intent.ACTION_SEND);
            if(type=='v'){
                picMessageIntent.setType("video/*");
            }else{
                picMessageIntent.setType("image/*");
            }

            File pic = new File(imgPath);
            Uri uri = FileProvider.getUriForFile(getApplicationContext(), getPackageName()+".fileprovider", pic);
            picMessageIntent.putExtra(Intent.EXTRA_STREAM, uri);

            startActivity(Intent.createChooser(picMessageIntent, "Share via"));
        }
        catch(Exception c){
            Toast.makeText(ShareImageActivity.this,c.getMessage(),Toast.LENGTH_LONG).show();
        }
    }
}