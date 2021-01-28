package com.example.videoeditor.UI.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.pm.PackageInfoCompat;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.applovin.sdk.AppLovinPrivacySettings;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.videoeditor.R;
import com.example.videoeditor.Utils.AdUtils;
import com.example.videoeditor.Utils.Provider;
import com.google.ads.consent.ConsentForm;
import com.google.ads.consent.ConsentFormListener;
import com.google.ads.consent.ConsentInfoUpdateListener;
import com.google.ads.consent.ConsentInformation;
import com.google.ads.consent.ConsentStatus;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;
import com.unity3d.ads.metadata.MetaData;
import com.vungle.mediation.VungleConsent;
import com.vungle.warren.Vungle;
import com.willy.ratingbar.ScaleRatingBar;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

public class StartActivity extends AppCompatActivity {
    private Button start;
    private TextView getlikes,privacy;
    private AdUtils.Inters mInterstitialAd;
    private ConsentForm form;
    @Override
    public void onBackPressed() {
        Log.e("","");
        final int SHOW_NOTHING=-1,SHOW_FIRST=1,SHOW_SECOND=2,SHOW_LATER_FIRST=-2,SHOW_LATER_SECOND=-3;
        SharedPreferences sharedPreferences =getSharedPreferences("rate",MODE_PRIVATE);
        int what_to_show;
        if((what_to_show=sharedPreferences.getInt("what_to_show",SHOW_FIRST))>0 || sharedPreferences.getInt("show_after_later",-1)>0){
            final Dialog dialog = new Dialog(StartActivity.this,R.style.NewDialog);
            dialog.requestWindowFeature(1);
            dialog.setCanceledOnTouchOutside(true);
            dialog.setContentView(R.layout.dialog_rate_ios);
            dialog.show();

            TextView msg=(TextView)dialog.findViewById(R.id.txt_title);
            TextView title=(TextView)dialog.findViewById(R.id.txt_name_app);
            msg.setText(sharedPreferences.getString("rate_msg",getString(R.string.how_much_do_you_love_our_app)));
            title.setText(sharedPreferences.getString("rate_title",getString(R.string.app_name)));
            TextView btn_ok=(TextView)dialog.findViewById(R.id.btn_ok);
            TextView btn_not_now=(TextView)dialog.findViewById(R.id.btn_not_now);
            ScaleRatingBar ratingBar = dialog.findViewById(R.id.simpleRatingBar);
            ///////
            TextView donateText=dialog.findViewById(R.id.donate_text);
            donateText.setText(sharedPreferences.getString("donate_txt","Donate"));
            ///////
            LinearLayout btn_donate=(LinearLayout)dialog.findViewById(R.id.btn_donate);
            ImageView donateImg=(ImageView)dialog.findViewById(R.id.img_donate);
            String donate_url =sharedPreferences.getString("donate_url",getString(R.string.default_donate_url));
            Glide.with(this)
                    .load(sharedPreferences.getString("donate_img",""))
                    .placeholder(R.drawable.donate)
                    .into(donateImg);
            Log.e("-----",sharedPreferences.getString("donate_img",""));
            if(sharedPreferences.getBoolean("donate_show",true)){
                btn_donate.setVisibility(View.VISIBLE);
            }else{
                btn_donate.setVisibility(View.GONE);
            }
            btn_donate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //TODO get and go to the link of donating
                    Intent viewIntent =
                            new Intent("android.intent.action.VIEW",
                                    Uri.parse(donate_url));
                    startActivity(viewIntent);
                }
            });
            btn_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    if(ratingBar.getRating()>=sharedPreferences.getInt("rate_min",4)) {
                        int next = (what_to_show==SHOW_FIRST||what_to_show==SHOW_LATER_FIRST)?SHOW_SECOND:SHOW_NOTHING;
                        sharedPreferences.edit().putInt("what_to_show",next).apply();
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        String url =
                                (what_to_show==1||what_to_show==-2)?sharedPreferences.getString("rate_url",getString(R.string.default_rating_url))
                                        :sharedPreferences.getString("rate_url2",getString(R.string.default_rating_url2));
                        if(what_to_show==SHOW_SECOND||what_to_show==SHOW_LATER_SECOND){
                            sharedPreferences.edit()
                                    .putInt("what_to_show",SHOW_NOTHING)
                                    .putInt("show_after_later",0
                                    ).apply();
                        }
                        i.setData(Uri.parse(url));
                        startActivity(i);
                    }
                    finish();
                }
            });
            btn_not_now.setOnClickListener(view -> {
                int wts= sharedPreferences.getInt("what_to_show",1);
                int next;
                if(wts==1||wts==-2){
                    next=SHOW_LATER_FIRST;
                }else if(wts==-1){
                    next=SHOW_NOTHING;
                }else{
                    next=SHOW_LATER_SECOND;
                }
                sharedPreferences.edit()
                        .putInt("what_to_show",next)
                        .putInt("show_after_later",sharedPreferences.getInt("show_after_later",4)-1
                        ).apply();
                finish();
            });
        }else{
            finish();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        initViews();
        Gdpr();
        initAds();
        start.setOnClickListener(view -> {
            startActivity(new Intent(StartActivity.this,MainActivity.class));
                mInterstitialAd.show();

        });
        getlikes.setOnClickListener(view -> {
            startActivity(new Intent(StartActivity.this,FollowersActivity.class));
                mInterstitialAd.show();

        });
        privacy.setOnClickListener(view -> {
            startActivity(new Intent("android.intent.action.VIEW", Uri.parse(getSharedPreferences("privacy",MODE_PRIVATE).getString("privacy",getString(R.string.link_policy)))));
        });

    }

    private void initViews() {
        start= findViewById(R.id.btnStart);
        getlikes = findViewById(R.id.txtLikes);
        privacy = findViewById(R.id.tvPolicy);
    }
    private void initAds() {
        //interstitial

        mInterstitialAd = new AdUtils.Inters(this,false);
    }
    private void Gdpr(){
        //applovin
        AppLovinPrivacySettings.setHasUserConsent(true, getApplicationContext());
        //Facebook
        //Unity
        MetaData gdprMetaData = new MetaData(this);
        gdprMetaData.set("gdpr.consent", true);
        gdprMetaData.commit();
        //Vungle
        VungleConsent.updateConsentStatus(Vungle.Consent.OPTED_IN, "1.0.0");
        //admob
        ConsentInformation consentInformation = ConsentInformation.getInstance(this);
        String[] publisherIds = {getSharedPreferences("privacy",MODE_PRIVATE).getString("pub_id",getString(R.string.pub_id))};
        consentInformation.requestConsentInfoUpdate(publisherIds, new ConsentInfoUpdateListener() {
            @Override
            public void onConsentInfoUpdated(ConsentStatus consentStatus) {
                // User's consent status successfully updated.
            }

            @Override
            public void onFailedToUpdateConsentInfo(String errorDescription) {
                // User's consent status failed to update.
            }
        });
        URL privacyUrl = null;
        try {
            privacyUrl = new URL(getSharedPreferences("privacy",MODE_PRIVATE).getString("gdpr_privacy",getString(R.string.gdpr_policy)));
        } catch (MalformedURLException e) {
            e.printStackTrace();
            // Handle error.
        }
        form = new ConsentForm.Builder(this, privacyUrl)
                .withListener(new ConsentFormListener() {
                    @Override
                    public void onConsentFormLoaded() {
                        // Consent form loaded successfully.
                        form.show();
                    }

                    @Override
                    public void onConsentFormOpened() {
                        // Consent form was displayed.
                    }

                    @Override
                    public void onConsentFormClosed(
                            ConsentStatus consentStatus, Boolean userPrefersAdFree) {
                        // Consent form was closed.
                    }

                    @Override
                    public void onConsentFormError(String errorDescription) {
                        // Consent form error.
                    }
                })
                .withPersonalizedAdsOption()
                .withNonPersonalizedAdsOption()
                .withAdFreeOption()
                .build();
        form.load();
    }

}