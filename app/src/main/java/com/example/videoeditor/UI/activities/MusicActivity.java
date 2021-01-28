package com.example.videoeditor.UI.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.videoeditor.Adapters.EditorPagerAdapter;
import com.example.videoeditor.Adapters.MusicAdapter;
import com.example.videoeditor.Database.MusicViewModel;
import com.example.videoeditor.Entities.Music;
import com.example.videoeditor.R;
import com.example.videoeditor.UI.fragments.MusicLibFragment;
import com.example.videoeditor.Utils.AdUtils;
import com.example.videoeditor.Utils.CheapSoundFile;
import com.example.videoeditor.Utils.MediaUtils;
import com.example.videoeditor.Utils.Provider;
import com.example.videoeditor.VideoApplication;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.material.tabs.TabLayout;

import org.florescu.android.rangeseekbar.RangeSeekBar;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.example.videoeditor.Utils.MediaUtils.formatTime;

public class MusicActivity extends AppCompatActivity implements MusicLibFragment.OnMusicSelect {
    private TextView totalTime;

    private ImageView selectMusic,playBt;
    private LinearLayout controlCenter;
    private RangeSeekBar rangeSeekBar;
    private MediaPlayer mediaPlayer;
    private int min=0,max=0;
    private Music music;
    private TabLayout tabs;
    private ViewPager pager;
    private List<Music> recents;
    private MusicViewModel viewModel;
    private AdUtils.Inters mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);
        init();
        initAds();
        setupTabs();
        mediaPlayer = new MediaPlayer();
        playBt.setOnClickListener(view -> {
            if (mediaPlayer.isPlaying()){
                mediaPlayer.pause();
                playBt.setImageResource(R.drawable.ic_play);
            }else{
                playBt.setImageResource(R.drawable.ic_pause);
                mediaPlayer.start();
            }
        });
        getRecents();
        rangeSeekBar.setOnRangeSeekBarChangeListener((bar, minValue, maxValue) -> {
            if(min!=(int)minValue){
                mediaPlayer.seekTo(((int)minValue)*1000);
            }
            max = (int) maxValue;
            min = (int) minValue;
            totalTime.setText(formatTime((max-min)*1000));
        });

        selectMusic.setOnClickListener(view -> {
            String music_name=music.getName();

            if((max-min)==music.getDuration()/1000){
                SharedPreferences.Editor editor = getSharedPreferences("music_select", Context.MODE_PRIVATE).edit();
                editor.putBoolean("is_music_changed", true);
                editor.putString("music_name", music_name);
                editor.putString("music_path", music.getPath());
                editor.putInt("music_duration", music.getDuration());
                editor.apply();
                if(musicNotFound(music_name,music.getDuration())){
                    viewModel.insertMusic(music);
                }
                    mInterstitialAd.show();

                finish();
            }else{
                String dstFile = Environment.getExternalStorageDirectory() + "/" + getString(R.string.app_name) +"/"+
                        "/" +"Music" +"/"+
                        music.getName().substring(0,15)+"-Trim"+new Date().getTime()
                        + ".mp3";
                try {
                    CheapSoundFile soundFile = CheapSoundFile.create(music.getPath(), fractionComplete -> true);
                    soundFile.WriteFile(new File(dstFile),min*40,(max-min)*40);
                    SharedPreferences.Editor editor = getSharedPreferences("music_select", Context.MODE_PRIVATE).edit();
                    editor.putBoolean("is_music_changed", true);
                    editor.putString("music_name", music_name);
                    editor.putString("music_path", dstFile);
                    editor.putInt("music_duration", (max-min)*1000);
                    editor.apply();
                    if(musicNotFound(music_name,music.getDuration())){
                        viewModel.insertMusic(music);
                    }
                    finish();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        });
    }

    private boolean musicNotFound(String music_name, int duration) {
        for(Music m : recents){
            if(m.getDuration()==duration && music_name.equals(m.getName())){
                return false;
            }
        }
        return true;
    }

    private void getRecents() {
        viewModel = new ViewModelProvider(this,new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(MusicViewModel.class);
        viewModel.getMusics().observe(this, musicList -> {
            recents = musicList;

        });
    }
    private void initAds() {
        //ads
        mInterstitialAd = new AdUtils.Inters(this,false);
    }
    private void setupTabs() {
        tabs.setupWithViewPager(pager);
        EditorPagerAdapter adapter =new EditorPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(MusicLibFragment.newInstance(0),"Recent");
        adapter.addFrag(MusicLibFragment.newInstance(1),"Library");
        adapter.addFrag(MusicLibFragment.newInstance(2),"My Music");
        pager.setCurrentItem(1);
        pager.setAdapter(adapter);
    }


    private void init() {
        totalTime = findViewById(R.id.music_total_time);
        selectMusic = findViewById(R.id.select_music_bt);
        playBt = findViewById(R.id.music_play_bt);
        controlCenter = findViewById(R.id.control_center_music);
        rangeSeekBar = findViewById(R.id.music_seekbar);
        tabs = findViewById(R.id.musics_tabs);
        pager =findViewById(R.id.music_pager);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mediaPlayer.isPlaying()){
            mediaPlayer.stop();
        }
    }

    public void finishActivity(View view) {
        onBackPressed();
    }

    @Override
    public void onMusicSelect(Music music) {
        try {
            mediaPlayer.release();
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(MusicActivity.this, Uri.parse(music.getPath()));
            mediaPlayer.prepare();
            mediaPlayer.start();
            playBt.setImageResource(R.drawable.ic_pause);
        } catch (IOException e) {
            e.printStackTrace();
        }
        max = music.getDuration()/1000;
        rangeSeekBar.setRangeValues(0,max);
        rangeSeekBar.setSelectedMaxValue(max);
        rangeSeekBar.setSelectedMinValue(0);
        totalTime.setText(formatTime(max*1000));
        this.music = music;
    }
}