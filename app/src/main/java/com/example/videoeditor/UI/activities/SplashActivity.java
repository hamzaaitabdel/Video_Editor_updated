package com.example.videoeditor.UI.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.applovin.sdk.AppLovinPrivacySettings;
import com.example.videoeditor.R;
import com.example.videoeditor.Utils.Provider;
import com.google.ads.consent.ConsentForm;
import com.google.ads.consent.ConsentFormListener;
import com.google.ads.consent.ConsentInfoUpdateListener;
import com.google.ads.consent.ConsentInformation;
import com.google.ads.consent.ConsentStatus;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.unity3d.ads.metadata.MetaData;
import com.vungle.mediation.VungleConsent;
import com.vungle.warren.Vungle;

import java.net.MalformedURLException;
import java.net.URL;


public class SplashActivity extends AppCompatActivity {
    private static int splash_time_out=3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        if(requestPermission()) {
            new Handler(getMainLooper()).postDelayed(() -> {
                Intent SplachIntent = new Intent(SplashActivity.this, StartActivity.class);
                startActivity(SplachIntent);
                finish();
            }, splash_time_out);
        }
    }
    private boolean requestPermission() {
            if (ContextCompat.checkSelfPermission(SplashActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(SplashActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(SplashActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 121);
                return false;
            }
            return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==121){
            if(!(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)){
                finishAffinity();
            }else{
                Intent SplachIntent = new Intent(SplashActivity.this, StartActivity.class);
                startActivity(SplachIntent);
                finish();
            }
        }
    }


}