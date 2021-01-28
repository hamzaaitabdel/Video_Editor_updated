package com.example.videoeditor.UI.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import com.example.videoeditor.Entities.Image;
import com.example.videoeditor.R;
import com.example.videoeditor.UI.fragments.ImageFragment;
import com.example.videoeditor.UI.fragments.VideoFragment;
import com.example.videoeditor.Utils.Provider;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.material.tabs.TabLayout;

import static com.example.videoeditor.UI.activities.MainActivity.getAdSize;
import static com.example.videoeditor.Utils.AdUtils.loadBanner;

public class StudioActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private AdView adView;
    public StudioActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_studio);
        init();
        initAds();
        com.example.videoeditor.Adapters.StudioPagerAdapter pagerAdapter = new com.example.videoeditor.Adapters.StudioPagerAdapter(getSupportFragmentManager()) ;
        pagerAdapter.addFragment(new VideoFragment(),"VIDEO");
        pagerAdapter.addFragment(new ImageFragment(),"PHOTO");
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }
    private void init() {
        viewPager = findViewById(R.id.pager);
        tabLayout = findViewById(R.id.studio_tabs);
    }
    private void initAds() {
        //ads
        FrameLayout adContainer = findViewById(R.id.adView_studio);
        loadBanner(this,adContainer);
    }

    public void finishActivity(View view) {
        onBackPressed();
    }

}