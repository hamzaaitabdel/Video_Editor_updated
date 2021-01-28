package com.example.videoeditor.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.videoeditor.R;
import com.example.videoeditor.Utils.Provider;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;

import static com.example.videoeditor.dialogs.ImageDialog.mapAdViewToLayout1;

public class CustomDialog extends Dialog {
    private Activity activity;
    private OnDialogSaveClick onDialogSaveClick;
    private UnifiedNativeAdView adView1;
    private boolean repeate =false;
    public CustomDialog(@NonNull Context context,OnDialogSaveClick onDialogSaveClick) {
        super(context);
        this.activity = (Activity) context;
        this.onDialogSaveClick = onDialogSaveClick;
        initNative(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_dialog_layout);
        TextView cancel = findViewById(R.id.cancel_dialog_bt);
        Button save = findViewById(R.id.save_to_draft_bt);
        getWindow().setLayout((int) (activity.getResources().getDisplayMetrics().widthPixels*.9), ViewGroup.LayoutParams.WRAP_CONTENT);
        Button exit = findViewById(R.id.exit_dialog_bt);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        if(repeate){
            FrameLayout frameLayout = findViewById(R.id.banner_container_dialog);
            frameLayout.removeAllViews();
            frameLayout.addView(adView1);
        }
        save.setOnClickListener(view -> {
                  onDialogSaveClick.onDialogSaveClick();
        });
        exit.setOnClickListener(view -> {
            dismiss();
            activity.finish();
        });
        cancel.setOnClickListener(view -> {
            dismiss();
        });
    }
    public interface OnDialogSaveClick{
        void onDialogSaveClick();
    }
    private void initNative(Context context) {
        //native
        AdLoader adLoader = new AdLoader.Builder(context, Provider.nativeId)
                .forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
                    @Override
                    public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                        UnifiedNativeAdView adView = (UnifiedNativeAdView) getLayoutInflater().inflate(R.layout.native_ad_small_layout,null);
                        mapAdViewToLayout1(unifiedNativeAd,adView);
                        FrameLayout frameLayout = findViewById(R.id.banner_container_dialog);
                        if(frameLayout!=null){
                            frameLayout.removeAllViews();
                            frameLayout.addView(adView);
                        }
                        else{
                            adView1=adView;
                            repeate=true;
                        }
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
}
