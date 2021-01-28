package com.example.videoeditor.UI.fragments;

import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.videoeditor.Adapters.MusicAdapter;
import com.example.videoeditor.Database.MusicViewModel;
import com.example.videoeditor.Entities.Music;
import com.example.videoeditor.R;
import com.example.videoeditor.UI.activities.MusicActivity;
import com.example.videoeditor.Utils.MediaUtils;
import com.example.videoeditor.VideoApplication;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.example.videoeditor.Utils.MediaUtils.formatTime;

public class MusicLibFragment extends Fragment {
    private RecyclerView recyclerView;
    private TextView empty;
    private View root;
    private List<Music> musics;
    private MusicAdapter adapter;
    private int selected=-1,music_source;
    private OnMusicSelect onMusicSelect;
    private static final String ARG_PARAM ="music_source";

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        onMusicSelect = (OnMusicSelect) context;
    }
    public static MusicLibFragment newInstance(int source) {
        MusicLibFragment fragment = new MusicLibFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM, source);
        fragment.setArguments(args);
        return fragment;
    }
    public MusicLibFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            music_source = getArguments().getInt(ARG_PARAM);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root=  inflater.inflate(R.layout.fragment_music_lib, container, false);
        init();
        musics = new ArrayList<>();
        boolean isLib = music_source == 1;
        adapter = new MusicAdapter(getContext(), musics, new MusicAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int i) {
                if(music_source!=1){
                if(i!=selected){
                    onMusicSelect.onMusicSelect(musics.get(i));
                    selected=i;
                }}else{
                    if(i!=selected&& musics.get(i).getPath()!=null){
                        onMusicSelect.onMusicSelect(musics.get(i));
                        selected=i;

                    }
                }
            }

            @Override
            public void onMusicSave(int i, String path, int dur) {
                Music m = musics.get(i);
                musics.remove(i);
                Music m2 = new Music(m.getName(),path,dur);
                m2.setRes(m.getRes());
                musics.add(i,m2);
            }
        }, isLib);
        fetchData();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false));
        recyclerView.setAdapter(adapter);

        return root;
    }

    private void init() {
        recyclerView = root.findViewById(R.id.music_rv);
        empty = root.findViewById(R.id.music_empty_view);
    }

    private void fetchData() {
        switch (music_source) {
            case 0:
                MusicViewModel viewModel = new ViewModelProvider(this,new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication())).get(MusicViewModel.class);
                viewModel.getMusics().observe(this, musicList -> {
                    musics = musicList;
                    adapter.swapData(musics);
                    getActivity().runOnUiThread(()->{
                        if(musics.size()==0){
                            empty.setVisibility(View.VISIBLE);
                        }else{
                            empty.setVisibility(View.GONE);
                        }
                    });

                });
                break;
            case 1:
                musics = new ArrayList<>();
                musics.add(getMus("Default music1","https://firebasestorage.googleapis.com/v0/b/atlascapture-b4030.appspot.com/o/Default%20music1.mp3?alt=media&token=293b554c-dd53-4e84-a5ef-c741308a0a00"));
                musics.add(getMus("Circus_Waltz_Silent_Film_Light","https://firebasestorage.googleapis.com/v0/b/atlascapture-b4030.appspot.com/o/Circus_Waltz_Silent_Film_Light.mp3?alt=media&token=19f02b97-8e49-4303-a3a9-75fcac63540e"));
                musics.add(getMus("Safety_Net","https://firebasestorage.googleapis.com/v0/b/atlascapture-b4030.appspot.com/o/Safety_Net.mp3?alt=media&token=e78e8b24-4fcc-4d0e-8317-13af73f56f0a"));
                musics.add(getMus("Mr_Sunny_Face","https://firebasestorage.googleapis.com/v0/b/atlascapture-b4030.appspot.com/o/Mr_Sunny_Face.mp3?alt=media&token=ac4a9644-f271-44f4-8583-d8be69d8ce95"));
                musics.add(getMus("Kissing_a_Cancer","https://firebasestorage.googleapis.com/v0/b/atlascapture-b4030.appspot.com/o/Kissing_a_Cancer.mp3?alt=media&token=4118c759-4f6e-411b-891b-14a3ba7343b6"));
                musics.add(getMus("Joy_to_the_World_Instrumental","https://firebasestorage.googleapis.com/v0/b/atlascapture-b4030.appspot.com/o/Joy_to_the_World_Instrumental.mp3?alt=media&token=6906ebc6-2113-4d03-a5db-1fa98f1eb348"));
                musics.add(getMus("Friendly_Day","https://firebasestorage.googleapis.com/v0/b/atlascapture-b4030.appspot.com/o/Friendly_Day.mp3?alt=media&token=ecf396c4-0457-4428-924c-4077afe6ce8b"));
                musics.add(getMus("Fluffing_a_Duck","https://firebasestorage.googleapis.com/v0/b/atlascapture-b4030.appspot.com/o/Fluffing_a_Duck.mp3?alt=media&token=07150f81-e8ce-41b1-b396-9fa4288e7e6b"));
                musics.add(getMus("Every_Step.","https://firebasestorage.googleapis.com/v0/b/atlascapture-b4030.appspot.com/o/Every_Step.mp3?alt=media&token=c507f4eb-a403-4b1c-9a03-f33bfd7dcb42"));
                adapter.swapData(musics);
                empty.setVisibility(View.GONE);
                break;
            default:
                MediaUtils.getMusicCursor(getContext(), ((VideoApplication) getActivity().getApplication()).executorService, musicList -> {
                    musics = musicList;
                    for(int i=0;i<musics.size();i++){
                        if(!new File(musics.get(i).getPath()).exists()){
                            musics.remove(i);
                        }
                    }
                    getActivity().runOnUiThread(() -> {
                        if (musics.size() != 0) {
                            empty.setVisibility(View.GONE);
                        }
                        adapter.swapData(musics);
                    });
                });
        }
    }

    private Music getMus(String name,String res) {
        Music m = new Music(name,getMusicPath(name+".mp3"),getDur(name+".mp3"));
        m.setRes(res);
        return m;
    }

    private int getDur(String name) {
        String path = getMusicPath(name);
        if(path!=null){
        Uri uri = Uri.parse(path);
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(getActivity().getApplicationContext(),uri);
        String durationStr = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        return  Integer.parseInt(durationStr);}
        return 0;
    }

    private String getMusicPath(String s) {
        String mus_path =new File(Environment.getExternalStorageDirectory(),getString(R.string.app_name)).getAbsolutePath()
                +"/Music";
        File mus = new File(mus_path,s);
        if(mus.exists()){
            return mus.getAbsolutePath();
        }
        return null;
    }

    public interface OnMusicSelect{
        void onMusicSelect(Music music);
    }
}