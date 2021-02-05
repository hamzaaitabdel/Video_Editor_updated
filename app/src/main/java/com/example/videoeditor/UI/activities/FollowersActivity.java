package com.example.videoeditor.UI.activities;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.videoeditor.R;
import com.example.videoeditor.UI.fragments.FollowersFragment;
import com.example.videoeditor.UI.fragments.LikesFragment;
import com.example.videoeditor.Utils.AdUtils;
import com.example.videoeditor.Utils.Provider;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import static com.example.videoeditor.UI.activities.MainActivity.getAdSize;
import static com.example.videoeditor.Utils.AdUtils.loadBanner;

public class FollowersActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    private AdUtils.Inters mInterstitialAd;
    private AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followers);
        initAds();
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavView_Bar);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        loadFragment(new FollowersFragment());
        bottomNavigationView.getMenu().getItem(0).setChecked(true);

    }
    AdUtils.Inters mInterstitialAd2;
    private void initAds() {
        //ads

        FrameLayout adContainer = findViewById(R.id.adMobView);
        loadBanner(this,adContainer);
        mInterstitialAd = new AdUtils.Inters(this,false);

    }

    public void finishActivity(View view) {
        onBackPressed();
    }
    private boolean loadFragment(Fragment fragment) {
        if (fragment == null) {
            return false;
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
        return true;
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment fragment;
        switch (menuItem.getItemId()) {
            case R.id.ic_Follourl:
                fragment = new FollowersFragment();
                break;
            case R.id.ic_Likes:
                fragment = new LikesFragment();
                break;
            default:
                fragment = null;
                break;
        }
        return loadFragment(fragment);
    }
}