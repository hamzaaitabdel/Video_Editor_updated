package com.example.videoeditor.UI.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.videoeditor.Entities.Music;
import com.example.videoeditor.R;
import com.example.videoeditor.UI.activities.MainActivity;
import com.example.videoeditor.UI.activities.MusicActivity;

import java.io.File;

import static com.example.videoeditor.Utils.MediaUtils.formatTime;

public class MusicFragment extends Fragment {
    private LinearLayout openMusic,musicContainer;
    private OnMusicSelect onMusicSelect;
    private ImageView removeMusic;
    private Music music;
    private TextView musicName,musicDur;

    public MusicFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        onMusicSelect = (OnMusicSelect) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_music, container, false);
        openMusic = v.findViewById(R.id.open_music);
        removeMusic = v.findViewById(R.id.remove_music_frag);
        musicName = v.findViewById(R.id.music_name_frag);
        musicDur = v.findViewById(R.id.music_duration_frag);
        musicContainer = v.findViewById(R.id.music_container);

        music = new Music("Default Music",getDefaultMusic(),62000);
        setMusic();

        openMusic.setOnClickListener(view -> {
            startActivity(new Intent(getContext(), MusicActivity.class));
            onMusicSelect.onAddClick();
        });
        removeMusic.setOnClickListener(view -> {
            musicContainer.setVisibility(View.GONE);
            onMusicSelect.onMusicSelect(new Music(null, null, 0));
        });
        musicContainer.setOnClickListener(view -> {
            onMusicSelect.onMusicSelect(music);
        });
        return v;
    }

    public interface OnMusicSelect {
        void onMusicSelect(Music music);
        void onAddClick();
    }

    private void setMusic(){
        musicContainer.setVisibility(View.VISIBLE);
        musicName.setText(music.getName());
        musicDur.setText(formatTime(music.getDuration()));
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences preferences = getContext().getSharedPreferences("music_select", Context.MODE_PRIVATE);
        boolean isChanged = preferences.getBoolean("is_music_changed", false);
        if (isChanged) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("is_music_changed", false);
            editor.apply();
            String name = preferences.getString("music_name", "Default Music");
            String path = preferences.getString("music_path", "Default Music");
            int duration = preferences.getInt("music_duration", 62000);
            music = new Music(name,path,duration);
            setMusic();
            onMusicSelect.onMusicSelect(music);
        }
    }
    private String getDefaultMusic() {
        File file = new File(Environment.getExternalStorageDirectory(), getString(R.string.app_name));
        if (!file.exists()) {
            file.mkdir();
        }
        File mFile = new File(file,"Music");
        if (!mFile.exists()) {
            mFile.mkdir();
        }
        File musicF = new File(mFile,"Default Music1.mp3");
        if(musicF.exists()){
            return musicF.getAbsolutePath();
        }
        return null;
    }

}