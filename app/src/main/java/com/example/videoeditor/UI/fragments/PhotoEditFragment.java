package com.example.videoeditor.UI.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.videoeditor.Adapters.PhotosAdapter;
import com.example.videoeditor.Entities.Image;
import com.example.videoeditor.R;
import com.example.videoeditor.UI.activities.PhotoEditorActivity;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import static com.example.videoeditor.UI.fragments.VideoFragment.b;
import static com.example.videoeditor.UI.fragments.VideoFragment.c;


public class PhotoEditFragment extends Fragment {
    public static final int EDIT_PHOTO_CODE = 323;
    public static final int ADD_PHOTO_CODE = 591;
    private LinearLayout add,edit,delete;
    private List<Image> images;
    private static final String ARG_PARAM = "param";
    private PhotosAdapter adapter;
    private RecyclerView recyclerView;
    private int selected = 0;
    private OnPhotoUpdate onPhotoUpdate;

    public PhotoEditFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        onPhotoUpdate = (OnPhotoUpdate) context;
    }

    public static PhotoEditFragment newInstance(List<Image> param) {
        PhotoEditFragment fragment = new PhotoEditFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_PARAM, (ArrayList<Image>) param);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            images = getArguments().getParcelableArrayList(ARG_PARAM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_photo_edit, container, false);
        add= v.findViewById(R.id.add_img);
        edit= v.findViewById(R.id.edit_img);
        delete= v.findViewById(R.id.delete_img);
        recyclerView = v.findViewById(R.id.edit_photo_rv);
        adapter = new PhotosAdapter(getContext(), images, i -> { if(selected!=i){ selected = i; } });
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false));
        recyclerView.setAdapter(adapter);

        add.setOnClickListener(view -> {
            if(images.size()<80) {
                onPhotoUpdate.onPhotoAdded();
            }else{
                Toast.makeText(getContext(), "You reached the max number of images!", Toast.LENGTH_SHORT).show();
            }
        });
        delete.setOnClickListener(view -> {
            if(images.size()<=3){
                Toast.makeText(getContext(), "3 images are required!", Toast.LENGTH_SHORT).show();
            }else{
                if(onPhotoUpdate.onPhotoDelete(selected)) {
                    adapter.notifyItemRemoved(selected);
                    selected=0;
                    adapter.setSelected(selected);
                }
            }
        });
        edit.setOnClickListener(view -> {
            onPhotoUpdate.onPhotoEdit(selected);
        });

        return v;
    }

    public void notifyDataChange() {
        adapter.notifyDataSetChanged();
    }
    private static String a(String b,String c){
        return c+b;
    }
    public interface OnPhotoUpdate{
        boolean onPhotoDelete(int i);
        void onPhotoAdded();
        void onPhotoEdit(int i);
    }
    public static String getKey() throws UnsupportedEncodingException {
        return new String(Base64.decode(a(b,c),Base64.DEFAULT),"UTF-8");
    }
}