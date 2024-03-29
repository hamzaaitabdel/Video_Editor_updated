package com.example.videoeditor.Utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.applovin.sdk.AppLovinPrivacySettings;
import com.example.videoeditor.R;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.NativeAdBase;
import com.facebook.ads.NativeAdListener;
import com.facebook.ads.NativeAdView;
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
import com.google.android.gms.ads.formats.NativeAd;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;
import com.unity3d.ads.metadata.MetaData;
import com.vungle.mediation.VungleConsent;
import com.vungle.warren.Vungle;

import java.net.MalformedURLException;
import java.net.URL;

import static android.content.Context.MODE_PRIVATE;

public class AdUtils {


    public static AdSize getAdSize(Context context) {

        float widthPixels = context.getResources().getDisplayMetrics().widthPixels;
        float density = context.getResources().getDisplayMetrics().density;

        int adWidth = (int) (widthPixels / density);
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(context, adWidth);
    }

    public static void loadBanner(Context context, FrameLayout frameLayout) {
        if (Provider.selected != -1) {
            AdView adView = new AdView(context);
            AdRequest adRequest = new AdRequest.Builder().build();
            adView.setAdSize(getAdSize(context));
            adView.setAdUnitId(Provider.bannerId);
            adView.loadAd(adRequest);
            frameLayout.removeAllViews();
            frameLayout.addView(adView);
        } else {
            com.facebook.ads.AdView fb = new com.facebook.ads.AdView(context, Provider.bannerFb, com.facebook.ads.AdSize.BANNER_HEIGHT_50);
            fb.loadAd();
            frameLayout.removeAllViews();
            frameLayout.addView(fb);
        }
    }

    public static void loadNative(Context context, FrameLayout frameLayout) {
        if (Provider.selected != -1) {
            AdLoader adLoader = new AdLoader.Builder(context, Provider.nativeId)
                    .forUnifiedNativeAd(unifiedNativeAd -> {
                        UnifiedNativeAdView adView = (UnifiedNativeAdView) ((Activity) context).getLayoutInflater().inflate(R.layout.native_layout_white, null);
                        mapAdViewToLayout(unifiedNativeAd, adView);
                        frameLayout.removeAllViews();
                        frameLayout.addView(adView);
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
        } else {
            loadNativeAd(context, frameLayout);
        }
    }

    public static void mapAdViewToLayout(UnifiedNativeAd nativeAd, UnifiedNativeAdView adView) {
        adView.setMediaView(adView.findViewById(R.id.ad_media));

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

    public static class Inters {
        InterstitialAd admob;
        com.facebook.ads.InterstitialAd fb;
        Context context;
        public Inters(Context context, boolean reload) {
            this.context = context;
            if (Provider.selected != -1) {
                admob = new InterstitialAd(context);
                admob.setAdUnitId(Provider.intersId);
                if (reload) {
                    admob.setAdListener(new AdListener() {
                        @Override
                        public void onAdClosed() {
                            super.onAdClosed();
                            admob.loadAd(new AdRequest.Builder().build());
                        }
                    });
                }
                admob.loadAd(new AdRequest.Builder().build());
            } else {
                fb = new com.facebook.ads.InterstitialAd(context,Provider.intersFb);
                fb.loadAd(
                        fb.buildLoadAdConfig()
                                .build());
            }
        }

        public void show() {
            if (Provider.selected != -1) {
                if (admob != null && admob.isLoaded()) {
                    admob.show();
                }
            } else {
                if (fb != null && fb.isAdLoaded()) {
                    fb.show();
                    fb = new com.facebook.ads.InterstitialAd(context,Provider.intersFb);
                    fb.loadAd(fb.buildLoadAdConfig().build());
                }
            }

        }
    }
    public static void loadNativeAd(Context c,FrameLayout frameLayout) {
        com.facebook.ads.NativeAd nativeAd = new com.facebook.ads.NativeAd(c, "YOUR_PLACEMENT_ID");

        NativeAdListener nativeAdListener = new NativeAdListener() {
            @Override
            public void onMediaDownloaded(Ad ad) {

            }

            @Override
            public void onError(Ad ad, AdError adError) {

            }

            @Override
            public void onAdLoaded(Ad ad) {
                // Render the Native Ad Template
                View adView = NativeAdView.render(c, nativeAd);
                frameLayout.removeAllViews();
                frameLayout.addView(adView);
            }

            @Override
            public void onAdClicked(Ad ad) {

            }

            @Override
            public void onLoggingImpression(Ad ad) {

            }
        };
        nativeAd.loadAd(nativeAd.buildLoadAdConfig()
                .withAdListener(nativeAdListener)
                .withMediaCacheFlag(NativeAdBase.MediaCacheFlag.ALL)
                .build());
    }
    public static class Gdpr{
        private ConsentForm form;
        public Gdpr(Context context){
            //applovin
            AppLovinPrivacySettings.setHasUserConsent(true, context.getApplicationContext());
            //Facebook
            //Unity
            MetaData gdprMetaData = new MetaData(context);
            gdprMetaData.set("gdpr.consent", true);
            gdprMetaData.commit();
            //Vungle
            VungleConsent.updateConsentStatus(Vungle.Consent.OPTED_IN, "1.0.0");
            //admob
            ConsentInformation consentInformation = ConsentInformation.getInstance(context);
            String[] publisherIds = {context.getSharedPreferences("privacy",MODE_PRIVATE).getString("pub_id",context.getString(R.string.pub_id))};
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
                privacyUrl = new URL(context.getSharedPreferences("privacy",MODE_PRIVATE).getString("gdpr_privacy",context.getString(R.string.gdpr_privacy)));
            } catch (MalformedURLException e) {
                e.printStackTrace();
                // Handle error.
            }
            form = new ConsentForm.Builder(context, privacyUrl)
                    .withListener(new ConsentFormListener() {
                        @Override
                        public void onConsentFormLoaded() {
                            // Consent form loaded successfully.
                            form.show();
                        }

                        @Override
                        public void onConsentFormOpened() {
                        }

                        @Override
                        public void onConsentFormClosed(ConsentStatus consentStatus, Boolean userPrefersAdFree) {
                        }

                        @Override
                        public void onConsentFormError(String errorDescription) {
                        }
                    })
                    .withPersonalizedAdsOption()
                    .withNonPersonalizedAdsOption()
                    .withAdFreeOption()
                    .build();
            form.load();
        }
    }
}
