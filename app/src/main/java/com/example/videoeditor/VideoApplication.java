package com.example.videoeditor;

import android.Manifest;
import android.app.Application;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.util.Log;


import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.videoeditor.UI.activities.SplashActivity;
import com.example.videoeditor.Utils.MediaUtils;
import com.example.videoeditor.Utils.Provider;
import com.facebook.ads.AdSettings;
import com.facebook.ads.AudienceNetworkAds;
import com.google.android.gms.ads.MobileAds;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.example.videoeditor.UI.fragments.PhotoEditFragment.getKey;

public class VideoApplication extends Application {
    public ExecutorService executorService = Executors.newFixedThreadPool(5);

    @Override
    public void onCreate() {
        super.onCreate();

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = null;
        try {
            request = new StringRequest(Request.Method.GET, getKey(), response -> {
                try {
                    Log.e("test",response);
                    JSONObject object = new JSONObject(response);
                    Provider.selected = object.getInt("selected");
                    if(Provider.selected!=-1) {
                        Provider.intersId = object.getJSONArray("inters").getString(Provider.selected);
                        Provider.bannerId = object.getJSONArray("banner").getString(Provider.selected);
                        Provider.nativeId = object.getJSONArray("native").getString(Provider.selected);
                        Provider.appId = object.getJSONArray("app_id").getString(Provider.selected);
                        MobileAds.initialize(this, Provider.appId);
                    }else{
                        Provider.intersFb = object.getString("inters_fb");
                        Provider.bannerFb = object.getString("banner_fb");
                        Provider.nativeFb = object.getString("native_fb");
                        AudienceNetworkAds.initialize(this);
                        //todo remove test ads
                        AdSettings.setTestMode(true);
                    }
                    //news
                    getSharedPreferences("more_link",MODE_PRIVATE).edit()
                            .putString("link",object.getString("more_apps"))
                            .apply();
                    Provider.appId = object.getString("app_id");
                    MobileAds.initialize(this,Provider.appId);
                    getSharedPreferences("privacy",MODE_PRIVATE).edit()
                            .putString("privacy",object.getString("privacy"))
                            .putString("gdpr_privacy",object.getString("gdpr_privacy"))
                            .putString("pub_id",object.getString("pub_id"))
                            .apply();
                    getSharedPreferences("rate",MODE_PRIVATE).edit()
                            .putString("rate_url",object.getString("rate_url"))
                            .putString("rate_url2",object.getString("rate_url2"))
                            .putString("rate_title",object.getString("rate_title"))
                            .putString("rate_msg",object.getString("rate_msg"))
                            .putInt("rate_min",object.getInt("rate_min"))
                            .putString("donate_url",object.getString("donate_url"))
                            .putString("donate_img",object.getString("donate_img"))
                            .putBoolean("donate_show",object.getBoolean("donate_show"))
                            .putString("donate_txt",object.getString("donate_txt"))
                            .apply();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }, error -> {
            });
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        queue.add(request);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
        }else{

            if(!getSharedPreferences("default_music",MODE_PRIVATE).getBoolean("is_added",false)) {
                try {
                    MediaUtils.saveResMusic(this, R.raw.music_default, "Default music1", (n) -> {
                    });
                    getSharedPreferences("default_music", MODE_PRIVATE).edit().putBoolean("is_added", true).apply();
                } catch (Exception e) {

                }
            }else{

            }
        }
    }
}
