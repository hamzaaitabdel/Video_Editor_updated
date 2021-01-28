package com.example.videoeditor.UI.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import com.example.videoeditor.R;


public class TimeFragment extends Fragment {
    private SeekBar seekBar;
    private OnTimeSelect onTimeSelect;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        onTimeSelect = (OnTimeSelect) context;
    }

    public TimeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_time, container, false);
        seekBar = v.findViewById(R.id.time_seekbar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                onTimeSelect.onTimeSelect(seekBar.getProgress());
            }
        });
        return v;
    }

    public interface OnTimeSelect{
        void onTimeSelect(int time);
    }
}