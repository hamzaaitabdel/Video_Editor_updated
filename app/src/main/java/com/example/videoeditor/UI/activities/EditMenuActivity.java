package com.example.videoeditor.UI.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.videoeditor.R;
import com.example.videoeditor.Utils.AdUtils;
import com.example.videoeditor.Utils.Provider;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;

import static com.example.videoeditor.UI.activities.MainActivity.mapAdViewToLayout;
import static com.example.videoeditor.Utils.AdUtils.loadNative;

public class EditMenuActivity extends AppCompatActivity {
    private LinearLayout cutVideo, mergeVideo, moreApps;
    private AdUtils.Inters mInterstitialAd;
    public EditMenuActivity() {
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_menu);
        init();
        initAds();
        moreApps.setOnClickListener(view -> {
            Intent viewIntent =
                    new Intent("android.intent.action.VIEW",
                            Uri.parse(getSharedPreferences("more_link", MODE_PRIVATE).getString("link", "http://www.google.com")));
            startActivity(viewIntent);
        });
        cutVideo.setOnClickListener(view -> {
            Intent intent = new Intent(EditMenuActivity.this, SelectVideoActivity.class);
            startActivity(intent);
                mInterstitialAd.show();
        });
        mergeVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditMenuActivity.this, SelectVideoMergeActivity.class);
                startActivity(intent);
                    mInterstitialAd.show();
            }
        });
    }

    private void initAds() {
        //interstitial
        mInterstitialAd = new AdUtils.Inters(this,false);
        //native
        FrameLayout frameLayout=findViewById(R.id.native_edit_container);
        loadNative(this,frameLayout);
    }

    private void init() {
        cutVideo = findViewById(R.id.cutVideoSelect);
        mergeVideo = findViewById(R.id.mergeVideoSelect);
        moreApps = findViewById(R.id.moreApssSelect);
    }

    public void finishActivity(View v) {
        onBackPressed();
    }
}
