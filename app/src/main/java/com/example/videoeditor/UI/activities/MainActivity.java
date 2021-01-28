package com.example.videoeditor.UI.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.drawable.DrawableCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.content.pm.PackageManager;

import com.applovin.sdk.AppLovinPrivacySettings;
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
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.formats.MediaView;
import com.google.android.gms.ads.formats.NativeAd;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;
import com.unity3d.ads.metadata.MetaData;
import com.vungle.mediation.VungleConsent;
import com.vungle.warren.Vungle;
import androidx.core.content.pm.PackageInfoCompat;

import java.net.MalformedURLException;
import java.net.URL;

import static com.example.videoeditor.UI.activities.CreateActivity.requestPermission;
import static com.example.videoeditor.Utils.AdUtils.loadBanner;
import static com.example.videoeditor.Utils.AdUtils.loadNative;

public class MainActivity extends AppCompatActivity {
    private ImageButton create, editVid, editPhoto, studio;
    private AdUtils.Inters mInterstitialAd;
    private AdView adView;
    private ConsentForm form;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        requestPermission(this);
        setViewsClickListener();
//        Gdpr();
        initAds();
    }

    private void initAds() {
        FrameLayout adContainer = findViewById(R.id.adView);
        loadBanner(this,adContainer);
        mInterstitialAd = new AdUtils.Inters(this,false);

        //native
       FrameLayout frameLayout=findViewById(R.id.adView);
       loadNative(this,frameLayout);
    }
    public static AdSize getAdSize(Context context) {

        float widthPixels = context.getResources().getDisplayMetrics().widthPixels;
        float density = context.getResources().getDisplayMetrics().density;

        int adWidth = (int) (widthPixels / density);
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(context, adWidth);
    }
    private void setViewsClickListener() {
        create.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this,CreateActivity.class));
                mInterstitialAd.show();

        });
        editPhoto.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this,CreateActivity.class);
            intent.putExtra("dir","PHOTO_EDIT");
            startActivity(intent);
                mInterstitialAd.show();

        });
        editVid.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this,EditMenuActivity.class));
            });
        studio.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, StudioActivity.class));
                mInterstitialAd.show();

        });
    }

    private void initViews() {
        create = findViewById(R.id.create_bt);
        editVid = findViewById(R.id.edit_video_bt);
        editPhoto = findViewById(R.id.edit_photo_bt);
        studio = findViewById(R.id.studio_bt);

    }

    public void openDraft(View view) {
        startActivity(new Intent(MainActivity.this,DraftActivity.class));
    }
    public static void mapAdViewToLayout(UnifiedNativeAd nativeAd, UnifiedNativeAdView adView) {
        adView.setMediaView((MediaView) adView.findViewById(R.id.ad_media));

        // Register the view used for each individual asset.
        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView.setBodyView(adView.findViewById(R.id.ad_body));
        adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
        adView.setIconView(adView.findViewById(R.id.ad_icon));
        adView.setPriceView(adView.findViewById(R.id.ad_price));
        adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
        adView.setStoreView(adView.findViewById(R.id.ad_store));
        adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));


        ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());
        ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
        ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());

        NativeAd.Image icon = nativeAd.getIcon();

        if (icon == null) {
            adView.getIconView().setVisibility(View.INVISIBLE);
        } else {
            ((ImageView) adView.getIconView()).setImageDrawable(icon.getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getPrice() == null) {
            adView.getPriceView().setVisibility(View.INVISIBLE);
        } else {
            adView.getPriceView().setVisibility(View.VISIBLE);
            ((TextView) adView.getPriceView()).setText(nativeAd.getPrice());
        }

        if (nativeAd.getStore() == null) {
            adView.getStoreView().setVisibility(View.INVISIBLE);
        } else {
            adView.getStoreView().setVisibility(View.VISIBLE);
            ((TextView) adView.getStoreView()).setText(nativeAd.getStore());
        }

        if (nativeAd.getStarRating() == null) {
            adView.getStarRatingView().setVisibility(View.INVISIBLE);
        } else {
            ((RatingBar) adView.getStarRatingView())
                    .setRating(nativeAd.getStarRating().floatValue());
            adView.getStarRatingView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getAdvertiser() == null) {
            adView.getAdvertiserView().setVisibility(View.INVISIBLE);
        } else {
            ((TextView) adView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
            adView.getAdvertiserView().setVisibility(View.VISIBLE);
        }

        adView.setNativeAd(nativeAd);
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
