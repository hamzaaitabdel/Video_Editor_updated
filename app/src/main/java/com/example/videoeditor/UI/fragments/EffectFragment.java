package com.example.videoeditor.UI.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.videoeditor.Adapters.SimpleImagesRvAdapter;
import com.example.videoeditor.R;

import java.util.ArrayList;
import java.util.List;

public class EffectFragment extends Fragment {
    private RecyclerView recyclerView;
    private OnEffectSelect onEffectSelect;
    private int alpha=70;
    private SeekBar seekBar;
    private int selected=0;
    private SimpleImagesRvAdapter adapter;
    private List<Integer> list;
    private int selected_res=-1;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        onEffectSelect = (OnEffectSelect) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=  inflater.inflate(R.layout.fragment_effect, container, false);
        recyclerView = v.findViewById(R.id.effect_rv);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        seekBar = v.findViewById(R.id.alpha_seekbar);
        list = new ArrayList<>();
        list.add(R.drawable.none);
        list.add(R.drawable.e1);
        list.add(R.drawable.e2);
        list.add(R.drawable.e3);
        list.add(R.drawable.e4);
        list.add(R.drawable.e8);
        list.add(R.drawable.e9);
        list.add(R.drawable.e10);
        list.add(R.drawable.e11);
        list.add(R.drawable.e12);
        list.add(R.drawable.e13);
        list.add(R.drawable.e14);
        list.add(R.drawable.e15);
        list.add(R.drawable.e16);
        list.add(R.drawable.e18);
        list.add(R.drawable.e19);
        list.add(R.drawable.e20);
        if(selected_res!=-1){
        for(int i=0;i<list.size();i++){
            if(selected_res==list.get(i)){
                selected=i;
            }
        }}
        adapter = new SimpleImagesRvAdapter(getContext(), list, i -> {

            if(list.get(i)==R.drawable.none){
                seekBar.setVisibility(View.INVISIBLE);
                onEffectSelect.onEffectSelect(-1,alpha);
                selected=-1;
            }else {
                seekBar.setVisibility(View.VISIBLE);
                onEffectSelect.onEffectSelect(list.get(i),alpha);
                selected = list.get(i);
            }
        }, false);
        adapter.setSelected(selected);
        recyclerView.setAdapter(adapter);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                alpha=seekBar.getProgress();
                onEffectSelect.onEffectSelect(selected,alpha);
            }
        });

        if(selected==0){
            seekBar.setVisibility(View.INVISIBLE);
        }else {
            seekBar.setVisibility(View.VISIBLE);
        }
        return v;
    }

    public void setSelectedRes(int res) {
        selected_res=res;
    }

    public interface OnEffectSelect{
        void onEffectSelect(int resId,int alpha);
    }
}