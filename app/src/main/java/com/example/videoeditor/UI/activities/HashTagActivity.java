package com.example.videoeditor.UI.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.videoeditor.R;
import com.example.videoeditor.Utils.Provider;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;

import static com.example.videoeditor.UI.activities.MainActivity.mapAdViewToLayout;
import static com.example.videoeditor.Utils.AdUtils.loadNative;

public class HashTagActivity extends AppCompatActivity {
    private Button btnCopy;
    private TextView htvHashtag;
    private TextView txtHashtag;
    String txtfollowers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hash_tag);
        initView();
        initNative();
    }
    private void initView() {
        txtHashtag = findViewById(R.id.txtHashtag);
        htvHashtag = findViewById(R.id.htvHashtag);
        btnCopy =findViewById(R.id.btnCopy);
        btnCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ClipboardManager) HashTagActivity.this.getSystemService(CLIPBOARD_SERVICE)).setPrimaryClip(ClipData.newPlainText("label", HashTagActivity.this.txtfollowers));
                Toast.makeText(HashTagActivity.this, "Hashtags copied", Toast.LENGTH_SHORT).show();
                final Dialog dialog = new Dialog(HashTagActivity.this);
                dialog.requestWindowFeature(1);
                dialog.setCancelable(false);
                dialog.setContentView((int) R.layout.step2_hashtag_dialog);
                ((Button) dialog.findViewById(R.id.btnDone)).setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        HashTagActivity.this.onBackPressed();
                        dialog.dismiss();
                    }
                });
                dialog.show();
                dialog.getWindow().setLayout(-2, -2);
            }
        });
        Intent intent = getIntent();
        String valueOf = String.valueOf(intent.getSerializableExtra("followers"));
        txtfollowers = String.valueOf(intent.getSerializableExtra("hashtagfollowers"));
        txtHashtag.setText(valueOf);
        htvHashtag.setText(txtfollowers);
    }
    private void initNative() {
        //native
       FrameLayout frameLayout=findViewById(R.id.native_container);
       loadNative(this,frameLayout);
    }

}