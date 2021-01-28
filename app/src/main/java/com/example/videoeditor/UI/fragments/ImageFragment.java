package com.example.videoeditor.UI.fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.videoeditor.Adapters.StudioAdapter;
import com.example.videoeditor.Entities.StudioItem;
import com.example.videoeditor.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.example.videoeditor.UI.fragments.VideoFragment.getDetails;

public class ImageFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<StudioItem> studioItems;
    private TextView emptyView;
    private StudioAdapter studioAdapter;

    public ImageFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_image, container, false);
        studioItems =new ArrayList<>();
        recyclerView = v.findViewById(R.id.rv_Studio_Image);
        emptyView = v.findViewById(R.id.image_empty_view);
        studioAdapter = new StudioAdapter(getContext(), studioItems, new StudioAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int i) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                File itemSharew = new File(studioItems.get(i).getPath());
                Uri uriw = FileProvider.getUriForFile(getContext().getApplicationContext(), getContext().getPackageName()+".fileprovider", itemSharew);
                intent.setDataAndType(uriw, "image/*");
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivity(intent);
            }

            @Override
            public void onMenuClick(int i) {
                OptionListDialogFragment optionListDialogFragment = new OptionListDialogFragment();
                optionListDialogFragment.setOnOptionSelect(option -> {
                    StudioItem item = studioItems.get(i);
                    switch (option) {
                        case "Open with...":
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            File itemSharew = new File(item.getPath());
                            Uri uriw = FileProvider.getUriForFile(getContext().getApplicationContext(), getContext().getPackageName()+".fileprovider", itemSharew);
                            intent.setDataAndType(uriw, "image/*");
                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            startActivity(intent);
                            break;
                        case "Rename":
                            EditText editText =new EditText(getContext());
                            new AlertDialog.Builder(getContext())
                                    .setView(editText)
                                    .setTitle("Rename")
                                    .setPositiveButton("Rename", (dialogInterface, i1) -> {
                                        File f = new File(item.getPath());
                                        File dest = new File(f.getParentFile(),editText.getText().toString()+".png");
                                        if(dest.exists()){
                                            Toast.makeText(getContext(), "File already exist! Please choose other name!", Toast.LENGTH_SHORT).show();
                                        }else {
                                            if (f.renameTo(dest)) {
                                                Toast.makeText(getContext(), "Image Renamed Successfully!", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(getContext(), "Something wrong, please try again!", Toast.LENGTH_SHORT).show();
                                            }
                                            fetchData();
                                        }
                                    })
                                    .setNegativeButton("Cancel",null)
                                    .show();
                            break;
                        case "Delete":
                            new AlertDialog.Builder(getContext())
                                    .setTitle("Delete")
                                    .setMessage("Are you sure you want delete this item?")
                                    .setPositiveButton("Delete", (dialogInterface, i1) -> {
                                        File file = new File(item.getPath());
                                        if(file.delete()){
                                            Toast.makeText(getContext(), "Image Deleted Successfully!", Toast.LENGTH_SHORT).show();
                                        }else {
                                            Toast.makeText(getContext(), "Something wrong, please try again!", Toast.LENGTH_SHORT).show();
                                        }
                                        fetchData();
                                    })
                                    .setNegativeButton("Cancel",null)
                                    .show();
                            break;
                        case "Share":
                            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                            sharingIntent.setType("image/*");
                            File itemShare = new File(item.getPath());
                            Uri uri = FileProvider.getUriForFile(getContext().getApplicationContext(), getContext().getPackageName()+".fileprovider", itemShare);
                            sharingIntent.putExtra(Intent.EXTRA_STREAM, uri);
                            startActivity(Intent.createChooser(sharingIntent,"Share Image"));
                            break;
                        case "Detail":
                            new AlertDialog.Builder(getContext())
                                    .setTitle("Detail")
                                    .setMessage(getDetails(item,false))
                                    .setPositiveButton("Ok",null)
                                    .show();
                            break;

                    }
                });
                optionListDialogFragment.show(getFragmentManager(), "video_options");
            }
        },false);
        fetchData();
        setEmptyView();
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        recyclerView.setAdapter(studioAdapter);
        return v;
    }
    private void fetchData() {
        studioItems = new ArrayList<>();
        File file = new File(Environment.getExternalStorageDirectory(),getString(R.string.app_name));
        if(!file.exists()){
            file.mkdir();
        }
        File img_file = new File(file,"Photo Editor");
        if(!img_file.exists())
            img_file.mkdir();
        for(File child : img_file.listFiles()){
            studioItems.add(new StudioItem(child.getAbsolutePath(), (String) DateFormat.format("dd-MM-yyyy hh:mm",child.lastModified()),child.getName().substring(0,child.getName().length()-4)));
        }
        studioAdapter.swapData(studioItems);
    }
    private void setEmptyView() {
        if(studioItems.size()==0){
            emptyView.setVisibility(VISIBLE);
            recyclerView.setVisibility(GONE);
        }else{
            emptyView.setVisibility(GONE);
            recyclerView.setVisibility(VISIBLE);
        }
    }
}