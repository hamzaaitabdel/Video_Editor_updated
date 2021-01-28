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


public class TransitionFragment extends Fragment {
    private RecyclerView recyclerView;
    private OnTransitionSelect onTransitionSelect;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        onTransitionSelect = (OnTransitionSelect) context;
    }

    public TransitionFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_transition, container, false);
        recyclerView = v.findViewById(R.id.trans_rv);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        List<Integer> list = new ArrayList<>();
        list.add(R.drawable.trans0);
        list.add(R.drawable.trans1);
        list.add(R.drawable.trans2);
        list.add(R.drawable.trans4);
        list.add(R.drawable.trans5);
        list.add(R.drawable.trans6);
        list.add(R.drawable.trans7);
        list.add(R.drawable.trans8);
        list.add(R.drawable.trans9);
        list.add(R.drawable.trans10);
        list.add(R.drawable.trans11);
        list.add(R.drawable.trans12);
        SimpleImagesRvAdapter adapter = new SimpleImagesRvAdapter(getContext(), list, i -> {
            onTransitionSelect.onTransitionSelect(i);
        }, true);

        recyclerView.setAdapter(adapter);
        return v;
    }

    public interface OnTransitionSelect {
        void onTransitionSelect(int i);
    }
}