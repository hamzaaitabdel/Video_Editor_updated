package com.example.videoeditor.UI.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.videoeditor.Adapters.SimpleImagesRvAdapter;
import com.example.videoeditor.R;

import java.util.ArrayList;
import java.util.List;


public class FrameFragment extends Fragment {
    private RecyclerView recyclerView;
    private OnFrameSelect onFrameSelect;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        onFrameSelect = (OnFrameSelect) context;
    }

    public FrameFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_frame, container, false);
        recyclerView = v.findViewById(R.id.frame_rv);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        List<Integer> list = new ArrayList<>();
        list.add(R.drawable.preview);
        list.add(R.drawable.fr1);
        list.add(R.drawable.fr2);
        list.add(R.drawable.fr3);
        list.add(R.drawable.fr4);
        list.add(R.drawable.fr5);
        list.add(R.drawable.fr6);
        list.add(R.drawable.fr7);
        list.add(R.drawable.fr8);
        list.add(R.drawable.fr9);
        list.add(R.drawable.fr10);
        list.add(R.drawable.fr11);
        list.add(R.drawable.fr12);
        list.add(R.drawable.fr13);
        list.add(R.drawable.fr14);
        list.add(R.drawable.fr15);
        list.add(R.drawable.fr16);
        list.add(R.drawable.fr17);
        list.add(R.drawable.fr18);
        list.add(R.drawable.fr19);
        list.add(R.drawable.fr20);
        list.add(R.drawable.fr21);
        list.add(R.drawable.fr22);
        list.add(R.drawable.fr23);
        list.add(R.drawable.fr24);
        list.add(R.drawable.fr25);
        list.add(R.drawable.fr26);
        list.add(R.drawable.fr27);
        list.add(R.drawable.fr28);
        list.add(R.drawable.frr1);
        list.add(R.drawable.frr2);
        list.add(R.drawable.frr3);
        list.add(R.drawable.frr4);
        list.add(R.drawable.frr5);
        list.add(R.drawable.frr6);
        list.add(R.drawable.frr7);
        list.add(R.drawable.frr8);
        list.add(R.drawable.frr9);
        list.add(R.drawable.frr10);
        list.add(R.drawable.frr11);
        list.add(R.drawable.frr12);
        list.add(R.drawable.frr13);
        list.add(R.drawable.frr14);
        SimpleImagesRvAdapter adapter = new SimpleImagesRvAdapter(getContext(), list,
                i -> {
                if(list.get(i)==R.drawable.preview){
                    onFrameSelect.onFrameSelect(-1);
                }else {
                    onFrameSelect.onFrameSelect(list.get(i));
                }
                }, true);
        recyclerView.setAdapter(adapter);
        return v;
    }

    public interface OnFrameSelect {
        void onFrameSelect(int frame);
    }
}